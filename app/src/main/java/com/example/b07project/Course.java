package com.example.b07project;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public String getPrerequisites(HashMap<String, Course> courseDirectory){
        List<String> codes = new ArrayList<>();
        for (String courseId : this.prerequisites) {
            codes.add(courseDirectory.get(courseId).courseCode);
        }

        String result = "Prerequisites: ";
        int n = prerequisites.size();
        if (n == 0 || prerequisites == null) {
            return "No prerequisites";
        }
        return result + TextUtils.join(", ", codes);
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

    public static List<Course> getCoursePath(HashMap<String, Course> courses,  boolean reverser,String CourseCode) {
        if (!courses.containsKey(CourseCode)) {
            return null;
        }

        // create the final result
        List<Course> plan = new ArrayList<Course>();

        //create the queue
        Queue<String> q = new LinkedList<>();

        //add that course
        q.offer(CourseCode);

        while (!q.isEmpty()) {
            //dequeue that course
            String cc = q.poll();
            System.out.println(cc);
            //get the course from hashmap
            Course c = courses.get(cc);
            System.out.println(c);
            if (!plan.contains(c))
                plan.add(c);

            //for loop the pre of this course
            for (String pre : c.prerequisites) {
                q.offer(pre);
            }
        }

        if (reverser)
            Collections.reverse(plan);

        return plan;
    }



}

