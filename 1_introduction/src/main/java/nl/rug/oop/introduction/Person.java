package nl.rug.oop.introduction;

/**
 * This class represents a person.
 */
public class Person {
    /** The name of the person. */
    private String name;

    /**
     * Create a new person with a name.
     * 
     * @param name The name of the person
     */
    public Person(String name) {
        this.name = name;
    }

    /**
     * Get the name of the person.
     * 
     * @return The name of the person
     */
    public String getName(){
        return this.name;
    }
}
