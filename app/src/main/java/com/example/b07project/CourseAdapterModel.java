package com.example.b07project;

import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;

public class CourseAdapterModel {
    public static final String DELIMITER = ", ";
    Course courseData;
    DatabaseReference ref;

    public CourseAdapterModel() {}

    public CourseAdapterModel(Course courseData, DatabaseReference ref) {
        this.courseData = courseData;
        this.ref = ref;
    }

    public DatabaseReference getRef(){ return ref;}

    public String getCourseCode() {
        return courseData.courseCode;
    }

    public String getCourseName() {
        return courseData.courseName;
    }

    public String getOfferingSessions() {
        return TextUtils.join(CourseAdapterModel.DELIMITER, courseData.offeringSessions);
    }

    public String getPrerequisites() {
        String pre = TextUtils.join(CourseAdapterModel.DELIMITER, courseData.prerequisites);
        if(pre.isEmpty()) return "No prerequisites";
        return pre;
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