package com.example.b07project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SCourseViewAdapter extends RecyclerView.Adapter<SCourseViewAdapter.MyHolder>{
    private List mList, kList;
    private DatabaseReference m;
    private FirebaseAuth auth;
    private FirebaseUser userId;
    private String[] CourseCodes = new String[]{};
    private String[] CourseKeys = new String[]{};
    private int courseNum;

    SCourseViewAdapter(List list_1, List list_2) {
        mList = list_1;
        kList = list_2;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stucourse, parent, false);
        MyHolder holder = new MyHolder(view);
        m = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser();
        return holder;
    }

    public int exist(String m, String[] list){
        for(String str: list){
            if (str.equals(m)){
                return 1;
            }
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textView.setText(mList.get(position).toString());
        holder.kView.setText(kList.get(position).toString());
        holder.ibt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String k = holder.kView.getText().toString();
                String str = holder.textView.getText().toString();
                String [] test = str.split("\n");
                m.child("students").child(userId.getUid()).child("taken").get().addOnCompleteListener
                        (new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            courseNum = (int)task.getResult().getChildrenCount();
                            Iterable<DataSnapshot> courses = task.getResult().getChildren();
                            CourseCodes = new String[courseNum];
                            CourseKeys = new String[courseNum];

                            int index = 0;
                            for (DataSnapshot childSnapshot : courses) {
                                CourseCodes[index] = childSnapshot.getValue(String.class);
                                CourseKeys[index] = childSnapshot.getKey();
                                index++;
                            }

                            Log.e("TextView", test[3]);
                            if(exist(k, CourseKeys) == 1){
                                Toast.makeText(v.getContext(), "You have already taken this course",
                                Toast.LENGTH_SHORT).show();
                            }
                            else if(test[3].equals("No prerequisites")){
                                m.child("students").child(userId.getUid()).child("taken").child(k).setValue(test[0]);
                                Toast.makeText(v.getContext(), test[0] + " is successfully added to the taken courses",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String s = test[3].substring(15);
                                String [] pre = s.split(", ");
                                int n = pre.length, count = 0;
                                for(int i = 0; i < n; i++){
                                    count = count + exist(pre[i], CourseCodes);
                                }
                                if(count == n){
                                    m.child("students").child(userId.getUid()).child("taken").child(k).setValue(test[0]);
                                    Toast.makeText(v.getContext(), test[0] + " is successfully added to the taken courses",
                                            Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(v.getContext(), "The prerequisites for this course are not met",
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
        return mList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView textView, kView;
        ImageButton ibt;

        public MyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_content);
            kView = itemView.findViewById(R.id.k_content);
            ibt = itemView.findViewById(R.id.imageButton2);
        }
    }
}

