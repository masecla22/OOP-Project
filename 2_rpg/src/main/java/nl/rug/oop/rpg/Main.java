package nl.rug.oop.rpg;

/**
 * The main class. This is the starting point
 */
public class Main {
    /**
     * The main function. Creates a new game and runs it.
     * 
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        SwappableGameRunner runner = new SwappableGameRunner();
        runner.run();
    }
}
