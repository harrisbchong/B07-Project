package com.example.b07project;

/**
 * The StudentUser class stores the information related to a student who has signed up for the app,
 * and determines how it is stored on the Realtime Database.
 *
 * @version 1.0
 * @author Cheng Liang Huang
 */
public class StudentUser extends User {
    /**
     * The name of the student's program
     */
    public String programName;

    /**
     * Default constructor required for use in the Realtime Database
     */
    public StudentUser() {
        super();
    }

    /**
     * Create a new instance when supplied names
     * @param username Name of the student
     * @param programName Name of the student's program
     */
    public StudentUser(String username, String programName) {
        super(username);

        this.programName = programName;
    }
}
