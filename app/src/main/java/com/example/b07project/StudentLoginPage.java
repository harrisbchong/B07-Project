package com.example.b07project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.b07project.databinding.FragmentStudentLoginPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StudentLoginPage extends Fragment {

    private FragmentStudentLoginPageBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentStudentLoginPageBinding.inflate(inflater, container, false);
        this.mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.studentLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.studentEmailAddressInput.getText().toString();
                String password = binding.studentPasswordInput.getText().toString();

                if (email == null || email.length() == 0) {
                    Toast.makeText(getActivity(), "You must enter an email address.", Toast.LENGTH_LONG).show();
                } else if (password == null || password.length() == 0) {
                    Toast.makeText(getActivity(), "You must enter a password.", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // If login succeeds
                                        NavHostFragment.findNavController(StudentLoginPage.this)
                                                .navigate(R.id.action_studentLoginPage_to_studentCourseView);
                                    } else {
                                        // If login fails
                                        Toast.makeText(getActivity(), "Email or password is incorrect.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(StudentLoginPage.this).navigate(R.id.action_studentLoginPage_to_studentFrontPage);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}