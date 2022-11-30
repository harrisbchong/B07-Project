package com.example.b07project;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class courseAdapter extends FirebaseRecyclerAdapter<Course,
        courseAdapter.coursesViewholder> {

    public courseAdapter(@NonNull FirebaseRecyclerOptions<Course> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull coursesViewholder holder, int position,
                                    @NonNull Course model) {
        holder.courseCode.setText(model.getCourseCode());
        holder.courseName.setText(model.getCourseName());
        holder.offeringSessions.setText(model.getOfferingSessions());
        holder.prerequisites.setText(model.getPrerequisites());
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
        public coursesViewholder(@NonNull View itemView) {
            super(itemView);
            courseCode = itemView.findViewById(R.id.courseCode);
            courseName = itemView.findViewById(R.id.courseName);
            offeringSessions = itemView.findViewById(R.id.offeringSessions);
            prerequisites = itemView.findViewById(R.id.prerequisites);
        }
    }
}
