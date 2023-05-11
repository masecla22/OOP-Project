package nl.rug.oop.rpg.player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.interaction.DialogInteraction;
import nl.rug.oop.rpg.map.behaviours.Fightable;
import nl.rug.oop.rpg.map.entities.NPC;
import nl.rug.oop.rpg.map.objects.Door;
import nl.rug.oop.rpg.map.objects.Room;

@Data
@RequiredArgsConstructor
public class Player implements Fightable {
    @NonNull
    private Game game;

    @NonNull
    private String name;

    @NonNull
    private Room currentlyIn;

    private double health = 100;

    public String getFormattedHealth() {
        return String.format("%.2f", this.health);
    }

    @Override
    public void receiveDamage(Fightable from, double damage) {
        health -= damage;
        System.out.println("You took " + String.format("%.2f", damage) + " damage!");
    }

    private double damage = 5;

    @Override
    public void attack(Fightable enemy, double damage) {
        Fightable.super.attack(enemy, damage);

        System.out.println("You dealt " + damage + " damage!");
    }

    public double punch(Fightable enemy) {
        this.attack(enemy, damage);
        return damage;
    }

    public double headButt(Fightable enemy) {
        this.attack(enemy, damage * 1.5);
        this.health-=10;
        System.out.println("Your head hurts and you loose 10HP");
        return damage * 1.5;
    }

    public double dropKick(Fightable enemy) {
        this.attack(enemy, damage * 3.0);
        if (ThreadLocalRandom.current().nextDouble() < 0.5){
            this.health -= 35;
            System.out.println("Your leg hurts very badly and you loose 35HP");
        }

        return damage * 3.0;
    }

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

    public void handleInspect() {
        this.currentlyIn.inspect();
    }

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

        for (Door door : doors)
            interaction.option(door.getDescription(), () -> door.interact(this));

        interaction.interact();
    }

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

        for (NPC npc : this.currentlyIn.getNpcs())
            interaction.option(npc.getDescription(), () -> npc.interact(this));

        interaction.interact();
    }
}
