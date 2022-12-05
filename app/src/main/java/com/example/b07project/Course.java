package com.example.b07project;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Course {

    public String courseCode;

    public String courseName;

    public List<String> prerequisites;

    public List<String> offeringSessions;

    public Course() {
        this.prerequisites = new ArrayList<>();
        this.offeringSessions = new ArrayList<>();
    }

    public Course(String courseCode, String courseName, List<String> prerequisites,
                  List<UniversitySession> sessions) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.prerequisites = prerequisites;
        this.offeringSessions = new ArrayList<>();
        Collections.sort(sessions, (o1, o2) -> Integer.compare(o1.getSessionOrder(), o2.getSessionOrder()));
        for (UniversitySession session: sessions) {
            this.offeringSessions.add(session.getSessionName());
        }
    }

    public void setCourseCode(String code) {
        this.courseCode = code;
    }

    public void setCourseName(String name) {
        this.courseName = name;
    }

    @Exclude
    public String getCourseCode(){
        return this.courseCode;
    }

    @Exclude
    public String getCourseName(){
        return this.courseName;
    }

    @Exclude
    public String getPrerequisitesAsString(HashMap<String, Course> courseDirectory){
        List<String> codes = new ArrayList<>();
        for (String courseId : this.prerequisites) {
            codes.add(courseDirectory.get(courseId).courseCode);
        }

        if (prerequisites == null || this.prerequisites.size() == 0) {
            return "No prerequisites";
        }
        return TextUtils.join(", ", codes);
    }

    @Exclude
    public String getSessionsAsString(){
        if(this.offeringSessions == null || this.offeringSessions.size() == 0){
            return "Please implement sessions for this course";
        }
        return TextUtils.join(", ", this.offeringSessions);
    }

}