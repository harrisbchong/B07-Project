package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentAcademicHistory extends AppCompatActivity implements View.OnClickListener{

    private Button backbt;
    private ImageButton tobt;
    private DatabaseReference mDR;
    private FirebaseAuth auth;
    private FirebaseUser userId;
    private RecyclerView mRecycleView;
    private SCourseTakenAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private HashMap<String, Course> courseDirectory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framgent_student_academic_history);
        mDR = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser();

        mRecycleView = findViewById(R.id.rv_course_taken);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);

        mDR.child("courses").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Iterable<DataSnapshot> courses = task.getResult().getChildren();
                    courseDirectory = new HashMap<>();
                    // retrieve all courses available
                    for (DataSnapshot childSnapshot : courses) {
                        Course course = childSnapshot.getValue(Course.class);
                        courseDirectory.put(childSnapshot.getKey(), course);
                    }

                    mDR.child("students").child(userId.getUid()).get().addOnCompleteListener(
                            new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                Student student = task.getResult().getValue(Student.class);
                                List<Course> coursesTaken = new ArrayList<>();
                                List<String> courseKeys = new ArrayList<>();

                                // retrieve the data for the courses that the student has taken
                                for (String taken : student.taken) {
                                    if (courseDirectory.containsKey(taken)) {
                                        // only add courses if the key is found
                                        coursesTaken.add(courseDirectory.get(taken));
                                        courseKeys.add(taken);
                                    }
                                }

                                // remove any keys that don't match courses
                                if (student.taken.size() != courseKeys.size()) {
                                    student.taken = courseKeys;
                                    mDR.child("students").child(userId.getUid()).setValue(student);
                                }

                                mAdapter = new SCourseTakenAdapter(coursesTaken, courseKeys,
                                        student);
                                mRecycleView.setLayoutManager(mLinearLayoutManager);
                                mRecycleView.setAdapter(mAdapter);
                            }
                        }
                    });
                }
            }
        });

        backbt = (Button) findViewById(R.id.slbackbt3);
        backbt.setOnClickListener(this);
        tobt = (ImageButton) findViewById(R.id.toCourseView);
        tobt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.slbackbt3:
                startActivity(new Intent(this, StudentFrontPage.class));
                break;
            case R.id.toCourseView:
                startActivity(new Intent(this, StudentCourseView.class));
                break;
        }

    }


}
