package com.example.b07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class AdminFrontPage extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View adminFrontPageLayout = inflater.inflate(R.layout.fragment_admin_front_page, container, false);
        return adminFrontPageLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.add_course_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminFrontPage.this).navigate(R.id.action_adminFrontPage_to_adminCourseAddPage);
            }
        });

        view.findViewById(R.id.browse_course_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(AdminFrontPage.this).navigate(R.id.action_adminFrontPage_to_adminBrowseCourseViewPage);
            }
        });

    }

}