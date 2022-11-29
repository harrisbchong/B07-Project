package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b07project.databinding.FragmentAdminFrontPageBinding;
import com.example.b07project.databinding.FragmentAdminLoginPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminFrontPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser ad;
    private DatabaseReference aRef;
    private String id;
    private Button logoutbt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_front_page);
        aRef = FirebaseDatabase.getInstance().getReference("admins");
        logoutbt = (Button) findViewById(R.id.alogoutbt);
        logoutbt.setOnClickListener(this);
        ad = FirebaseAuth.getInstance().getCurrentUser();
        id = ad.getUid();

        aRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student s = snapshot.getValue(Student.class);
                if (s != null) {
                    TextView name = findViewById(R.id.sname);
                    name.setText(s.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminFrontPage.this, "Admin Not Found", Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alogoutbt:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

}