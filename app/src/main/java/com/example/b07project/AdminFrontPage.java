package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminFrontPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser ad;
    private DatabaseReference aRef;
    private String id;
    private Button logoutbt, bcbt, acbt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_front_page);
        aRef = FirebaseDatabase.getInstance().getReference("admins");
        logoutbt = (Button) findViewById(R.id.alogoutbt);
        logoutbt.setOnClickListener(this);
        bcbt = (Button) findViewById(R.id.bcbt);
        bcbt.setOnClickListener(this);
        acbt = (Button) findViewById(R.id.ctbt);
        acbt.setOnClickListener(this);
        ad = FirebaseAuth.getInstance().getCurrentUser();
        id = ad.getUid();

        aRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student s = snapshot.getValue(Student.class);
                if (s != null) {
                    TextView name = findViewById(R.id.sname);
                    name.setText(s.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminFrontPage.this, "Admin Not Found", Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alogoutbt:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.ctbt:
                startActivity(new Intent(this, AdminAddCourse.class));
                break;
            case R.id.bcbt:
                startActivity(new Intent(this, AdminCourseView.class));
                break;
        }
    }

}