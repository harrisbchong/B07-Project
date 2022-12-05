package com.example.b07project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class StudentCoursePlanner extends AppCompatActivity implements View.OnClickListener {
    private Model model;
    private Button backbt, gbt;
    private EditText cctxt;
    private String id;
    private TextView ptxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_course_planner);
        model = Model.getInstance();
        backbt = (Button) findViewById(R.id.scpbackbt);
        backbt.setOnClickListener(this);
        gbt = (Button) findViewById(R.id.gtbt);
        gbt.setOnClickListener(this);
        ptxt = (TextView) findViewById(R.id.plantable);
        ptxt.setVisibility(View.INVISIBLE);
        cctxt = (EditText) findViewById(R.id.cctxt);
        id = getIntent().getStringExtra("id");

    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.scpbackbt:
                startActivity(new Intent(this, StudentFrontPage.class));
                break;
            case R.id.gtbt:
                generateplan();
                break;
        }
    }


    public String nextS(String s){
        if (s.equals("Fall") )
            return "Winter";
        if(s.equals("Winter"))
            return "Summer";
        if(s.equals("Summer"))
            return "Fall";
        return null;
    }



    @SuppressLint("SuspiciousIndentation")
    public void generateplan() {

        String[] coursestotake = cctxt.getText().toString().trim().toUpperCase().split(",");

        LinkedHashMap<String, HashSet<String>> plantable = new LinkedHashMap<>();
        model.getStudent(id, (Student student) -> {
            model.getCourses((HashMap<String, Course> allc) -> {
                for (String code: coursestotake) {
                    if (!allc.containsKey(code)) {
                        cctxt.setError("Incorrect Course Code");
                        cctxt.requestFocus();
                        return;
                    }
                    List<Course> coursePath = Course.getCoursePath(allc, true, code);
                    if (coursePath == null) {
                        return;
                    }
                    // add all of my taken courses into this taken list
                    List<String> token = new ArrayList<>(student.taken);
                    //System.out.println("----------------------------------");
                    //System.out.println(student.taken);
                    // the current taking list of courses
                    List<String> taking = new ArrayList<>();
                    // *****Real time year and Semester
                    int currentY = 2022;
                    String currentS = "Fall";
                    //


                    while (coursePath.size() > 0) {
                        Course c = coursePath.get(0);
                        //checker for if its already there
                        if (token.contains(c.courseCode)) {
                            coursePath.remove(c);
                            continue;
                        }
                        while (taking.size() == 0) {
                            for (Course currCourse : coursePath) {
                                System.out.println("curr: " + currCourse.courseCode);
                                if (token.containsAll(currCourse.prerequisites) &&
                                        currCourse.offeringSessions.contains(currentS)) {
                                    taking.add(currCourse.courseCode);
                                }
                            }
                            String key = currentY + " " + currentS;
                            if (!plantable.containsKey(key))  //after adding all the taking courses this year, I will
                                plantable.put(key, new HashSet<>());
                            plantable.get(key).addAll(taking);
                            currentS = nextS(currentS);
                            if (currentS.equals("Winter"))
                                currentY += 1;
                        }
                        for(String dodoo: taking)
                            coursePath.remove(allc.get(dodoo));

                        token.addAll(taking);
                        taking.clear();
                    }

                    //this is the Display UI method area

                    String tostring = "";
                    for (String key: plantable.keySet()) {
                        if(!plantable.get(key).isEmpty())
                        tostring += key + ": " + "\n" + String.join(", ", Objects.requireNonNull(plantable.get(key))) +"\n"+ "\n";
                    }
                    ptxt.setVisibility(View.VISIBLE);
                    ptxt.setText(String.join("\n", tostring.substring(0,tostring.length()-2)));

                }
            });

        });
    }






}

