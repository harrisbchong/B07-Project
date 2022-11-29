package com.example.b07project;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.function.Consumer;

public class Model {
    private static Model instance;

    private DatabaseReference stuRef;
    private DatabaseReference adRef;
    private DatabaseReference courseRef;
    private FirebaseAuth auth;
    private StudentLoginPage view1;
    private AdminLoginPage view2;
    private Model() {
        stuRef = FirebaseDatabase.getInstance().getReference("students");
        adRef = FirebaseDatabase.getInstance().getReference("admins");
        courseRef = FirebaseDatabase.getInstance().getReference("courses");
        auth = FirebaseAuth.getInstance();

    }

    public static Model getInstance() {
        if (instance == null)
            instance = new Model();
        return instance;
    }

    public void authenticate(String email, String password, Consumer<Student> callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        callback.accept(null);
                    }
                }
                else {
                    stuRef.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Student stu = snapshot.getValue(Student.class);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                callback.accept(stu);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    public void stuauthenticate(String email, String password, Consumer<Student> callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        callback.accept(null);
                    }
                }
                else {
                    stuRef.child(Objects.requireNonNull(auth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Student user = snapshot.getValue(Student.class);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                callback.accept(user);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    public void adauthenticate(String email, String password, Consumer<Admin> callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        callback.accept(null);
                    }
                }
                else {
                    adRef.child(Objects.requireNonNull(auth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Admin user = snapshot.getValue(Admin.class);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                callback.accept(user);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    public void alogin(String email, String password, AdminLoginPage a) {

        adauthenticate(email, password, (Admin user) -> {
            if (user == null) {
                a.failedToLogin();
            }
            else {
                a.redirectToAdminFrontPage(user.id);;
            }

        });
    }

    public void stulogin(String email, String password, StudentLoginPage s) {

        stuauthenticate(email, password, (Student user) -> {
            if (user == null) {
                s.failedToLogin();
            }
            else {
                s.redirectToStudentFrontPage(user.id);
            }
        });
    }

    public void getStudent(String stuID, Consumer<Student> callback) {
        stuRef.child(stuID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student stu = snapshot.getValue(Student.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    callback.accept(stu);
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {}
        });
    }

    public void addStudent(Student s, Consumer<Boolean> callback) {
        stuRef.child(s.id).setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    callback.accept(task.isSuccessful());
                }
            }
        });
    }

    public void addAdmin(Admin a, Consumer<Boolean> callback) {
        adRef.child(a.id).setValue(a).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    callback.accept(task.isSuccessful());
                }
            }
        });
    }
    public void register(String email, String password, Consumer<String> callback) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @SuppressLint("NewApi")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                callback.accept(task.isSuccessful() ? auth.getUid() : null);
            }
        });
    }



}
