package com.example.b07project;

import java.util.ArrayList;

public class Course {
    private String courseCode;
    private String courseName;
    private String offeringSessions;
    private String prerequisites;

    public Course(){

    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getOfferingSessions() {
        return offeringSessions;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public void setSessions(String offeringSessions) {
        this.offeringSessions = offeringSessions;
    }
}
