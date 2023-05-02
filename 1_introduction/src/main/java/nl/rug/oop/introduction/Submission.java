package nl.rug.oop.introduction;

import java.time.LocalDateTime;

/**
 * This class represents a submission.
 */
public class Submission {
    /** The quality of the submission. */
    private int quality;

    /** The time of the submission. */
    private final LocalDateTime submissionTime;

    /** The assignment of the submission. */
    private Assignment assignment;

    /** The student of the submission. */
    private Student student;

    /**
     * Create a new submission with a quality, an assignment and a student.
     * The submissionTime will be set to the current time.
     * 
     * @param quality    The quality of the submission
     * @param assignment The assignment of the submission
     * @param student    The student of the submission
     */
    public Submission(int quality, Assignment assignment, Student student) {
        this.quality = quality;
        this.submissionTime = LocalDateTime.now();

        this.assignment = assignment;
        this.student = student;
    }

    /**
     * Get the quality of the submission.
     * 
     * @return The quality of the submission
     */
    public int getQuality() {
        return this.quality;
    }

    /**
     * Get the time of the submission.
     * 
     * @return The time of the submission
     */
    public LocalDateTime getSubmissionTime() {
        return this.submissionTime;
    }

    /**
     * Get the assignment of the submission.
     * 
     * @return The assignment of the submission
     */
    public Assignment getAssignment() {
        return this.assignment;
    }

    /**
     * Get the student of the submission.
     * 
     * @return The student of the submission
     */
    public Student getStudent() {
        return this.student;
    }
}
