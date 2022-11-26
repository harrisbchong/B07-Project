package com.example.b07project;

/**
 * The StudentUser class stores the information related to a student who has signed up for the app,
 * and determines how it is stored on the Realtime Database.
 *
 * @version 1.0
 * @author Cheng Liang Huang
 */
public class StudentUser {
    /**
     * The name of the student's program
     */
    public String programName;

    /**
     * The name of the student
     */
    public String studentName;

    /**
     * Default constructor required for use in the Realtime Database
     */
    public StudentUser() {}

    /**
     * Create a new instance when supplied names
     * @param programName Name of the student's program
     * @param studentName Name of the student
     */
    public StudentUser(String programName, String studentName) {
        this.programName = programName;
        this.studentName = studentName;
    }
}
