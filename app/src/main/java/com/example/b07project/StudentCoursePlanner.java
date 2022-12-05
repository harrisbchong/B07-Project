package com.example.b07project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.Map;
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
        for (int i = 0; i < coursestotake.length; i++) {
            coursestotake[i] = coursestotake[i].trim();
        }

        LinkedHashMap<String, HashSet<String>> plantable = new LinkedHashMap<>();
        model.getStudent(id, (Student student) -> {
            model.getCourses((HashMap<String, Course> allc) -> {
                // convert input codes to keys
                for (int i = 0; i < coursestotake.length; i++) {
                    for (Map.Entry<String, Course> entry : allc.entrySet()) {
                        if (entry.getValue().courseCode.equals(coursestotake[i])) {
                            coursestotake[i] = entry.getKey();
                            break;
                        }
                    }
                }

                for (String code: coursestotake) {
                    if (!allc.containsKey(code)) {
                        cctxt.setError("Incorrect Course Code");
                        cctxt.requestFocus();
                        return;
                    }
                    List<String> coursePath = Course.getCoursePath(allc, true, code);
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



                    while (coursePath.size() > 0) {
                        String courseKey = coursePath.get(0);
                        Course c = allc.get(courseKey);
                        //checker for if its already there
                        if (token.contains(courseKey)) { // change this to key
                            coursePath.remove(courseKey);
                            continue;
                        }
                        while (taking.size() == 0) {
                            for (String currKey : coursePath) {
                                Course currCourse = allc.get(currKey);
                                if (token.containsAll(currCourse.prerequisites) &&
                                        currCourse.offeringSessions.contains(currentS)) {
                                    taking.add(currKey);
                                }
                            }
                            String key = currentY + " " + currentS;
                            if (!plantable.containsKey(key))  //after adding all the taking courses this year, I will
                                plantable.put(key, new HashSet<>());
                            List<String> takingCodes = new ArrayList<>();
                            for (String tKey: taking) {
                                takingCodes.add(allc.get(tKey).courseCode);
                            }

                            plantable.get(key).addAll(takingCodes);
                            currentS = nextS(currentS);
                            if (currentS.equals("Winter"))
                                currentY += 1;
                        }
                        for(String dodoo: taking)
                            coursePath.remove(dodoo);

                        token.addAll(taking);
                        taking.clear();
                    }

                    //this is the Display UI method area

                    List<String> outputStrings = new ArrayList<>();
                    for (String key: plantable.keySet()) {
                        if(!plantable.get(key).isEmpty())
                        outputStrings.add(key + ": " + "\n" + String.join(", ",
                                Objects.requireNonNull(plantable.get(key))));
                    }
                    ptxt.setVisibility(View.VISIBLE);
                    ptxt.setText(outputStrings.size() > 0 ?
                            TextUtils.join("\n\n", outputStrings) :
                            "No More Remaining Courses To Take.");
//                    if (tostring.length() > 1) {
//                        ptxt.setVisibility(View.VISIBLE);
//                        tostring = tostring.substring(0,tostring.length()-2);
//                    }
//                    ptxt.setText(String.join("\n", tostring));

                }
            });

        });
    }






}

