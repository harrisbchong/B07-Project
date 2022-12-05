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
                holder.textView.setText("Sorry, this course\nhas been deleted by admin");
                holder.kView.setText("");
                holder.ibt.setVisibility(View.INVISIBLE);
            } else {
                holder.textView.setText(course.getCourseCode() + "\n" + course.getCourseName());
                holder.kView.setText(courseKeys.get(position));
                holder.ibt.setVisibility(View.VISIBLE);
            }

            holder.ibt.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String k = holder.kView.getText().toString();
                    if (k != "" && student.taken.contains(k)) {
                        student.taken.remove(k);
                        m.child(student.id).setValue(student).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    holder.textView.setText("This course is successfully removed\n" +
                                            "from your taken course list.");
                                } else {
                                    holder.textView.setText("Failed to remove course from list " +
                                            "of taken courses. Please refresh page.");
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

            TextView textView, kView;
            ImageButton ibt;

            public MyHolderST(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_content_1);
                kView = itemView.findViewById(R.id.k_content_1);
                ibt = itemView.findViewById(R.id.imageButton3);
            }
        }
    }

