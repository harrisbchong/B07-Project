package com.example.b07project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminAddCourse extends AppCompatActivity {
    private String[] allCourseNames = new String[]{};
    private String[] allCourseKeys = new String[]{};
    private boolean[] allCourseSelected = new boolean[]{};
    private Dialog prerequisiteDialog = null;
    private DatabaseReference courseData;

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
                allCourseNames[index] = course == null ? "NULL" : course.getCourseCode();
                allCourseSelected[index] = previouslySelected.contains(childSnapshot.getKey());
                allCourseKeys[index] = childSnapshot.getKey();
                index++;
            }

            // if the prerequisite dialog is shown, close it and make it a new one
            if (prerequisiteDialog != null && prerequisiteDialog.isShowing()) {
                prerequisiteDialog.cancel();
                prerequisiteDialog = makePrerequisiteDialog();
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
    private Dialog makePrerequisiteDialog() {
        // create a popup to select the course prerequisites
        AlertDialog.Builder prereqDialog = new AlertDialog.Builder(this);

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
    private View.OnClickListener onCourseAddSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // course code
            EditText adminAddCourseCodeInput = findViewById(R.id.adminAddCourseCodeInput);
            String courseCode = adminAddCourseCodeInput.getText().toString().trim();
            if (courseCode.isEmpty()) {
                // check if course code is empty
                adminAddCourseCodeInput.setError("Course code required.");
                adminAddCourseCodeInput.requestFocus();
                return;
            } else if (!courseCode.matches("[a-zA-Z0-9]+")) {
                // check if non-alphanumeric characters are given in the name
                adminAddCourseCodeInput.setError(
                        "Only alphanumeric characters (a-z, A-Z, 0-9) are allowed." +
                                "for the course code.");
                adminAddCourseCodeInput.requestFocus();
                return;
            } else if (Arrays.asList(allCourseNames).contains(courseCode)) {
                // check if a course with the same code already exists
                adminAddCourseCodeInput.setError(
                        "A course with code " + courseCode + " already exists.");
                adminAddCourseCodeInput.requestFocus();
                return;
            }

            // course name
            EditText adminAddCourseNameInput = findViewById(R.id.adminAddCourseNameInput);
            String courseName = adminAddCourseNameInput.getText().toString().trim();
            if (courseName.isEmpty()) {
                // check if course name is empty
                adminAddCourseNameInput.setError("Course name required.");
                adminAddCourseNameInput.requestFocus();
                return;
            } else if (!courseCode.matches("[a-zA-Z0-9 ]+")) {
                // check if course name has invalid characters
                adminAddCourseNameInput.setError(
                        "Only alphanumeric characters (a-z, A-Z, 0-9) and spaces are allowed." +
                                "for the course name.");
                adminAddCourseNameInput.requestFocus();
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
            CheckBox adminAddCourseFallCheckbox = findViewById(R.id.adminAddCourseFallCheckbox);
            CheckBox adminAddCourseWinterCheckbox = findViewById(R.id.adminAddCourseWinterCheckbox);
            CheckBox adminAddCourseSummerCheckbox = findViewById(R.id.adminAddCourseSummerCheckbox);
            List<UniversitySession> offeringSessions = new ArrayList<>();
            if (adminAddCourseFallCheckbox.isChecked()) {
                offeringSessions.add(UTSCSessions.Fall);
            }
            if (adminAddCourseWinterCheckbox.isChecked()) {
                offeringSessions.add(UTSCSessions.Winter);
            }
            if (adminAddCourseSummerCheckbox.isChecked()) {
                offeringSessions.add(UTSCSessions.Summer);
            }

            // create course object and write to the database
            Course newCourse = new Course(courseCode, courseName, selectedCourses,
                    offeringSessions);
            DatabaseReference newCourseRef = courseData.push();
            courseData.child(newCourseRef.getKey()).setValue(newCourse);

            // reset the interface fields
            adminAddCourseNameInput.setText("");
            adminAddCourseCodeInput.setText("");
            Arrays.fill(allCourseSelected, false);
            adminAddCourseFallCheckbox.setChecked(false);
            adminAddCourseWinterCheckbox.setChecked(false);
            adminAddCourseSummerCheckbox.setChecked(false);

            // display a success message
            AlertDialog.Builder successDialog = new AlertDialog.Builder(view.getContext());
            successDialog.setTitle("Success");
            successDialog.setMessage("Course '" + courseCode + ": " + courseName + "' " +
                    "successfully added to the course database.");
            successDialog.setPositiveButton("OK", (dialogInterface, i) -> onAddSuccess());
            successDialog.setCancelable(false);
            successDialog.show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_course);

        this.courseData = FirebaseDatabase.getInstance().getReference("courses");
        this.courseData.orderByChild("courseCode").addValueEventListener(courseListener);

        // the back button takes user back to admin front page
        Button adminCourseAddBackButton = findViewById(R.id.adminCourseAddBackButton);
        adminCourseAddBackButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminFrontPage.class));
        });

        // make the prerequisite button show the dialog on click
        Button adminCourseAddPrereqButton = findViewById(R.id.adminCourseAddPrereqButton);
        adminCourseAddPrereqButton.setOnClickListener(v -> {
            prerequisiteDialog = makePrerequisiteDialog();
            prerequisiteDialog.show();
        });

        ImageButton adminAddCourseSubmitButton = findViewById(
                R.id.adminAddCourseSubmitButton);
        adminAddCourseSubmitButton.setOnClickListener(this.onCourseAddSubmit);
    }

    public void onAddSuccess() {
        Intent intent = new Intent(this,AdminFrontPage.class);
        startActivity(intent);
    }


}