package nl.rug.oop.rpg;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import nl.rug.oop.rpg.game.save.SaveStateManager;
import nl.rug.oop.rpg.game.save.SaveStateRequest;
import nl.rug.oop.rpg.interaction.DialogInteraction;

/**
 * This class handles the swapping and loading of games.
 */
public class SwappableGameRunner {

    /** The save state manager which handles IO. */
    private SaveStateManager saveStateManager;

    /** The currently running game. */
    private Game currentlyRunningGame;

    /** A mapping of SaveStateRequest to their handlers. */
    private Map<SaveStateRequest, Runnable> requestHandlers = Map.of(
            SaveStateRequest.QUICKSAVE, this::handleQuickSave,
            SaveStateRequest.QUICKLOAD, this::handleQuickLoad,
            SaveStateRequest.SAVE, this::handleSave,
            SaveStateRequest.LOAD, this::handleLoad);

    /**
     * Creates a new SwappableGameRunner.
     * If there are no saves, it will create a new game.
     */
    public SwappableGameRunner() {
        saveStateManager = new SaveStateManager();

        try {
            this.currentlyRunningGame = saveStateManager.getAllSaves()
                    .stream().findFirst().map(c -> {
                        try {
                            System.out.println("Loading save " + c);
                            return saveStateManager.loadGame(c);
                        } catch (IOException e) {
                            System.err.println("Failed to load save " + c + ". Giving new game instead!");
                            Game game = new Game();
                            game.initialize();
                            return game;
                        }
                    })
                    .orElseGet(() -> {
                        System.out.println("No Saves found! Making new game!");
                        Game game = new Game();
                        game.initialize();
                        return game;
                    });
        } catch (IOException e) {
            System.err.println("Failed to load save. Giving new game instead!");
            this.currentlyRunningGame = new Game();
            this.currentlyRunningGame.initialize();
        }
    }

    /**
     * Runs the game loop of the currently loaded game.
     */
    public void run() {
        this.currentlyRunningGame.run();

        // If this method ever returns, it means the player has requested some type
        // of save/load operation

        SaveStateRequest request = this.currentlyRunningGame.getSaveStateRequest();
        // Invalidate the save state request
        this.currentlyRunningGame.setSaveStateRequest(null);

        this.requestHandlers.get(request).run();
    }

    /**
     * Handles the quick saving of the game.
     */
    private void handleQuickSave() {
        try {
            saveStateManager.saveGame(this.currentlyRunningGame, "quicksave");

            // Save is complete, resume the game
            this.run();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Failed to quick-save game!");
            System.exit(0);
        }
    }

    /**
     * Handles the quick loading of the game.
     */
    private void handleQuickLoad() {
        try {
            // Swap out the current game for the quicksave
            this.currentlyRunningGame = saveStateManager.loadGame("quicksave");

            // Load is complete, resume the game
            this.run();
        } catch (IOException exception) {
            System.err.println("Failed to quick-load game!");
            System.exit(0);
        }
    }

    /**
     * Handles the loading of a save.
     */
    private void handleLoad() {
        try {
            // Prompt an interaction for the user to select a save
            DialogInteraction interaction = this.currentlyRunningGame.newInteraction()
                    .prompt("Select a save to load")
                    .cursor("Pick a save: (-1 to cancel)");

            for (String saveName : saveStateManager.getAllSaves()) {
                if (saveName.equals("quicksave")) {
                    continue;
                }

                interaction.option("Load save " + saveName, () -> {
                    try {
                        // Swap out the current game for the selected save
                        this.currentlyRunningGame = saveStateManager.loadGame(saveName);
                    } catch (IOException exception) {
                        this.currentlyRunningGame = null;
                    }
                });
            }

            interaction.interact();

            // Here the game should have been swapped out for the selected save
            if (currentlyRunningGame != null) {
                this.run();
            } else {
                throw new IOException("Failed to load save");
            }
        } catch (IOException exception) {
            System.err.println("Failed to load game!");
            System.exit(0);
        }
    }

    /**
     * Gets a save name from the user.
     * 
     * @param scanner The scanner to use for input
     * @return The save name
     */
    private String getSaveName(Scanner scanner) {
        System.out.print("Save name: ");
        String saveName = scanner.nextLine();
        saveName = saveStateManager.sanitizeSaveName(saveName);

        return saveName;
    }

    /**
     * Handles the saving of the game.
     */
    private void handleSave() {
        Scanner saveScanner = new Scanner(System.in);
        String saveName = getSaveName(saveScanner);

        DialogInteraction interaction = this.currentlyRunningGame.newInteraction()
                .prompt("Game will be saved as " + saveName
                        + ".ser. Is that alright?");

        interaction.option("Confirm", () -> {
            try {
                saveStateManager.saveGame(this.currentlyRunningGame, saveName);
            } catch (IOException e) {
                System.err.println("Failed to save game!");
                System.exit(0);
            }
        });

        interaction.option("Cancel", () -> {
            System.out.println("Save cancelled!");
        });

        interaction.interact();

        // Save is complete, resume the game
        this.run();
    }

}
