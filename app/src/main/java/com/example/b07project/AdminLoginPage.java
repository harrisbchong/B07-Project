package com.example.b07project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AdminLoginPage extends AppCompatActivity implements View.OnClickListener {

    private Button loginbt;
    private CheckBox remem;
    private Model model;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    private EditText etxt, ptxt;
    private ImageButton backbt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_login_page);
        backbt = (ImageButton) findViewById(R.id.back_button_3);
        backbt.setOnClickListener(this);
        etxt = (EditText) findViewById(R.id.admin_email_address_input);
        ptxt = (EditText) findViewById(R.id.admin_password_input);
        loginbt = (Button) findViewById(R.id.admin_login_submit);
        loginbt.setOnClickListener(this);
        remem = (CheckBox) findViewById(R.id.Arem);
        pref = getSharedPreferences("admins", Context.MODE_PRIVATE);
        edit = pref.edit();


        checkremember();
        model = Model.getInstance();
    }
    private void checkremember() {
        boolean remember = pref.getBoolean("remember", false);
        String email = pref.getString("email", "");
        String password = pref.getString("password", "");

        etxt.setText(email);
        ptxt.setText(password);
        remem.setChecked(remember);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.admin_login_submit:
                logIn();
                break;
            case R.id.back_button_3:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }



    private void logIn() {
        String email = etxt.getText().toString().trim();
        String password = ptxt.getText().toString().trim();
        edit.putBoolean("remember", remem.isChecked());
        edit.putString("Email", remem.isChecked()? email : "");
        edit.putString("Password", remem.isChecked()? password : "");
        edit.apply();

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

        model.alogin(email, password,this);


    }

    public void failedToLogin() {
        Toast.makeText(this, "Failed To Login.", Toast.LENGTH_LONG).show();
    }



    public void redirectToAdminFrontPage(String id) {
        Intent intent = new Intent(this,AdminFrontPage.class);
        intent.putExtra("currentid", id);
        startActivity(intent);
    }
}