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

import java.util.List;

public class SCourseTakenAdapter extends RecyclerView.Adapter<SCourseTakenAdapter.MyHolderST>{
        private List mList, kList;
        private DatabaseReference m;
        private FirebaseAuth auth;
        private FirebaseUser userId;

        SCourseTakenAdapter(List list_1, List list_2) {
            mList = list_1;
            kList = list_2;
        }

        @Override
        public MyHolderST onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_stucoursetaken, parent, false);
            MyHolderST holder = new MyHolderST(view);
            m = FirebaseDatabase.getInstance().getReference("students");
            auth = FirebaseAuth.getInstance();
            userId = auth.getCurrentUser();
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolderST holder, int position) {
            holder.textView.setText(mList.get(position).toString());
            holder.kView.setText(kList.get(position).toString());
            holder.ibt.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String k = holder.kView.getText().toString();
                    m.child(userId.getUid()).child("taken").child(k).removeValue();
                    holder.textView.setText("This course is successfully removed\nfrom your taken course list.");
                }
            });
        }


        @Override
        public int getItemCount() {
            return mList.size();
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

