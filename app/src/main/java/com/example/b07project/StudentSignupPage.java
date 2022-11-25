package com.example.b07project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.b07project.databinding.FragmentStudentSignupPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StudentSignupPage extends Fragment {

    private FragmentStudentSignupPageBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentSignupPageBinding.inflate(inflater, container, false);
        this.firebaseAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.studentSignupSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailAddressInput.getText().toString();
                String password = binding.passwordInput.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // If registration succeeds
                                    NavHostFragment.findNavController(StudentSignupPage.this)
                                            .navigate(R.id.action_studentSignupPage_to_studentCourseView);
                                } else {
                                    // If registration fails
                                    Toast.makeText(getActivity(), "Email is already taken or credentials invalid.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        binding.backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(StudentSignupPage.this).navigate(R.id.action_studentSignupPage_to_studentFrontPage);
            }
        });
    }
}