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
import java.util.List;

public class StudentAcademicHistory extends AppCompatActivity implements View.OnClickListener{

    private Button backbt;
    private ImageButton tobt;
    private DatabaseReference mDR;
    private FirebaseAuth auth;
    private FirebaseUser userId;
    private String[] allCourseCodes = new String[]{};
    private String[] allCourseNames = new String[]{};
    private String[] allCourseKeys = new String[]{};
    private int courseNum, i = 0;
    private RecyclerView mRecycleView;
    private SCourseTakenAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List mList, kList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framgent_student_academic_history);
        mDR = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser();

        mRecycleView = findViewById(R.id.rv_course_taken);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mList = new ArrayList<>();
        kList = new ArrayList<>();
        initData_1(mList, kList);
        mAdapter = new SCourseTakenAdapter(mList, kList);
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        mRecycleView.setAdapter(mAdapter);
        mDR.child("students").child(userId.getUid()).child("taken").get().addOnCompleteListener(
                new OnCompleteListener<DataSnapshot>() {
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
                    int index = 0;
                    for (DataSnapshot childSnapshot : courses) {
                        allCourseKeys[index] = childSnapshot.getKey();
                        allCourseCodes[index] = childSnapshot.getValue(String.class);
                        index++;
                    }

                    for(int i = 0; i < allCourseKeys.length; i++){
                        int finalI = i;
                        mDR.child("courses").child(allCourseKeys[i]).get().addOnCompleteListener
                                (new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                } else {
                                    Course course = task.getResult().getValue(Course.class);
                                    if(course == null){
                                        allCourseCodes[finalI] = "Sorry,the class";
                                        allCourseNames[finalI] = "has been deleted by the admin.";
                                        mDR.child("students").child(userId.getUid()).child("taken").child(allCourseKeys[finalI]).removeValue();
                                    } else {
                                        allCourseCodes[finalI] = course.getCode();
                                        allCourseNames[finalI] = course.getName();
                                        mDR.child("students").child(userId.getUid()).child("taken").child(allCourseKeys[finalI])
                                                .setValue(course.getCode());
                                    }
                                }
                                mList = new ArrayList<>();
                                initData_1(mList, kList);
                                mAdapter = new SCourseTakenAdapter(mList, kList);
                                mRecycleView.setLayoutManager(mLinearLayoutManager);
                                mRecycleView.setAdapter(mAdapter);
                            }
                        });
                    }
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

    public void initData_1(List list_1, List list_2){
        for(int i = 0; i < courseNum; i++){
            list_1.add(allCourseCodes[i] + "\n" + allCourseNames[i]);
            list_2.add(allCourseKeys[i]);
        }
    }


}
