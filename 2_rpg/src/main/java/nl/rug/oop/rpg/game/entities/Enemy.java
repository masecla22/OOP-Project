package nl.rug.oop.rpg.game.entities;

import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.behaviours.Fightable;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.player.Player;
import nl.rug.oop.rpg.interaction.CombatInteraction;

/**
 * An enemy is an NPC that can fight.
 */
public class Enemy extends NPC implements Fightable {
    /** The health of this enemy. */
    @Getter
    private double health;

    /** The damage this enemy deals. */
    @Getter
    private double damage;

    /**
     * Creates a new enemy.
     * 
     * @param game        - the game
     * @param room        - the room
     * @param description - the description
     * @param name        - the name
     * @param health      - the health
     * @param damage      - the damage
     */
    public Enemy(Game game, Room room, String description, String name, double health, double damage) {
        super(game, room, description, name);
        this.health = health;
        this.damage = damage;
    }

    /**
     * Attacks the enemy.
     * 
     * @param from   - the entity that attacks
     * @param damage - the damage to deal
     */
    @Override
    public void receiveDamage(Fightable from, double damage) {
        this.health -= damage;
    }

    /**
     * Applies the next attack to the enemy.
     * 
     * @param enemy - the enemy
     * @return - the amount of damage
     */
    @Override
    public double applyNextAttack(Fightable enemy) {
        double calculatedDamage = ThreadLocalRandom.current().nextDouble(0.5, 1.5) * this.damage;
        this.attack(enemy, calculatedDamage);
        return calculatedDamage;
    }

    /**
     * Interacts with the enemy.
     * 
     * @param player - the player
     */
    @Override
    public void interact(Player player) {
        System.out.println("This NPC turns out to be quite hostile!");

        new CombatInteraction(getGame(), player, this)
                .onWin(() -> {
                    System.out.println("You have defeated the enemy! They're now gone!");
                    this.getRoom().removeNPC(this);

                    double reward = this.damage * 8;
                    reward = Math.max(0, reward);
                    reward *= ThreadLocalRandom.current().nextDouble() / 2 + 3;
                    player.addGold((int) reward);
                })
                .interact();
    }

    @Override
    public void inspect() {
        // There's no real way of inspecting an enemy, as checking them out leads into a
        // battle being started. This is just a placeholder.
        System.out.println("This enemy has " + this.health + " health and deals " + this.damage + " damage.");
    }

}
