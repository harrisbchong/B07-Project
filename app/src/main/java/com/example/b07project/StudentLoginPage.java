package com.example.b07project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.b07project.databinding.FragmentStudentLoginPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentLoginPage extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.fragment_student_login_page);
        backbt = (ImageButton) findViewById(R.id.back_button);
        backbt.setOnClickListener(this);
        etxt = (EditText) findViewById(R.id.student_email_address_input);
        ptxt = (EditText) findViewById(R.id.student_password_input);
        loginbt = (Button) findViewById(R.id.student_login_submit);
        loginbt.setOnClickListener(this);
        remem = (CheckBox) findViewById(R.id.srbox);
        model = Model.getInstance();
        pref = getSharedPreferences("students", Context.MODE_PRIVATE);
        edit = pref.edit();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.student_login_submit:
                logIn();
                break;
            case R.id.back_button:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private void checkremember() {
        boolean remember = pref.getBoolean("remember", false);
        String email = pref.getString("email", "");
        String password = pref.getString("password", "");

        etxt.setText(email);
        ptxt.setText(password);
        remem.setChecked(remember);

    }




    private void logIn() {
        String email = etxt.getText().toString().trim();
        String password = ptxt.getText().toString().trim();

        edit.putBoolean("Remembered", remem.isChecked());
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

        model.stulogin(email, password,this);


    }

    public void failedToLogin() {
        Toast.makeText(this, "Failed To Login.", Toast.LENGTH_LONG).show();
    }



    public void redirectToStudentFrontPage(String id) {
        Intent intent = new Intent(this,StudentFrontPage.class);
        intent.putExtra("currentid", id);
        startActivity(intent);
    }

}