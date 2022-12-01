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

import java.util.List;

public class SCourseViewAdapter extends RecyclerView.Adapter<SCourseViewAdapter.MyHolder>{
    private List mList;

    SCourseViewAdapter(List list) {
        mList = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stucourse, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textView.setText(mList.get(position).toString());
        holder.ibt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                holder.textView.setText("Testing!!!" + position);
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
