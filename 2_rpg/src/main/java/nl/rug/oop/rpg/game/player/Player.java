package nl.rug.oop.rpg.game.player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.behaviours.Fightable;
import nl.rug.oop.rpg.game.entities.NPC;
import nl.rug.oop.rpg.game.objects.Door;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.interaction.DialogInteraction;

/**
 * The player class.
 */
@Data
@RequiredArgsConstructor
public class Player implements Fightable {
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
            interaction.option(npc.getDescription(), () -> npc.interact(this));
        }

        interaction.interact();
    }
}
