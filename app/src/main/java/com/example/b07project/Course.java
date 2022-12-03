package com.example.b07project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
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

    public String getCode(){
        return this.courseCode;
    }

    public String getName(){
        return this.courseName;
    }

    public String getPrerequisites(){
        String result = "Prerequisites: ";
        int n = prerequisites.size();
        if((n == 0)||(prerequisites == null)){
            return "No prerequisites";
        }
        for(int i = 0; i < n - 1; i++){
            result = result + prerequisites.get(i) +  ", ";
        }
        return result + prerequisites.get(n-1);
    }

    public String getSessions(){
        String result = "Sessions: ";
        int n = offeringSessions.size();
        if((n == 0)||(offeringSessions == null)){
            return "Please implement sessions for this course";
        }
        for (int i = 0;i < n - 1; i++){
            result = result + offeringSessions.get(i) + ", ";
        }
        return result + offeringSessions.get(n-1);
    }


}

/*
for (int i = 0;i < n; i++){
            String part = m.child("courses").child(prerequisites.get(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        String course = task.getResult().getValue(Course.class).getCode();
                        result = result + course + ", ";
                        }
                    }
                }
            });
        }
 */