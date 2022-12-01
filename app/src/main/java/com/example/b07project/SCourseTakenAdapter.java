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
            //将我们自定义的item布局R.layout.item_one转换为View
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_stucoursetaken, parent, false);
            //将view传递给我们自定义的ViewHolder
            com.example.b07project.SCourseTakenAdapter.MyHolder holder = new com.example.b07project.SCourseTakenAdapter.MyHolder(view);
            //返回这个MyHolder实体
            return holder;
        }

        @Override
        public void onBindViewHolder(com.example.b07project.SCourseTakenAdapter.MyHolder holder, int position) {
            holder.textView.setText(mList.get(position).toString());
        }


        //获取数据源总的条数
        @Override
        public int getItemCount() {
            return mList.size();
        }

        /**
         * 自定义的ViewHolder
         */
        class MyHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public MyHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_content_1);
            }
        }
    }

