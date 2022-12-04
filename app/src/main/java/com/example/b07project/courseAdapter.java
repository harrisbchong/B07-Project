package com.example.b07project;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

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
        holder.prerequisites.setText(model.getPrerequisites());
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
                .inflate(R.layout.course, parent, false);
        return new courseAdapter.coursesViewholder(view);
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
