package nl.rug.oop.rpg.interaction;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * A class that represents a dialog interaction.
 */
public class DialogInteraction {
    /** The prompt (which shows up before the options). */
    private String prompt;
    /** The cursor (which shows up after the options). */
    private String cursor = "";

    /** The scanner used for input. */
    private Scanner scanner;

    /** The options. */
    private List<String> options = new ArrayList<>();
    /** The actions. */
    private List<Runnable> actions = new ArrayList<>();

    /**
     * Creates a new dialog interaction.
     * 
     * @param scanner - the scanner used for input
     */
    public DialogInteraction(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Adds an option to the dialog interaction.
     * 
     * @param option   - the option
     * @param runnable - the action
     * @return - this
     */
    public DialogInteraction option(String option, Runnable runnable) {
        options.add(option);
        actions.add(runnable);
        return this;
    }

    /**
     * Sets the prompt.
     * 
     * @param prompt - the prompt
     * @return - this
     */
    public DialogInteraction prompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    /**
     * Sets the cursor.
     * 
     * @param cursor - the cursor
     * @return - this
     */
    public DialogInteraction cursor(String cursor) {
        this.cursor = cursor;
        return this;
    }

    /**
     * Interacts with the dialog interaction.
     */
    public void interact() {
        System.out.println(this.prompt);
        if (options.size() == 0) {
            return;
        }

        for (int i = 0; i < options.size(); i++) {
            System.out.println("  (" + i + ") " + options.get(i));
        }

        int choice = -1;
        while (choice < 0 || choice >= options.size()) {
            if (!cursor.isEmpty()) {
                System.out.println(cursor);
            }
            System.out.print("> ");

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException exception) {
                scanner.nextLine();
                break;
            }

            if (choice == -1) {
                break;
            }
        }

        if (choice == -1) {
            return;
        }

        Runnable runnable = actions.get(choice);

        if (runnable != null) {
            runnable.run();
        }
    }
}
