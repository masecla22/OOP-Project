package nl.rug.oop.introduction;

import java.util.List;

/**
 * This class represents a lecturer.
 */
public class Lecturer extends Person {
    /**
     * Create a new lecturer with a name.
     * 
     * @param name The name of the lecturer
     */
    public Lecturer(String name) {
        super(name);
    }

    /**
     * Lecture a list of students.
     * 
     * @param student The list of students to lecture
     */
    public void lecture(List<Student> student) {
        System.out.println(this.getName() + " is teaching extremely important assignments!");
        student.forEach(Student::obtainKnowledge);
    }
}
