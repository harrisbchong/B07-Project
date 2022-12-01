package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentAcademicHistory extends AppCompatActivity implements View.OnClickListener{

    private Button backbt;
    private DatabaseReference courseData;
    private String[] allCourseCodes = new String[]{};
    private String[] allCourseNames = new String[]{};
    private String[] allCourseKeys = new String[]{};
    private RecyclerView mRecycleView;
    private SCourseTakenAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List mList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framgent_student_academic_history);
        this.courseData = FirebaseDatabase.getInstance().getReference();
        this.courseData.child("courses").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Iterable<DataSnapshot> courses = task.getResult().getChildren();
                    allCourseCodes = new String[(int) task.getResult().getChildrenCount()];
                    allCourseNames = new String[(int) task.getResult().getChildrenCount()];
                    allCourseKeys = new String[(int) task.getResult().getChildrenCount()];
                    int index = 0;
                    for (DataSnapshot childSnapshot : courses) {
                        Course course = childSnapshot.getValue(Course.class);
                        allCourseCodes[index] = course == null ? "NULL" : course.courseCode;
                        allCourseNames[index] = course == null ? "NULL" : course.courseName;
                        allCourseKeys[index] = childSnapshot.getKey();
                        index++;
                    }

                }

            }

        });
        backbt = (Button) findViewById(R.id.slbackbt3);
        backbt.setOnClickListener(this);
        mList = new ArrayList<>();
        mRecycleView = findViewById(R.id.rv_course_taken);
        initData_1(mList);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new SCourseTakenAdapter(mList);
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        mRecycleView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.slbackbt3:
                startActivity(new Intent(this, StudentFrontPage.class));
                break;
        }

    }

    public void initData_1(List list){
        for(int i = 0; i < allCourseCodes.length; i++){
            list.add(allCourseCodes[i] + "hello\n");
        }
    }
}
