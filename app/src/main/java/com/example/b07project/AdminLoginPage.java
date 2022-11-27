package com.example.b07project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.b07project.databinding.FragmentAdminLoginPageBinding;
import com.example.b07project.databinding.FragmentStudentLoginPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminLoginPage extends Fragment {

    private FragmentAdminLoginPageBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminLoginPageBinding.inflate(inflater, container, false);
        this.mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        binding.adminLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.adminEmailAddressInput.getText().toString();
                String password = binding.adminPasswordInput.getText().toString();

                if ((email == null || email.length() == 0)||
                        (password == null || password.length() == 0)) {
                    Toast.makeText(getActivity(), "email or password cannot be empty.", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // If login succeeds
                                        FirebaseUser userId = mAuth.getCurrentUser();

                                        mDatabase.child("admins").child(userId.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Error getting data, so cannot login", Toast.LENGTH_LONG).show();
                                                } else {
                                                    if(task.getResult().getValue()==null){
                                                        //if the user account is not an admin account
                                                        Toast.makeText(getActivity(), "This is not an admin account", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        //if the account is an admin account
                                                        NavHostFragment.findNavController(AdminLoginPage.this)
                                                                .navigate(R.id.action_adminLoginPage_to_adminFrontPage);
                                                    }
                                                }
                                            }
                                        });
                                    } else {
                                        // If login fails
                                        Toast.makeText(getActivity(), "Email or password is incorrect.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        binding.backButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminLoginPage.this).navigate(R.id.action_adminLoginPage_to_studentFrontPage);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}