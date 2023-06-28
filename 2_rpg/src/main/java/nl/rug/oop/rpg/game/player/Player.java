package nl.rug.oop.rpg.game.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.behaviours.Fightable;
import nl.rug.oop.rpg.game.entities.NPC;
import nl.rug.oop.rpg.game.items.Item;
import nl.rug.oop.rpg.game.items.ModifierItem;
import nl.rug.oop.rpg.game.objects.Door;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.interaction.DialogInteraction;

/**
 * The player class.
 */
@Data
@RequiredArgsConstructor
public class Player implements Fightable, Serializable {
    /** Serial version ID. */
    private static final long serialVersionUID = 1249087623489l;

    /** The Game of this player. */
    @NonNull
    private Game game;

    /** The name of this player. */
    @NonNull
    private String name;

    /** The room this player is currently in. */
    @NonNull
    private Room currentlyIn;

    /** The health for the player. */
    private double health = 100;

    /** The damage this player deals. */
    private double damage = 5;

    /** How much gold the player has. */
    private int gold = 0;

    /** The inventory of this player. */
    private PlayerInventory inventory;

    public void initialize() {
        inventory = new PlayerInventory(game, new ArrayList<>());
    }

    /**
     * Returns the health as a formatted string.
     * 
     * @return - the formatted health
     */
    public String getFormattedHealth() {
        return String.format("%.2f", this.health);
    }

    /**
     * Receives damage from an entity.
     * 
     * @param from   - the entity that attacks
     * @param damage - the damage to deal
     */
    @Override
    public void receiveDamage(Fightable from, double damage) {
        List<Item> items = this.getInventory().getItems();

        // Sort alphabetically to maintain consistency
        Collections.sort(items, Comparator.comparing(c -> c.getName()));

        for (Item item : items) {
            if (item instanceof ModifierItem modifier) {
                damage = modifier.modifyIncomingDamage(damage);
            }
        }

        health -= damage;
        System.out.println("You took " + String.format("%.2f", damage) + " damage!");
    }

    /**
     * Attacks the enemy.
     * 
     * @param enemy  - the entity to attack
     * @param damage - the damage to deal
     */
    @Override
    public void attack(Fightable enemy, double damage) {
        List<Item> items = this.getInventory().getItems();

        // Sort alphabetically to maintain consistency
        Collections.sort(items, Comparator.comparing(c -> c.getName()));

        for (Item item : items) {
            if (item instanceof ModifierItem modifier) {
                damage = modifier.modifyOutgoingDamage(damage);
            }
        }

        Fightable.super.attack(enemy, damage);

        System.out.println("You dealt " + damage + " damage!");
    }

    /**
     * Punches the enemy.
     * 
     * @param enemy - the enemy
     * @return - the amount of damage
     */
    public double punch(Fightable enemy) {
        this.attack(enemy, damage);
        return damage;
    }

    /**
     * Headbutts the enemy.
     * 
     * @param enemy - the enemy
     * @return - the amount of damage
     */
    public double headButt(Fightable enemy) {
        this.attack(enemy, damage * 1.5);
        this.health -= 10;
        System.out.println("Your head hurts and you loose 10HP");
        return damage * 1.5;
    }

    /**
     * Dropkicks the enemy.
     * 
     * @param enemy - the enemy
     * @return - the amount of damage
     */
    public double dropKick(Fightable enemy) {
        this.attack(enemy, damage * 3.0);
        if (ThreadLocalRandom.current().nextDouble() < 0.5) {
            this.health -= 35;
            System.out.println("Your leg hurts very badly and you loose 35HP");
        }

        return damage * 3.0;
    }

    /**
     * Applies the next attack.
     * 
     * @param enemy - the enemy
     * @return - the amount of damage
     */
    @Override
    public double applyNextAttack(Fightable enemy) {
        AtomicReference<Double> appliedDamage = new AtomicReference<>(0.0);

        this.game.newInteraction()
                .prompt("You look to use your next attack!\nHP: "
                        + this.getFormattedHealth() + " DMG: " + this.damage + "\nEnemy HP: " + enemy.getHealth())
                .option("Punch the enemy (" + damage + " damage) ",
                        () -> appliedDamage.set(this.punch(enemy)))
                .option("Headbutt the enemy (" + damage * 1.5 + " damage, -10HP)",
                        () -> appliedDamage.set(this.headButt(enemy)))
                .option("Dropkick the enemy (" + damage * 3.0 + " damage, 50% chance to loose 35HP)",
                        () -> appliedDamage.set(this.dropKick(enemy)))
                .interact();

        return appliedDamage.get();
    }

    /**
     * Handles the player looking around the room.
     */
    public void handleInspect() {
        this.currentlyIn.inspect();
    }

    /**
     * Handles the player looking for doors around him.
     */
    public void handleLookWayOut() {
        List<Door> doors = this.currentlyIn.getDoors();
        if (doors.isEmpty()) {
            this.game.newInteraction()
                    .prompt("You look around for doors. Your chances look grim as you find none.")
                    .interact();
            return;
        }

        DialogInteraction interaction = this.game.newInteraction()
                .prompt("You look around for doors. You see: ")
                .cursor("Which door do you take? (-1 : stay here)");

        for (Door door : doors) {
            interaction.option(door.getDescription(), () -> door.interact(this));
        }

        interaction.interact();
    }

    /**
     * Handles the player looking for NPCs in the room.
     */
    public void handleLookForCompany() {
        if (this.currentlyIn.getNpcs().isEmpty()) {
            this.game.newInteraction()
                    .prompt("You look for company yet find only more loneliness within yourself.")
                    .interact();

            return;
        }

        DialogInteraction interaction = this.game.newInteraction()
                .prompt("You look if anyone's here.\nYou see: ")
                .cursor("Interact ? (-1 : do nothing)");

        for (NPC npc : this.currentlyIn.getNpcs()) {
            interaction.option(npc.getName(), () -> npc.interact(this));
        }

        interaction.interact();
    }

    /**
     * Handles the player checking their inventory.
     */
    public void handleCheckInventory() {
        if (this.getInventory().getItems().size() == 0) {
            this.game.newInteraction()
                    .prompt("You look at your inventory\n" + "It's empty")
                    .interact();
            return;
        }

        this.game.newInteraction()
                .prompt("You look at your inventory\n" + this.inventory.toString())
                .cursor("What do you want to do ? (-1 : go back)")
                .option("Use an item", () -> this.inventory.handleItemUse(this))
                .interact();
    }

    /**
     * Removes gold from the player.
     * 
     * @param gold - the amount of gold to remove
     */
    public void removeGold(int gold) {
        this.gold -= gold;
    }

    /**
     * Adds gold to the player.
     * 
     * @param gold - the amount of gold to add
     */
    public void addGold(int gold) {
        this.gold += gold;
    }

    /**
     * Heals a player for a certain amount of health.
     * Will apply all the modifier items of the player.
     * 
     * @param health - the amount of health to heal
     */
    public void heal(double health) {
        List<Item> items = this.getInventory().getItems();

        // Sort alphabetically to maintain consistency
        Collections.sort(items, Comparator.comparing(c -> c.getName()));

        for (Item item : items) {
            if (item instanceof ModifierItem modifier) {
                health = modifier.modifyIncomingHealing(health);
            }
        }

        this.health += health;
    }
}
