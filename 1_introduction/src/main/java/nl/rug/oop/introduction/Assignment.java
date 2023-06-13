package nl.rug.oop.introduction;

import java.time.LocalDateTime;

/**
 * This class represents an assignment.
 */
public class Assignment {
    /** The name of the assignment. */
    private String name;

    /** The deadline for the assignment. */
    private final LocalDateTime deadline;

    /**
     * Create a new assignment with a name and a deadline.
     * 
     * @param name     The name of the assignment
     * @param deadline The deadline for the assignment
     */
    public Assignment(String name, LocalDateTime deadline) {
        this.name = name;
        this.deadline = deadline;
    }

    /**
     * Get the name of the assignment.
     * 
     * @return The name of the assignment
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the deadline for the assignment.
     * 
     * @return The deadline for the assignment
     */
    public LocalDateTime getDeadline() {
        return this.deadline;
    }
}
