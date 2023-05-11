package nl.rug.oop.rpg.map.entities;

import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.interaction.CombatInteraction;
import nl.rug.oop.rpg.map.behaviours.Fightable;
import nl.rug.oop.rpg.map.objects.Room;
import nl.rug.oop.rpg.player.Player;

public class Enemy extends NPC implements Fightable {
    @Getter
    private double health;

    @Getter
    private double damage;

    @Override
    public void receiveDamage(Fightable from, double damage) {
        this.health -= damage;
    }

    @Override
    public double applyNextAttack(Fightable enemy) {
        double calculatedDamage = ThreadLocalRandom.current().nextDouble(0.5, 1.5) * this.damage;
        this.attack(enemy, calculatedDamage);
        return calculatedDamage;
    }

    public Enemy(Game game, Room room, String description, double health, double damage) {
        super(game, room, description);
        this.health = health;
        this.damage = damage;
    }

    @Override
    public void interact(Player player) {
        super.interact(player);
        System.out.println("This NPC turns out to be quite hostile!");

        new CombatInteraction(getGame(), player, this)
                .onWin(() -> {
                    System.out.println("You have defeated the enemy! They're now gone!");
                    this.getRoom().removeNPC(this);
                })
                .interact();
    }

}
