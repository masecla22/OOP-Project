package nl.rug.oop.rpg.game.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import nl.rug.oop.rpg.Game;

/**
 * A class that manages saving and loading the game.
 */
public class SaveStateManager {
    /**
     * Sanitizes a save name by removing all non-alphanumeric characters.
     * 
     * @param saveName the save name to sanitize
     * @return the sanitized save name
     */
    public String sanitizeSaveName(String saveName) {
        return saveName.replaceAll("[^a-zA-Z0-9]", "");
    }

    private File getSaveDirectory() throws IOException {
        File file = new File("savedgames");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    private File getSaveFile(String saveName) throws IOException {
        // Ensure saveName is only made of letters and numbers
        saveName = sanitizeSaveName(saveName);
        return new File(getSaveDirectory(), saveName + ".ser");
    }

    /**
     * Gets all the saves. It will return all the names,
     * including quicksave, without the .ser extension.
     * 
     * @return a set of all the saves
     * @throws IOException if there is an error reading the save directory
     */
    public Set<String> getAllSaves() throws IOException {
        File saveDirectory = getSaveDirectory();
        String[] saveNames = saveDirectory.list();
        Set<String> result = new HashSet<>();
        for (String saveName : saveNames) {
            if (saveName.endsWith(".ser")) {
                result.add(saveName.substring(0, saveName.length() - 4));
            }
        }

        return result;
    }

    /**
     * Saves the game to a file.
     * 
     * @param game     the game to save
     * @param saveName the name of the save
     * @throws IOException if there is an error saving the game
     */
    public void saveGame(Game game, String saveName) throws IOException {
        File saveFile = getSaveFile(saveName);
        if (saveFile.exists()) {
            saveFile.delete();
        }

        saveFile = getSaveFile(saveName);

        try (
                FileOutputStream outputStream = new FileOutputStream(saveFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(game);
        }
    }

    /**
     * Loads a game from a file.
     * 
     * @param saveName the name of the save
     * @return the loaded game
     * @throws IOException if there is an error loading the game
     */
    public Game loadGame(String saveName) throws IOException {
        File saveFile = getSaveFile(saveName);
        if (!saveFile.exists()) {
            throw new IOException("Save file does not exist");
        }

        try (
                FileInputStream inputStream = new FileInputStream(saveFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            Game game = (Game) objectInputStream.readObject();
            game.afterLoad();
            return game;
        } catch (ClassNotFoundException e) {
            // This should never happen as we don't delete classes
            throw new IOException("Save file is corrupted");
        }
    }
}
