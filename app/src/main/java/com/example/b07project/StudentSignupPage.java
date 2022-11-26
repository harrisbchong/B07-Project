package com.example.b07project;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
    private DatabaseReference studentsData;
    private DatabaseReference adminsData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentSignupPageBinding.inflate(inflater, container, false);

        // initialize realtime database and authentication instances
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.studentsData = FirebaseDatabase.getInstance().getReference("students");
        this.adminsData = FirebaseDatabase.getInstance().getReference("admins");

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
                String username = binding.nameInput.getText().toString();

                int checkedRadioId = binding.userTypeRadioGroup.getCheckedRadioButtonId();

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
                } else if (username == null || username.length() == 0) {
                    // reject cases where no username is given
                    Toast.makeText(activity, "You must enter a name.", Toast.LENGTH_LONG).show();
                } else {
                    String logd = "Checked radio is " + Integer.toString(checkedRadioId);
                    Log.d("tag", logd);
                    if (checkedRadioId == binding.studentSignupRadio.getId()) {
                        // if student signup is selected, try authenticating as student
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // store student data in the realtime database
                                            StudentUser student = new StudentUser(username, programName);
                                            FirebaseUser userId = firebaseAuth.getCurrentUser();
                                            studentsData.child(userId.getUid()).setValue(student);

                                            // If registration succeeds
                                            NavHostFragment.findNavController(StudentSignupPage.this)
                                                    .navigate(R.id.action_studentSignupPage_to_studentCourseView);
                                        } else {
                                            // If registration fails
                                            Toast.makeText(activity, "Email is already taken or credentials invalid.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else if (checkedRadioId == binding.adminSignupRadio.getId()) {

                        if (programName == "adminProgramPassword12345") {
                            // if admin signup is selected, try authenticating as an admin
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // store admin data in the realtime database
                                                AdminUser admin = new AdminUser(username);
                                                FirebaseUser userId = firebaseAuth.getCurrentUser();
                                                adminsData.child(userId.getUid()).setValue(admin);

                                                // If registration succeeds
                                                NavHostFragment.findNavController(StudentSignupPage.this)
                                                        .navigate(R.id.action_studentSignupPage_to_adminFrontPage);

                                            } else {
                                                // If registration fails
                                                Toast.makeText(activity, "Email is already taken or credentials invalid.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(activity, "You must provide the secret program name to make an admin account.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(activity, "Please indicate if you are a student or an admin.", Toast.LENGTH_LONG).show();
                    }
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