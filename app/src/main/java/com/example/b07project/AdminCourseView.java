package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AdminCourseView extends AppCompatActivity {

    private RecyclerView recyclerView;
    courseAdapter adapter;
    DatabaseReference m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_view);
        m = FirebaseDatabase.getInstance().getReference("test");
        recyclerView = findViewById(R.id.courses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Course> options = new FirebaseRecyclerOptions.Builder<Course>()
                .setQuery(m, Course.class).build();
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
        adapter.stopListening();
    }
}