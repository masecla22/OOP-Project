package nl.rug.oop.introduction;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains and manages the main method.
 */

public class Main {
    /**
     * The main method.
     * 
     * @param args - unused (arguments from the command line)
     */
    public static void main(String[] args) {
        Lecturer theOneAndOnly = new Lecturer("Arnold Mejister");

        List<Student> students = new ArrayList<>();

        students.add(new Student("Mattia Marziali", 6));
        students.add(new Student("Denisa Neagu", 6));
        // of couse we're giving ourselves 6

        students.add(new Student("Greg Souvlaki", 3));
        students.add(new Student("Lorenzo De Smallis", 4));
        students.add(new Student("Boyan Aloe Vera", 5));

        Assignment assignment = new Assignment("ImpProg Lab #3",
                LocalDateTime.of(2023, Month.APRIL, 29, 12, 30));

        theOneAndOnly.lecture(students);

        List<Submission> submissions = new ArrayList<>();
        students.stream().map(student -> student.doAssignment(assignment))
                .forEach(submissions::add);

        TeachingAssistant whoElseButHim = new TeachingAssistant("Channa");

        whoElseButHim.gradeAll(submissions);
    }
}