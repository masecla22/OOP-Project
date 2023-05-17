package nl.rug.oop.rpg;

import java.util.Scanner;

import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.entities.NPC;
import nl.rug.oop.rpg.game.objects.Door;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.player.Player;
import nl.rug.oop.rpg.interaction.DialogInteraction;

/**
 * The main game class.
 */
public class Game {
    /** The scanner used for input. */
    private Scanner scanner;

    /** The player. */
    private Player player;

    /**
     * Initializes the game.
     */
    public void initialize() {
        this.scanner = new Scanner(System.in);
        player = new Player(this, "Player", getInitialRoom());
    }

    /**
     * Runs the game loop.
     */
    public void run() {
        while (true) {
            buildCurrentInteraction().interact();
        }
    }

    /**
     * Creates a new interaction.
     * 
     * @return the new interaction
     */
    public DialogInteraction newInteraction() {
        return new DialogInteraction(scanner)
                .prompt("HP: " + this.player.getFormattedHealth() + ", DMG: "
                        + this.player.getDamage()
                        + "\nWhat do you want to do?");
    }

    /**
     * Gets the initial room.
     * 
     * @return - the initial room
     */
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

    /**
     * Builds the current interaction.
     * 
     * @return - the current interaction
     */
    private DialogInteraction buildCurrentInteraction() {
        return newInteraction()
                .option("Inspect current room", player::handleInspect)
                .option("Look for a way out", player::handleLookWayOut)
                .option("Look for company", player::handleLookForCompany);
    }

}