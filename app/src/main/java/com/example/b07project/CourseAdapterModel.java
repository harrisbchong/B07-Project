package com.example.b07project;

import android.text.TextUtils;

import java.util.Arrays;

public class CourseAdapterModel {
    public static final String DELIMITER = ", ";
    Course courseData;

    public CourseAdapterModel() {}

    public CourseAdapterModel(Course courseData) {
        this.courseData = courseData;
    }

    public String getCourseCode() {
        return courseData.courseName;
    }

    public String getCourseName() {
        return courseData.courseName;
    }

    public String getOfferingSessions() {
        return TextUtils.join(CourseAdapterModel.DELIMITER, courseData.offeringSessions);
    }

    public String getPrerequisites() {
        return TextUtils.join(CourseAdapterModel.DELIMITER, courseData.prerequisites);
    }

    public void setCourseCode(String courseCode) {
        this.courseData.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseData.courseName = courseName;
    }

    public void setPrerequisites(String prerequisites) {
        this.courseData.prerequisites = Arrays.asList(
                prerequisites.split(CourseAdapterModel.DELIMITER));
    }

    public void setSessions(String offeringSessions) {
        this.courseData.offeringSessions = Arrays.asList(
                offeringSessions.split(CourseAdapterModel.DELIMITER));
    }
}