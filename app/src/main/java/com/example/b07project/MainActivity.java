package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signbt;
    private Button adminbt;
    private Button studentbt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signbt = (Button) findViewById(R.id.signup_button);
        signbt.setOnClickListener(this);
        adminbt = (Button) findViewById(R.id.admin_login_button);
        adminbt.setOnClickListener(this);
        studentbt = (Button) findViewById(R.id.student_login_button);
        studentbt.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.student_login_button:
                startActivity(new Intent(this, StudentLoginPage.class));
                break;
            case R.id.admin_login_button:
                startActivity(new Intent(this, AdminLoginPage.class));
                break;
            case R.id.signup_button:
                startActivity(new Intent(this, SignupPage.class));
                break;
        }
    }


}