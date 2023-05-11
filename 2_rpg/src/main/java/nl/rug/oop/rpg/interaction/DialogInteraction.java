package nl.rug.oop.rpg.interaction;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DialogInteraction {
    private String prompt;
    private String cursor = "";

    private Scanner scanner;

    private List<String> options = new ArrayList<>();
    private List<Runnable> actions = new ArrayList<>();

    public DialogInteraction(Scanner scanner) {
        this.scanner = scanner;
    }

    public DialogInteraction option(String option, Runnable runnable) {
        options.add(option);
        actions.add(runnable);
        return this;
    }

    public DialogInteraction prompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    public DialogInteraction cursor(String cursor) {
        this.cursor = cursor;
        return this;
    }

    public void interact() {
        System.out.println(this.prompt);
        if (options.size() == 0)
            return;

        for (int i = 0; i < options.size(); i++) {
            System.out.println("  (" + i + ") " + options.get(i));
        }

        int choice = -1;
        while (choice < 0 || choice >= options.size()) {
            if (!cursor.isEmpty())
                System.out.println(cursor);
            System.out.print("> ");
            
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException exception) {
                scanner.nextLine();
                break;
            }

            if (choice == -1)
                break;
        }

        if (choice == -1)
            return;

        actions.get(choice).run();
    }
}
