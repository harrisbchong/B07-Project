package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.multicast.ChannelManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminEditCourse extends AppCompatActivity {
    private String[] allCourseNames = new String[]{};
    private String[] allCourseKeys = new String[]{};
    private boolean[] allCourseSelected = new boolean[]{};
    private Dialog prerequisiteDialog = null;
    private DatabaseReference courseData;
    private DatabaseReference ref;
    private String oldCode;

    private final ValueEventListener courseListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            // keep track of the courses currently selected by saving their keys
            List<String> previouslySelected = new ArrayList<>();
            for (int i = 0; i < allCourseNames.length; i++) {
                if (allCourseSelected[i]) {
                    previouslySelected.add(allCourseKeys[i]);
                }
            }

            // update fields
            Iterable<DataSnapshot> courses = snapshot.getChildren();
            allCourseNames = new String[(int) snapshot.getChildrenCount()];
            allCourseSelected = new boolean[(int) snapshot.getChildrenCount()];
            allCourseKeys = new String[(int) snapshot.getChildrenCount()];
            int index = 0;
            // extract course information
            for (DataSnapshot childSnapshot : courses) {
                Course course = childSnapshot.getValue(Course.class);
                allCourseNames[index] = course == null ? "NULL" : course.courseCode;
                allCourseSelected[index] = previouslySelected.contains(childSnapshot.getKey());
                allCourseKeys[index] = childSnapshot.getKey();
                index++;
            }

            // if the prerequisite dialog is shown, close it and make it a new one
            if (prerequisiteDialog != null && prerequisiteDialog.isShowing()) {
                prerequisiteDialog.cancel();
                prerequisiteDialog = makePrerequisiteDialog(ref);
                prerequisiteDialog.show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("firebase", "AdminCourseAdd.java onCancelled called when" +
                    "retrieving list of courses");
        }
    };

    /**
     * Make the prerequisite selection dialog interface.
     * @return The dialog interface object to be displayed
     */
    private Dialog makePrerequisiteDialog(DatabaseReference ref) {
        // create a popup to select the course prerequisites
        AlertDialog.Builder prereqDialog = new AlertDialog.Builder(this);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> prereqs = snapshot.getChildren();
                for(DataSnapshot child : prereqs){
                    for(int i = 0 ; i < allCourseKeys.length; i++){
                        if(child.getValue(String.class).equals(allCourseKeys[i])){
                            allCourseSelected[i] = true;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // specify the list of options
        prereqDialog.setMultiChoiceItems(allCourseNames, allCourseSelected,
                (dialog, which, isChecked) -> {
                    allCourseSelected[which] = isChecked;
                });

        // specify close button
        prereqDialog.setNeutralButton("Done", (dialog, which) -> {});
        prereqDialog.setTitle("Edit course prerequisites");
        return prereqDialog.create();
    }

    /**
     * Add a new course to the database, based on the inputs in user interface.
     */
    private View.OnClickListener onCourseEditSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // course code
            EditText adminEditCourseCodeInput = findViewById(R.id.adminEditCourseCodeInput);
            String courseCode = adminEditCourseCodeInput.getText().toString().trim();
            if (courseCode.isEmpty()) {
                // check if course code is empty
                adminEditCourseCodeInput.setError("Course code required.");
                adminEditCourseCodeInput.requestFocus();
                return;
            } else if (!courseCode.matches("[a-zA-Z0-9]+")) {
                // check if non-alphanumeric characters are given in the name
                adminEditCourseCodeInput.setError(
                        "Only alphanumeric characters (a-z, A-Z, 0-9) are allowed." +
                                "for the course code.");
                adminEditCourseCodeInput.requestFocus();
                return;
            } else if (Arrays.asList(allCourseNames).contains(courseCode) &&
                    !courseCode.equals(oldCode)) {
                // check if a course with the same code already exists
                adminEditCourseCodeInput.setError(
                        "A course with code " + courseCode + " already exists.");
                adminEditCourseCodeInput.requestFocus();
                return;
            }

            // course name
            EditText adminEditCourseNameInput = findViewById(R.id.adminEditCourseNameInput);
            String courseName = adminEditCourseNameInput.getText().toString().trim();
            if (courseName.isEmpty()) {
                // check if course name is empty
                adminEditCourseNameInput.setError("Course name required.");
                adminEditCourseNameInput.requestFocus();
                return;
            } else if (!courseCode.matches("[a-zA-Z0-9 ]+")) {
                // check if course name has invalid characters
                adminEditCourseNameInput.setError(
                        "Only alphanumeric characters (a-z, A-Z, 0-9) and spaces are allowed." +
                                "for the course name.");
                adminEditCourseNameInput.requestFocus();
                return;
            }

            // retrieve selected courses prerequisites
            List<String> selectedCourses = new ArrayList<>();
            for (int i = 0; i < allCourseNames.length; i++) {
                if (allCourseSelected[i]) {
                    selectedCourses.add(allCourseKeys[i]);
                }
            }

            // extract offering sessions data
            CheckBox adminEditCourseFallCheckbox = findViewById(R.id.adminEditCourseFallCheckbox);
            CheckBox adminEditCourseWinterCheckbox = findViewById(R.id.adminEditCourseWinterCheckbox);
            CheckBox adminEditCourseSummerCheckbox = findViewById(R.id.adminEditCourseSummerCheckbox);
            List<UniversitySession> offeringSessions = new ArrayList<>();
            if (adminEditCourseFallCheckbox.isChecked()) {
                offeringSessions.add(UTSCSessions.Fall);
            }
            if (adminEditCourseWinterCheckbox.isChecked()) {
                offeringSessions.add(UTSCSessions.Winter);
            }
            if (adminEditCourseSummerCheckbox.isChecked()) {
                offeringSessions.add(UTSCSessions.Summer);
            }

            // create course object and write to the database
            Course newCourse = new Course(courseCode, courseName, selectedCourses,
                    offeringSessions);
            ref.setValue(newCourse);

            // reset the interface fields
            adminEditCourseNameInput.setText("");
            adminEditCourseCodeInput.setText("");
            Arrays.fill(allCourseSelected, false);
            adminEditCourseFallCheckbox.setChecked(false);
            adminEditCourseWinterCheckbox.setChecked(false);
            adminEditCourseSummerCheckbox.setChecked(false);

            // display a success message
            AlertDialog.Builder successDialog = new AlertDialog.Builder(view.getContext());
            successDialog.setTitle("Success");
            successDialog.setMessage("The course was successfully edited.");
            successDialog.setPositiveButton("OK", (dialogInterface, i) -> onEditSuccess());
            successDialog.setCancelable(false);
            successDialog.show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_course);

        this.courseData = FirebaseDatabase.getInstance().getReference("courses");
        String key = getIntent().getStringExtra("key");
        this.ref = courseData.child(key);
        this.courseData.orderByChild("courseCode").addValueEventListener(courseListener);

        EditText adminEditCourseCodeInput = findViewById(R.id.adminEditCourseCodeInput);
        EditText adminEditCourseNameInput = findViewById(R.id.adminEditCourseNameInput);
        CheckBox adminEditCourseFallCheckbox = findViewById(R.id.adminEditCourseFallCheckbox);
        CheckBox adminEditCourseWinterCheckbox = findViewById(R.id.adminEditCourseWinterCheckbox);
        CheckBox adminEditCourseSummerCheckbox = findViewById(R.id.adminEditCourseSummerCheckbox);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                oldCode = snapshot.child("courseCode").getValue(String.class);
                adminEditCourseCodeInput.setText(oldCode);
                adminEditCourseNameInput.setText(snapshot.child("courseName").
                        getValue(String.class));
                Iterable<DataSnapshot> sessions = snapshot.child("offeringSessions").getChildren();
                for(DataSnapshot child : sessions){
                    if(child.getValue(String.class).equals("Fall")){
                        adminEditCourseFallCheckbox.setChecked(true);
                    }
                    else if(child.getValue(String.class).equals("Winter")){
                        adminEditCourseWinterCheckbox.setChecked(true);
                    }
                    else {
                        adminEditCourseSummerCheckbox.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // the back button takes user back to admin front page
        Button adminCourseEditBackButton = findViewById(R.id.adminCourseEditBackButton);
        adminCourseEditBackButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminCourseView.class));
            Toast.makeText(this, "Course Edit Cancelled", Toast.LENGTH_SHORT).show();
        });

        // make the prerequisite button show the dialog on click
        Button adminCourseEditPrereqButton = findViewById(R.id.adminCourseEditPrereqButton);
        adminCourseEditPrereqButton.setOnClickListener(v -> {
            prerequisiteDialog = makePrerequisiteDialog(ref.child("prerequisites"));
            prerequisiteDialog.show();
        });

        ImageButton adminEditCourseSubmitButton = findViewById(R.id.adminEditCourseSubmitButton);
        adminEditCourseSubmitButton.setOnClickListener(this.onCourseEditSubmit);
    }

    public void onEditSuccess() {
        Intent intent = new Intent(this,AdminCourseView.class);
        startActivity(intent);
    }
}