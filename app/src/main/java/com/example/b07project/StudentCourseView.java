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
import java.util.Arrays;
import java.util.List;

public class StudentCourseView extends AppCompatActivity implements View.OnClickListener{

    private Button backbt;
    private DatabaseReference courseData;
    private String[] allCourseCodes = new String[]{};
    private String[] allCourseNames = new String[]{};
    private String[] allCoursePrerequisites = new String[]{};
    private String[] allCourseSessions = new String[]{};
    private String[] allCourseKeys = new String[]{};
    private int courseNum;
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
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mList = new ArrayList<>();
        kList = new ArrayList<>();
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
                    courseNum = (int)task.getResult().getChildrenCount();
                    Iterable<DataSnapshot> courses = task.getResult().getChildren();
                    allCourseCodes = new String[courseNum];
                    allCourseNames = new String[courseNum];
                    allCourseKeys = new String[courseNum];
                    allCoursePrerequisites = new String[courseNum];
                    allCourseSessions = new String[courseNum];
                    int index = 0;
                    for (DataSnapshot childSnapshot : courses) {
                        Course course = childSnapshot.getValue(Course.class);
                        allCourseCodes[index] = course.getCode();
                        allCourseNames[index] = course.getName();
                        allCoursePrerequisites[index] = course.getPrerequisites();
                        allCourseSessions[index] = course.getSessions();
                        allCourseKeys[index] = childSnapshot.getKey();
                        index++;
                    }
                    initData(mList, kList);
                    mAdapter = new SCourseViewAdapter(mList, kList);
                    mRecycleView.setLayoutManager(mLinearLayoutManager);
                    mRecycleView.setAdapter(mAdapter);
                }

            }

        });

        backbt = (Button) findViewById(R.id.scwbackbt);
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

    public void initData(List list_1, List list_2){
        for(int i = 0; i < courseNum; i++){
            list_1.add(allCourseCodes[i] + "\n" + allCourseNames[i] + "\n" + allCourseSessions[i]
                    + "\n" + allCoursePrerequisites[i]);
            list_2.add(allCourseKeys[i]);
        }
    }


}