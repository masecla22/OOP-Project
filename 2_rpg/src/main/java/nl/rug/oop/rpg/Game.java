package nl.rug.oop.rpg;

import java.io.Serializable;
import java.util.Scanner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.rpg.game.map.GameMap;
import nl.rug.oop.rpg.game.player.Player;
import nl.rug.oop.rpg.game.save.SaveStateRequest;
import nl.rug.oop.rpg.interaction.DialogInteraction;

/**
 * The main game class.
 */
public class Game implements Serializable {
    /** The serial version UID. */
    private static final long serialVersionUID = 234109876243L;

    /** The scanner used for input. */
    @Getter(AccessLevel.PROTECTED)
    private transient Scanner scanner;

    /** The player. */
    private Player player;

    private GameMap map;

    /**
     * This is used to signal to the game loop to stop and
     * pass the message to the SwappableGameRunner which
     * handles the actual swapping and initialization
     * of the game.
     */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private transient SaveStateRequest saveStateRequest = null;

    /**
     * Initializes the game.
     */
    public void initialize() {
        this.map = new GameMap(this);

        this.map.initialize();

        player = new Player(this, "Player", this.map.getInitialRoom());
        player.initialize();

        this.afterLoad();
    }

    public void afterLoad() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Runs the game loop.
     */
    public void run() {
        while (true) {
            buildCurrentInteraction().interact();

            if (saveStateRequest != null) {
                break;
            }
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
                .option("Look for company", player::handleLookForCompany)
                .option("QuickSave", () -> saveStateRequest = SaveStateRequest.QUICKSAVE)
                .option("QuickLoad", () -> saveStateRequest = SaveStateRequest.QUICKLOAD)
                .option("Save", () -> saveStateRequest = SaveStateRequest.SAVE)
                .option("Load", () -> saveStateRequest = SaveStateRequest.LOAD);
    }

}