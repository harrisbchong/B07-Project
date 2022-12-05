package com.example.b07project;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class courseAdapter extends FirebaseRecyclerAdapter<CourseAdapterModel,
        courseAdapter.coursesViewholder> {
    Context c;

    public courseAdapter(@NonNull FirebaseRecyclerOptions<CourseAdapterModel> options, Context c) {
        super(options);
        this.c = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull coursesViewholder holder, int position,
                                    @NonNull CourseAdapterModel model) {
        holder.courseCode.setText(model.getCourseCode());
        holder.courseName.setText(model.getCourseName());
        holder.offeringSessions.setText(model.getOfferingSessions());
        getPrerequisites(model, holder);
        holder.deletebt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Course Deletion");
                builder.setMessage("Are you sure you want to delete " + model.getCourseCode());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        model.getRef().removeValue();
                        Toast.makeText(c, model.getCourseCode() + " has been deleted",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(c, "Course Deletion Cancelled",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.editbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, AdminEditCourse.class);
                String databaseURL = model.getRef().toString();
                String ref[] = databaseURL.split("/courses/");
                i.putExtra("key", ref[1]);
                c.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public coursesViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.acourse, parent, false);
        return new courseAdapter.coursesViewholder(view);
    }

    public void getPrerequisites(@NonNull CourseAdapterModel model, @NonNull coursesViewholder holder) {
        HashMap<String, String> prereqCodes = new HashMap<>();
        FirebaseDatabase.getInstance().getReference("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> courses = snapshot.getChildren();
                List<String> preList = new ArrayList<>();
                for(DataSnapshot child : courses){
                    prereqCodes.put(child.getKey(),child.child("courseCode").getValue(String.class));
                }
                for(int i = 0 ; i < model.courseData.prerequisites.size(); i++){
                    preList.add(prereqCodes.get(model.courseData.prerequisites.get(i)));
                }

                String pre= TextUtils.join(CourseAdapterModel.DELIMITER, preList);
                if(pre.isEmpty()) holder.prerequisites.setText("No prerequisites");
                else {
                    holder.prerequisites.setText(pre);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class coursesViewholder extends RecyclerView.ViewHolder {
        TextView courseCode;
        TextView courseName;
        TextView offeringSessions;
        TextView prerequisites;
        ImageButton deletebt;
        ImageButton editbt;
        public coursesViewholder(@NonNull View itemView) {
            super(itemView);
            courseCode = itemView.findViewById(R.id.courseCode);
            courseName = itemView.findViewById(R.id.courseName);
            offeringSessions = itemView.findViewById(R.id.offeringSessions);
            prerequisites = itemView.findViewById(R.id.prerequisites);
            deletebt = itemView.findViewById(R.id.deleteButton);
            editbt = itemView.findViewById(R.id.editButton);
        }
    }
}
