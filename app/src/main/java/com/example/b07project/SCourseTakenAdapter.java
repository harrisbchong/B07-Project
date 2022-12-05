package com.example.b07project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SCourseTakenAdapter extends RecyclerView.Adapter<SCourseTakenAdapter.MyHolderST>{
        private DatabaseReference m;

        private List<Course> coursesTaken;
        private List<String> courseKeys;
        private Student student;

        SCourseTakenAdapter(List<Course> coursesTaken, List<String> courseKeys, Student student) {
            this.coursesTaken = coursesTaken;
            this.courseKeys = courseKeys;
            this.student = student;
        }

        @Override
        public MyHolderST onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_stucoursetaken, parent, false);
            MyHolderST holder = new MyHolderST(view);
            m = FirebaseDatabase.getInstance().getReference("students");
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolderST holder, int position) {
            Course course = coursesTaken.get(position);
            if (course == null) {
                holder.courseCode.setText("Course Not Found.");
                holder.courseName.setText("Sorry, This Course\nHas Been Deleted By Administration");
                holder.dcbt.setVisibility(View.INVISIBLE);
            } else {
                holder.courseCode.setText(course.getCourseCode());
                holder.courseName.setText(course.getCourseName());
                holder.dcbt.setVisibility(View.VISIBLE);
            }

            holder.dcbt.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String courseKeyToRemove = courseKeys.get(position);
                    if (courseKeyToRemove != "" && student.taken.contains(courseKeyToRemove)) {
                        student.taken.remove(courseKeyToRemove);
                        m.child(student.id).setValue(student).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    holder.courseCode.setText("\n  Course Has Been Removed.");
                                    holder.courseName.setText("");
                                }
                            }
                        });
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.coursesTaken.size();
        }

        class MyHolderST extends RecyclerView.ViewHolder {

            TextView courseCode;
            TextView courseName;
            ImageButton dcbt;

            public MyHolderST(View itemView) {
                super(itemView);
                courseCode = itemView.findViewById(R.id.courseCode);
                courseName = itemView.findViewById(R.id.courseName);
                dcbt = itemView.findViewById(R.id.dcbt);
            }
        }
    }

