package nl.rug.oop.introduction;

/**
 * This class represents a student.
 */
public class Student extends Person {
    /** The knowledge level of the student. */
    private int knowledgeLevel;

    /**
     * Create a new student with a name and a knowledge level.
     * 
     * @param name           The name of the student
     * @param knowledgeLevel The knowledge level of the student
     */
    public Student(String name, int knowledgeLevel) {
        super(name);
        this.knowledgeLevel = knowledgeLevel;
    }

    /**
     * Increments the knowledge level of the student by one, up to 6.
     * Usually the result of a lecture.
     */
    public void obtainKnowledge() {
        knowledgeLevel = Math.min(knowledgeLevel + 1, 6);
    }

    /**
     * Completes an assignment and returns a submission.
     * 
     * @param assignment The assignment to complete
     * @return The submission of the student
     */
    public Submission doAssignment(Assignment assignment) {
        return new Submission(this.knowledgeLevel, assignment, this);
    }

    /**
     * Get the knowledge level of the student.
     * 
     * @return The knowledge level of the student
     */
    public int getKnowledgeLevel() {
        return this.knowledgeLevel;
    }
}
