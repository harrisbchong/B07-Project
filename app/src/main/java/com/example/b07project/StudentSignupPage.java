package com.example.b07project;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.b07project.databinding.FragmentStudentSignupPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentSignupPage extends Fragment {

    private FragmentStudentSignupPageBinding binding;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentSignupPageBinding.inflate(inflater, container, false);

        // initialize realtime database and authentication instances
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseData = FirebaseDatabase.getInstance().getReference("students");

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
                String programName = binding.programInput.getText().toString();
                String studentName = binding.nameInput.getText().toString();
                FragmentActivity activity = getActivity();

                if (email == null || email.length() == 0) {
                    // reject cases where no email is given
                    Toast.makeText(activity, "You must enter an email address.", Toast.LENGTH_LONG).show();
                } else if (password == null || password.length() == 0) {
                    // reject cases where no password is given
                    Toast.makeText(activity, "You must enter a password.", Toast.LENGTH_LONG).show();
                } else if (programName == null || programName.length() == 0) {
                    // reject cases where no program is given
                    Toast.makeText(activity, "You must enter a program name.", Toast.LENGTH_LONG).show();
                } else if (studentName == null || studentName.length() == 0) {
                    // reject cases where no student name is given
                    Toast.makeText(activity, "You must enter a name.", Toast.LENGTH_LONG).show();
                } else {
                    // try authenticating if no errors are found
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // If registration succeeds
                                        NavHostFragment.findNavController(StudentSignupPage.this)
                                                .navigate(R.id.action_studentSignupPage_to_studentCourseView);

                                        // store student data in the realtime database
                                        StudentUser student = new StudentUser(programName, studentName);
                                        FirebaseUser userId = firebaseAuth.getCurrentUser();
                                        firebaseData.child(userId.getUid()).setValue(student);
                                    } else {
                                        // If registration fails
                                        Toast.makeText(activity, "Email is already taken or credentials invalid.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
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