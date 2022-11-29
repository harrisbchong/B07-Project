package com.example.b07project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class StudentSignupPage extends AppCompatActivity implements View.OnClickListener {

    private Button btnFinish;
    private ImageButton backbt;
    private EditText progtxt,nametxt,emailtxt,passtxt;
    private RadioButton adminbt, studentbt;
    private Model model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_signup_page);
        backbt = (ImageButton) findViewById(R.id.backbt);
        backbt.setOnClickListener(this);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
        nametxt = (EditText) findViewById(R.id.nametxt);
        progtxt = (EditText) findViewById(R.id.progtxt);
        emailtxt = (EditText) findViewById(R.id.emailtxt);
        passtxt = (EditText) findViewById(R.id.passtxt);
        adminbt = (RadioButton) findViewById(R.id.adminbt);
        studentbt = (RadioButton) findViewById(R.id.studentrbt);
        model = Model.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbt:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btnFinish:
                register();
                break;
        }
    }

    private void register() {
        String email = emailtxt.getText().toString().trim();
        String password = passtxt.getText().toString().trim();
        String name = nametxt.getText().toString().trim();
        String program = progtxt.getText().toString().trim();


        if (name.isEmpty()) {
            nametxt.setError("Name Required");
            nametxt.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailtxt.setError("Email Required");
            emailtxt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailtxt.setError("Improper Email Address Provided");
            emailtxt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passtxt.setError("Password Required");
            passtxt.requestFocus();
            return;
        }

        if (password.length() < 5) {
            passtxt.setError("Min Password length Is 5");
            passtxt.requestFocus();
            return;
        }

        model.register(email, password, (String ID) -> {
            if (ID == null) {
                Toast.makeText(StudentSignupPage.this, "Failed to register", Toast.LENGTH_LONG).show();
                return;
            }
            // Toast.makeText(RegisterActivity.this, "registered", Toast.LENGTH_LONG).show();
            if(studentbt.isChecked() == true){
                Student s = new Student(ID,program,email,name);
                model.addStudent(s, (Boolean created) -> {
                    if (!created) {
                        Toast.makeText(StudentSignupPage.this, "Failed to create a user!", Toast.LENGTH_LONG).show();

                        return;
                    }

                    Toast.makeText(StudentSignupPage.this, "user has been registered successfully!", Toast.LENGTH_LONG).show();


                    startActivity(new Intent(StudentSignupPage.this, MainActivity.class));
                });
            }
            if(adminbt.isChecked() == true){
                Admin a = new Admin(ID,email,name);
                model.addAdmin(a, (Boolean created) -> {
                    if (!created) {
                        Toast.makeText(StudentSignupPage.this, "Failed to create a user!", Toast.LENGTH_LONG).show();

                        return;
                    }

                    Toast.makeText(StudentSignupPage.this, "user has been registered successfully!", Toast.LENGTH_LONG).show();


                    startActivity(new Intent(StudentSignupPage.this, MainActivity.class));
                });
            }
        });

    }


}