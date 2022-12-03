package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.ClassSnapshotParser;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdminCourseView extends AppCompatActivity {

    private RecyclerView recyclerView;
    courseAdapter adapter = null;
    DatabaseReference courseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_course_view);
        courseData = FirebaseDatabase.getInstance().getReference("courses");

        recyclerView = findViewById(R.id.courses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<CourseAdapterModel> options = new FirebaseRecyclerOptions.Builder<CourseAdapterModel>()
                .setQuery(courseData, new SnapshotParser<CourseAdapterModel>() {
                    @Nullable
                    @org.jetbrains.annotations.Nullable
                    @Override
                    public CourseAdapterModel parseSnapshot(@NonNull @NotNull DataSnapshot snapshot) {
                        Course c = snapshot.getValue(Course.class);
                        DatabaseReference r = snapshot.getRef();
                        CourseAdapterModel data = new CourseAdapterModel(c, r);
                        return data;
                    }
                }).build();
        adapter = new courseAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }

    }
}