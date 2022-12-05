package com.example.b07project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentCourseView extends AppCompatActivity implements View.OnClickListener{

    private Button backbt;
    private DatabaseReference courseData;
    /**
     * All courses retrieved from database.
     * Key = course key/id, Value = course object
     */
    private HashMap<String, Course> courseDirectory;
    /**
     * A list of prerequisites for each coruse.
     * Key = course key/id, Value = comma-separated list of course codes representing the prerequisites
     */
    private HashMap<String, String> prerequisiteCodes = new HashMap<>();
    private RecyclerView mRecycleView;
    private SCourseViewAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List mList, kList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_course_view);
        courseData = FirebaseDatabase.getInstance().getReference();

        mRecycleView = findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        mList = new ArrayList<>();
        kList = new ArrayList<>();
        courseDirectory = new HashMap<>();
        prerequisiteCodes = new HashMap<>();

        initData(mList, kList);
        mAdapter = new SCourseViewAdapter(mList, kList);
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        mRecycleView.setAdapter(mAdapter);
        courseData.child("courses").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Iterable<DataSnapshot> courses = task.getResult().getChildren();
                    courseDirectory = new HashMap<>();
                    prerequisiteCodes = new HashMap<>();

                    // retrieve all courses available
                    for (DataSnapshot childSnapshot : courses) {
                        Course course = childSnapshot.getValue(Course.class);
                        courseDirectory.put(childSnapshot.getKey(), course);
                    }

                    // retrieve the prerequisite course names based on the course ids
                    for (Map.Entry<String, Course> course : courseDirectory.entrySet()) {
                        prerequisiteCodes.put(course.getKey(),
                                course.getValue().getPrerequisites(courseDirectory));
                    }
                    initData(mList, kList);
                    mAdapter = new SCourseViewAdapter(mList, kList);
                    mRecycleView.setLayoutManager(mLinearLayoutManager);
                    mRecycleView.setAdapter(mAdapter);

                }

            }

        });

        backbt = findViewById(R.id.scwbackbt);
        backbt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scwbackbt:
                startActivity(new Intent(this, StudentFrontPage.class));
                break;
        }

    }

    public void initData(List list_1, List list_2) {
        for (Map.Entry<String, Course> course : courseDirectory.entrySet()) {
            Course values = course.getValue();
            list_1.add(values.courseCode + "\n" +
                    values.courseName + "\n" +
                    values.getSessions() + "\n" +
                    prerequisiteCodes.get(course.getKey()));
            list_2.add(course.getKey());
        }
    }


}