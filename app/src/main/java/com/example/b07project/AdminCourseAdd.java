package com.example.b07project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.b07project.databinding.FragmentAdminCourseAddBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminCourseAdd extends Fragment {

    private FragmentAdminCourseAddBinding binding;
    private String[] allCourseNames = new String[]{};
    private String[] allCourseKeys = new String[]{};
    private boolean[] allCourseSelected = new boolean[]{};
    private Dialog prerequisiteDialog = null;

    private final ValueEventListener courseListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d("firebase", "Called onDataChange");
            // keep track of the courses currently selected by saving their keys
            List<String> previouslySelected = new ArrayList<>();
            for (int i = 0; i < allCourseNames.length; i++) {
                if (allCourseSelected[i]) {
                    previouslySelected.add(allCourseKeys[i]);
                }
            }

            // refresh fields
            Iterable<DataSnapshot> courses = snapshot.getChildren();
            allCourseNames = new String[(int) snapshot.getChildrenCount()];
            allCourseSelected = new boolean[(int) snapshot.getChildrenCount()];
            allCourseKeys = new String[(int) snapshot.getChildrenCount()];
            int index = 0;
            for (DataSnapshot childSnapshot : courses) {
                Course course = childSnapshot.getValue(Course.class);
                allCourseNames[index] = course == null ? "NULL" : course.courseCode;
                allCourseSelected[index] = previouslySelected.contains(childSnapshot.getKey());
                allCourseKeys[index] = childSnapshot.getKey();
                index++;
            }

            Log.d("firebase", "Status of dialog: " + Boolean.toString(prerequisiteDialog != null));

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

    private Dialog makePrerequisiteDialog() {
        // create a popup to select the course prerequisites
        AlertDialog.Builder prereqDialog = new AlertDialog.Builder(getActivity());

        // specify the list of options
        prereqDialog.setMultiChoiceItems(allCourseNames, allCourseSelected,
                (dialog, which, isChecked) -> {
                    allCourseSelected[which] = isChecked;
                });
        // specify close button
        prereqDialog.setNeutralButton("Done", (dialog, which) -> {});
        prereqDialog.setTitle("Edit course prerequisites");
        Dialog dialog = prereqDialog.create();
        return dialog;
    }

    private DatabaseReference courseData;

    public AdminCourseAdd() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminCourseAddBinding.inflate(inflater, container, false);

        this.courseData = FirebaseDatabase.getInstance().getReference("courses");
        this.courseData.orderByChild("courseCode").addValueEventListener(courseListener);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.adminCourseAddBackButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminCourseAdd.this)
                    .navigate(R.id.action_adminCourseAdd_to_adminFrontPage);
        });

        binding.adminCourseAddPrereqButton.setOnClickListener(v -> {
            prerequisiteDialog = makePrerequisiteDialog();
            prerequisiteDialog.show();
        });

        binding.adminAddCourseSubmitButton.setOnClickListener(inputView -> {
            // course code
            String courseCode = binding.adminAddCourseCodeInput.getText().toString().trim();
            if (courseCode.isEmpty()) {
                // check if course code is empty
                binding.adminAddCourseCodeInput.setError("Course code required.");
                binding.adminAddCourseCodeInput.requestFocus();
                return;
            } else if (!courseCode.matches("[a-zA-Z0-9]+")) {
                // check if non-alphanumeric characters are given in the name
                binding.adminAddCourseCodeInput.setError(
                        "Only alphanumeric characters (a-z, A-Z, 0-9) are allowed." +
                                "for the course code.");
                binding.adminAddCourseCodeInput.requestFocus();
                return;
            } else if (Arrays.asList(allCourseNames).contains(courseCode)) {
                // check if a course with the same code already exists
                binding.adminAddCourseCodeInput.setError(
                        "A course with code " + courseCode + " already exists.");
                binding.adminAddCourseCodeInput.requestFocus();
                return;
            }

            // course name
            String courseName = binding.adminAddCourseNameInput.getText().toString().trim();
            if (courseName.isEmpty()) {
                // check if course name is empty
                binding.adminAddCourseNameInput.setError("Course name required.");
                binding.adminAddCourseNameInput.requestFocus();
                return;
            } else if (!courseCode.matches("[a-zA-Z0-9 ]+")) {
                // check if course name has invalid characters
                binding.adminAddCourseNameInput.setError(
                        "Only alphanumeric characters (a-z, A-Z, 0-9) and spaces are allowed." +
                                "for the course name.");
                binding.adminAddCourseNameInput.requestFocus();
                return;
            }

            // retrieve selected courses prerequisites
            List<String> selectedCourses = new ArrayList<>();
            for (int i = 0; i < allCourseNames.length; i++) {
                if (allCourseSelected[i]) {
                    selectedCourses.add(allCourseNames[i]);
                }
            }

            // offering sessions
            List<UniversitySession> offeringSessions = new ArrayList<>();
            if (binding.adminAddCourseFallCheckbox.isChecked()) {
                offeringSessions.add(UTSCSessions.Fall);
            }
            if (binding.adminAddCourseWinterCheckbox.isChecked()) {
                offeringSessions.add(UTSCSessions.Winter);
            }
            if (binding.adminAddCourseSummerCheckbox.isChecked()) {
                offeringSessions.add(UTSCSessions.Summer);
            }

            Course newCourse = new Course(courseCode, courseName, selectedCourses,
                    offeringSessions);
            DatabaseReference newCourseRef = courseData.push();
            courseData.child(newCourseRef.getKey()).setValue(newCourse);

            binding.adminAddCourseNameInput.setText("");
            binding.adminAddCourseCodeInput.setText("");
            Arrays.fill(allCourseSelected, false);
            binding.adminAddCourseFallCheckbox.setChecked(false);
            binding.adminAddCourseWinterCheckbox.setChecked(false);
            binding.adminAddCourseSummerCheckbox.setChecked(false);

            NavHostFragment.findNavController(AdminCourseAdd.this)
                    .navigate(R.id.action_adminCourseAdd_to_adminFrontPage);

            AlertDialog.Builder successDialog = new AlertDialog.Builder(getActivity());
            successDialog.setTitle("Success");
            successDialog.setMessage("Course '" + courseCode + ": " + courseName + "' " +
                    "successfully added to the course database.");
            successDialog.setPositiveButton("OK", (dialog, which) -> {});
            successDialog.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}