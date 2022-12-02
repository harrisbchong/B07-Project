package com.example.b07project;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SCourseViewAdapter extends RecyclerView.Adapter<SCourseViewAdapter.MyHolder>{
    private List mList;
    private DatabaseReference m;
    private FirebaseAuth auth;
    private FirebaseUser userId;

    SCourseViewAdapter(List list) {
        mList = list;
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

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textView.setText(mList.get(position).toString());
        holder.ibt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String str = holder.textView.getText().toString();
                String [] test = str.split("\n");
                m.child("students").child(userId.getUid()).child("taken").child(test[0]).setValue(test[0]);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageButton ibt;

        public MyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_content);
            ibt = itemView.findViewById(R.id.imageButton2);
        }
    }
}
