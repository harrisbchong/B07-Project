package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_course_view);
        courseData = FirebaseDatabase.getInstance().getReference();

        mRecycleView = findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        courseDirectory = new HashMap<>();
        prerequisiteCodes = new HashMap<>();


        courseData.child("courses").get().addOnCompleteListener(
                new OnCompleteListener<DataSnapshot>() {
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
                                course.getValue().getPrerequisitesAsString(courseDirectory));
                    }
                    mAdapter = new SCourseViewAdapter(courseDirectory);
                    mRecycleView.setLayoutManager(mLinearLayoutManager);
                    mRecycleView.setAdapter(mAdapter);

                }

            }

        });

        mAdapter = new SCourseViewAdapter(courseDirectory);
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        mRecycleView.setAdapter(mAdapter);

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
}