package com.example.b07project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class StudentLoginPage extends AppCompatActivity implements View.OnClickListener {

    private Button loginbt,backbt;
    private CheckBox remem;
    private Model model;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    private EditText etxt, ptxt;
    private SPresenter presenter;
    private String email,password;
    private Boolean savedLogin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_login_page);
        backbt = (Button) findViewById(R.id.slbackbt);
        backbt.setOnClickListener(this);
        etxt = (EditText) findViewById(R.id.student_email_address_input);
        ptxt = (EditText) findViewById(R.id.student_password_input);
        loginbt = (Button) findViewById(R.id.student_login_submit);
        loginbt.setOnClickListener(this);
        remem = (CheckBox) findViewById(R.id.srbox);
        pref = getSharedPreferences("students", Context.MODE_PRIVATE);
        edit = pref.edit();
        model = Model.getInstance();
        presenter = new SPresenter(model,this);
        savedLogin = pref.getBoolean("saveLogin", false);
        if (savedLogin == true) {
            etxt.setText(pref.getString("email", ""));
            ptxt.setText(pref.getString("password", ""));
            remem.setChecked(true);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.student_login_submit:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etxt.getWindowToken(), 0);
                email = etxt.getText().toString();
                password = ptxt.getText().toString();
                if (remem.isChecked()) {
                    edit.putBoolean("saveLogin", true);
                    edit.putString("email", email);
                    edit.putString("password", password);
                    edit.commit();
                } else {
                    edit.clear();
                    edit.commit();
                }
                logIn();
                break;
            case R.id.slbackbt:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }





    private void logIn() {


        if (email.isEmpty()) {
            etxt.setError("Email Required");
            etxt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
           etxt.setError("Improper Email Address Provided");
            etxt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            ptxt.setError("Password Required");
            ptxt.requestFocus();
            return;
        }

        if (password.length() < 5) {
            ptxt.setError("Min Password length Is 5");
            ptxt.requestFocus();
            return;
        }

        presenter.stulogin(email, password);


    }

    public void failedToLogin() {
        Toast.makeText(this, "Failed To Login.", Toast.LENGTH_LONG).show();
    }



    public void redirectToStudentFrontPage(String id) {
        Intent intent = new Intent(this,StudentFrontPage.class);
        intent.putExtra("currentid", id);
        startActivity(intent);
    }

    public String getEmail(){
        email = etxt.getText().toString();
        return email;
    }

    public String getPassword(){
        password = ptxt.getText().toString();
        return password;
    }
}