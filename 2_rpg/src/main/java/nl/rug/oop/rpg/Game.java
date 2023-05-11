package nl.rug.oop.rpg;

import java.util.Scanner;

import nl.rug.oop.rpg.interaction.DialogInteraction;
import nl.rug.oop.rpg.map.entities.Enemy;
import nl.rug.oop.rpg.map.entities.NPC;
import nl.rug.oop.rpg.map.objects.Door;
import nl.rug.oop.rpg.map.objects.Room;
import nl.rug.oop.rpg.player.Player;

public class Game {
    private Scanner scanner;
    private Player player;

    public void initialize() {
        this.scanner = new Scanner(System.in);
        player = new Player(this, "Player", getInitialRoom());
    }

    public void run() {
        while (true) {
            buildCurrentInteraction().interact();
        }
    }

    public DialogInteraction newInteraction() {
        return new DialogInteraction(scanner)
                .prompt("HP: " + this.player.getFormattedHealth() + ", DMG: "
                        + this.player.getDamage()
                        + "\nWhat do you want to do?");
    }

    private Room getInitialRoom() {
        Room room = new Room("A rather dusty room full of computers!");
        Room cheeseRoom = new Room("A room with a suspicious amount of cheese!");
        room.addDoor(new Door("A door to the cheese room", cheeseRoom));

        cheeseRoom.addDoor(new Door("Mystery door!"));

        NPC cheeseSeller = new NPC(this, cheeseRoom, "Cheese seller");
        cheeseRoom.addNPC(cheeseSeller);

        NPC fighter = new Enemy(this, cheeseRoom, "Anxious cheese-powered fighter", 20, 30);
        cheeseRoom.addNPC(fighter);

        return room;
    }

    private DialogInteraction buildCurrentInteraction() {
        return newInteraction()
                .option("Inspect current room", player::handleInspect)
                .option("Look for a way out", player::handleLookWayOut)
                .option("Look for company", player::handleLookForCompany);
    }

}