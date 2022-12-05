package com.example.b07project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SCourseViewAdapter extends RecyclerView.Adapter<SCourseViewAdapter.MyHolder>{
    private DatabaseReference m;
    private FirebaseAuth auth;
    private FirebaseUser userId;

    private HashMap<String, Course> courses;

    SCourseViewAdapter(HashMap<String, Course> courses) {
        this.courses = courses;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scourse, parent, false);
        MyHolder holder = new MyHolder(view);
        m = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser();
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        String[] keys = courses.keySet().toArray(new String[0]);
        Course currentCourse = courses.get(keys[position]);

        holder.courseCode.setText(currentCourse.courseCode);
        holder.courseName.setText(currentCourse.courseName);
        holder.offeringSessions.setText(currentCourse.getSessionsAsString());
        holder.prerequisites.setText(currentCourse.getPrerequisitesAsString(courses));

        holder.addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String k = keys[position];

                m.child("students").child(userId.getUid()).get().addOnCompleteListener
                        (new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Course courseSelected = null;
                            if (courses.containsKey(k)) {
                                courseSelected = courses.get(k);
                            }

                            Student student = task.getResult().getValue(Student.class);

                            if (student.taken.contains(k)) {
                                // if the student has already taken the course
                                Toast.makeText(v.getContext(), "You have already taken this course",
                                        Toast.LENGTH_SHORT).show();
                            } else if (courseSelected.prerequisites.isEmpty()) {
                                student.taken.add(k);
                                m.child("students").child(userId.getUid()).setValue(student);
                                holder.addButton.setVisibility(View.INVISIBLE);
                                Toast.makeText(v.getContext(), courseSelected.courseCode +
                                                " is successfully added to the taken courses",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                boolean tookAllPrereqs = true;
                                for (String prereq : courseSelected.prerequisites) {
                                    if (!student.taken.contains(prereq)) {
                                        tookAllPrereqs = false;
                                        break;
                                    }
                                }
                                if(tookAllPrereqs){
                                    student.taken.add(k);
                                    m.child("students").child(userId.getUid()).setValue(student);
                                    holder.addButton.setVisibility(View.INVISIBLE);
                                    Toast.makeText(v.getContext(), courseSelected.courseCode +
                                                    " is successfully added to the taken courses",
                                            Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(v.getContext(),
                                            "The prerequisites for this course are not met",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                    }

                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.courses.keySet().size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView courseCode;
        TextView courseName;
        TextView offeringSessions;
        TextView prerequisites;
        ImageButton addButton;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            courseCode = itemView.findViewById(R.id.courseCode);
            courseName = itemView.findViewById(R.id.courseName);
            offeringSessions = itemView.findViewById(R.id.offeringSessions);
            prerequisites = itemView.findViewById(R.id.prerequisites);
            addButton = itemView.findViewById(R.id.acbt);
        }
    }
}

