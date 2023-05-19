package nl.rug.oop.rpg;

import java.util.Scanner;

import nl.rug.oop.rpg.game.map.GameMap;
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

    private GameMap map;

    /**
     * Initializes the game.
     */
    public void initialize() {
        this.scanner = new Scanner(System.in);
        this.map = new GameMap(this);

        this.map.initialize();

        player = new Player(this, "Player", this.map.getInitialRoom());
        player.initialize();
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
                        + this.player.getDamage() + ", Gold: " + this.player.getGold()
                        + "\nWhat do you want to do?");
    }

    /**
     * Builds the current interaction.
     * 
     * @return - the current interaction
     */
    private DialogInteraction buildCurrentInteraction() {
        return newInteraction()
                .option("Check inventory", player::handleCheckInventory)
                .option("Inspect current room", player::handleInspect)
                .option("Look for a way out", player::handleLookWayOut)
                .option("Look for company", player::handleLookForCompany);
    }

}