package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentFrontPage extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser stu;
    private DatabaseReference stuRef;
    private String id;
    private Button logoutbt,ctbt,ahbt,bcbt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_front_page);
        stuRef = FirebaseDatabase.getInstance().getReference("students");
        logoutbt = (Button) findViewById(R.id.alogoutbt);
        logoutbt.setOnClickListener(this);
        bcbt = (Button) findViewById(R.id.bcbt);
        bcbt.setOnClickListener(this);
        ctbt = (Button) findViewById(R.id.ctbt);
        ctbt.setOnClickListener(this);
        ahbt = (Button) findViewById(R.id.ahbt);
        ahbt.setOnClickListener(this);

        stu = FirebaseAuth.getInstance().getCurrentUser();
        id = stu.getUid();

        stuRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student s = snapshot.getValue(Student.class);
                if (s != null) {
                    TextView name = findViewById(R.id.sname);
                    name.setText(s.name);
                    TextView prog = findViewById(R.id.programn);
                    prog.setText(s.program);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentFrontPage.this, "Student Not Found", Toast.LENGTH_LONG).show();
            }
        });



    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alogoutbt:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "logOut successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.ctbt:
                startActivity(new Intent(this, StudentCourseTimeline.class));
                break;
            case R.id.bcbt:
                startActivity(new Intent(this, StudentCourseView.class));
                break;
            case R.id.ahbt:
                startActivity(new Intent(this, StudentAcademicHistory.class));
                break;

        }
    }
}