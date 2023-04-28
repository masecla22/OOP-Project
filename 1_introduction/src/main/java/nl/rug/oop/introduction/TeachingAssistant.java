package nl.rug.oop.introduction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * This class represents a teaching assistant.
 */
public class TeachingAssistant extends Person {
    /**
     * Create a new teaching assistant with a name.
     * 
     * @param name The name of the teaching assistant
     */
    public TeachingAssistant(String name) {
        super(name);
    }

    /**
     * Grade a submission. The grade is dependent on the quality of the submission
     * 
     * @param submission The submission to grade
     */
    public void grade(Submission submission) {
        // Get the submission time and deadline
        LocalDateTime submissionTime = submission.getSubmissionTime();
        LocalDateTime deadline = submission.getAssignment().getDeadline();

        Random random = new Random();

        // Random number between submission quality and 10 ()
        int grade = random.nextInt(5) + submission.getQuality();

        // The submission is late
        if (submissionTime.isAfter(deadline)) {
            grade = 0;
        }

        System.out.println(this.getName() + " graded " + submission.getAssignment().getName() + " by "
                + submission.getStudent().getName() + " as " + grade);
    }

    /**
     * Grade a list of submissions.
     * 
     * @param submissions The submissions to grade
     */
    public void gradeAll(List<Submission> submissions) {
        submissions.forEach(this::grade);
    }
}
