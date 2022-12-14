package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SignupPage extends AppCompatActivity implements View.OnClickListener {

    private Button btnFinish, backbt;
    private EditText nametxt,emailtxt,passtxt;
    private RadioButton adminbt, studentbt;
    private Model model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup_page);
        backbt = (Button) findViewById(R.id.rbackbt);
        backbt.setOnClickListener(this);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
        nametxt = (EditText) findViewById(R.id.nametxt);
        emailtxt = (EditText) findViewById(R.id.emailtxt);
        passtxt = (EditText) findViewById(R.id.passtxt);
        adminbt = (RadioButton) findViewById(R.id.adminbt);
        studentbt = (RadioButton) findViewById(R.id.studentrbt);
        model = Model.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbackbt:
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
                Toast.makeText(SignupPage.this, "Failed To Register", Toast.LENGTH_LONG).show();
                return;
            }
            // Toast.makeText(RegisterActivity.this, "registered", Toast.LENGTH_LONG).show();
            if(studentbt.isChecked() == true){
                Student s = new Student(ID,email,name);
                model.addStudent(s, (Boolean created) -> {
                    if (!created) {
                        Toast.makeText(SignupPage.this, "Failed To Create Student", Toast.LENGTH_LONG).show();

                        return;
                    }

                    Toast.makeText(SignupPage.this, "Student Registered Successfully", Toast.LENGTH_LONG).show();


                    startActivity(new Intent(SignupPage.this, MainActivity.class));
                });
            }
            if(adminbt.isChecked() == true){
                Admin a = new Admin(ID,email,name);
                model.addAdmin(a, (Boolean created) -> {
                    if (!created) {
                        Toast.makeText(SignupPage.this, "Failed To Create Admin", Toast.LENGTH_LONG).show();

                        return;
                    }

                    Toast.makeText(SignupPage.this, "Admin Registered Successfully", Toast.LENGTH_LONG).show();


                    startActivity(new Intent(SignupPage.this, MainActivity.class));
                });
            }
        });

    }


}