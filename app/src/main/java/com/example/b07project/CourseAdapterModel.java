package com.example.b07project;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return courseData.getCourseCode();
    }

    public String getCourseName() {
        return courseData.getCourseName();
    }

    public String getOfferingSessions() {
        return TextUtils.join(CourseAdapterModel.DELIMITER, courseData.offeringSessions);
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