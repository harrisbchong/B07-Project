package com.example.b07project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SCourseTakenAdapter extends RecyclerView.Adapter<com.example.b07project.SCourseTakenAdapter.MyHolder>{
        private List mList;

        SCourseTakenAdapter(List list) {
            mList = list;
        }

        @Override
        public com.example.b07project.SCourseTakenAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_stucoursetaken, parent, false);
            com.example.b07project.SCourseTakenAdapter.MyHolder holder = new com.example.b07project.SCourseTakenAdapter.MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(com.example.b07project.SCourseTakenAdapter.MyHolder holder, int position) {
            holder.textView.setText(mList.get(position).toString());
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public MyHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_content_1);
            }
        }
    }

