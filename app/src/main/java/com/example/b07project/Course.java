package com.example.b07project;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Course(String code, String name, List<String> s, List<String> prereq) {
        this.courseName = name;
        this.courseCode = code;
        this.prerequisites = prereq;
        this.offeringSessions = s;

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
