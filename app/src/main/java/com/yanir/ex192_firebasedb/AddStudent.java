package com.yanir.ex192_firebasedb;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;
import java.util.Date;

public class AddStudent extends AppCompatActivity {

    EditText firstNameEditText, lastNameEditText, IDEditText;
    Spinner gradeSpinner, classSpinner;
    Switch canVaccinateSwitch, isActiveSwitch;
    CheckBox firstVaccineCheckBox, secondVaccineCheckBox;
    Button saveButton, addFirstVaccineButton, addSecondVaccineButton;
    Student student;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // creat a list from 1 to 12 to set to the grade and class spinners
        String[] grades = new String[12];
        for (int i = 0; i < 12; i++) {
            grades[i] = (i + 1) + "";
        }

        // connect the views to the XML
        firstNameEditText = findViewById(R.id.student_first_name);
        lastNameEditText = findViewById(R.id.student_last_name);
        IDEditText = findViewById(R.id.student_ID);
        gradeSpinner = findViewById(R.id.student_grade);
        classSpinner = findViewById(R.id.student_class);
        canVaccinateSwitch = findViewById(R.id.student_can_vaccinate);
        firstVaccineCheckBox = findViewById(R.id.student_first_vaccine);
        secondVaccineCheckBox = findViewById(R.id.student_second_vaccine);
        saveButton = findViewById(R.id.save_button);
        addFirstVaccineButton = findViewById(R.id.add_first_vaccine);
        addSecondVaccineButton = findViewById(R.id.add_second_vaccine);

        // create an ArrayAdapter for the grade and class spinners
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, grades);
        gradeSpinner.setAdapter(gradeAdapter);
        classSpinner.setAdapter(gradeAdapter);

        // init the vaccine checkboxes and the vaccine switch
        initVaccineBoxes();

        // get the intent and check if it is on edit mode
        if (getIntent().getExtras() != null) {
            student = (Student) getIntent().getExtras().get("student");
            setStudent(student);
        }else {
            student = new Student();
        }
    }

    public void addStudent(View view) {
        // get the values from the views
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        int ID = Integer.parseInt(IDEditText.getText().toString());
        int grade = Integer.parseInt(gradeSpinner.getSelectedItem().toString());
        int classNumber = Integer.parseInt(classSpinner.getSelectedItem().toString());
        boolean canVaccinate = canVaccinateSwitch.isChecked();
        Vaccine firstVaccine = student.getFirstVaccine();
        Vaccine secondVaccine = student.getSecondVaccine();


        // check if the ID is valid
        if (!isValidID(ID)) {
            Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // set the values to the student
        student.setStudent(firstName, lastName, grade, classNumber, ID, canVaccinate , firstVaccine, secondVaccine);

        // add the student to the database
        if (FireDB.getInstance().addStudent(student)) {
            Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show();
        }
    }

    private void setStudent(Student student) {
        firstNameEditText.setText(student.getFirstName());
        lastNameEditText.setText(student.getLastName());
        IDEditText.setText(student.getID() + "");
        gradeSpinner.setSelection(student.getGrade() - 1);
        classSpinner.setSelection(student.getClassNumber() - 1);
        canVaccinateSwitch.setChecked(student.isCanVaccinate());
        firstVaccineCheckBox.setChecked(student.getFirstVaccine() != null);
        secondVaccineCheckBox.setChecked(student.getSecondVaccine() != null);
    }



    boolean isValidID(int ID) {
        if (true){
            return true;
        }
        // convert the ID to string
        String IDString = ID + "";
        int idSum = 0;
        // check if the ID is 9 digits and add 0 to the start if not
        if (IDString.length() < 9) {
            while (IDString.length() < 9) {
                IDString = "0" + IDString;
            }
        }
        // for loop for each digit in the ID
        for (int i = 0; i < 9; i++) {
            // get the digit
            int digit = Integer.parseInt(IDString.charAt(i) + "");
            // multiply the digit by 1 or 2
            int sum = digit * (i % 2 + 1);
            // if the sum is 2 digits, add the digits
            if (sum > 9) {
                sum = sum / 10 + sum % 10;
            }
            // add the sum to the total sum
            idSum += sum;
        }
        // check if the sum is a multiple of 10
        return idSum % 10 == 0;
    }


    private void initVaccineBoxes() {
        // set the checkboxes to unclickable
        firstVaccineCheckBox.setClickable(false);
        secondVaccineCheckBox.setClickable(false);

        //set an event listener for the can vaccinate switch
        canVaccinateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                firstVaccineCheckBox.setClickable(true);
            } else {
                firstVaccineCheckBox.setClickable(false);
                secondVaccineCheckBox.setClickable(false);
                firstVaccineCheckBox.setChecked(false);
                secondVaccineCheckBox.setChecked(false);
            }
        });

        // add event listener to the 2 checkboxes
        firstVaccineCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addFirstVaccineButton.setVisibility(View.VISIBLE);
                secondVaccineCheckBox.setClickable(true);
            } else {
                addFirstVaccineButton.setVisibility(View.INVISIBLE);
                secondVaccineCheckBox.setClickable(false);
            }
        });
        secondVaccineCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addSecondVaccineButton.setVisibility(View.VISIBLE);
            } else {
                addSecondVaccineButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void addFirstVaccine(View v){
        // add the second vaccine
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Add First Vaccine");
        builder.setMessage("Enter the date and location of the first vaccine");
        builder.setView(R.layout.dialog_add_vaccine);
        builder.setPositiveButton("Add", (dialog, which) -> {
            // get the dialog views
            AlertDialog dialogView = (AlertDialog) dialog;
            EditText dateEditText = dialogView.findViewById(R.id.vacTime);
            EditText locationEditText = dialogView.findViewById(R.id.vacLoc);
            // get the values from the views
            String date = dateEditText.getText().toString();
            String location = locationEditText.getText().toString();
            // set the values to the student
            student.getFirstVaccine().setDate(date);
            student.getFirstVaccine().setLocation(location);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog dialogView = builder.create();
        dialogView.show();

        EditText dateEditText = dialogView.findViewById(R.id.vacTime);
        EditText locationEditText = dialogView.findViewById(R.id.vacLoc);
        // set the values from the student to the views
        dateEditText.setText(student.getFirstVaccine().getDate());
        locationEditText.setText(student.getFirstVaccine().getLocation());
    }

    public void addSecondVaccine(View v){
        // add the second vaccine
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Second Vaccine");
        builder.setMessage("Enter the date and location of the second vaccine");
        builder.setView(R.layout.dialog_add_vaccine);
        builder.setPositiveButton("Add", (dialog, which) -> {
            // get the dialog views
            AlertDialog dialogView = (AlertDialog) dialog;
            EditText dateEditText = dialogView.findViewById(R.id.vacTime);
            EditText locationEditText = dialogView.findViewById(R.id.vacLoc);
            // get the values from the views
            String date = dateEditText.getText().toString();
            String location = locationEditText.getText().toString();
            // set the values to the student
            student.getSecondVaccine().setDate(date);
            student.getSecondVaccine().setLocation(location);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog dialogView = builder.create();
        dialogView.show();

        EditText dateEditText = dialogView.findViewById(R.id.vacTime);
        EditText locationEditText = dialogView.findViewById(R.id.vacLoc);
        // set the values from the student to the views
        dateEditText.setText(student.getSecondVaccine().getDate());
        locationEditText.setText(student.getSecondVaccine().getLocation());


    }

    public void ChooseDate(View v){
        LinearLayout layout = (LinearLayout) v.getParent();
        Date current = new Date();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // set the date to the edit text
                EditText dateEditText = layout.findViewById(R.id.vacTime);
                dateEditText.setText(dayOfMonth + "/" + month + "/" + year);
            }
        }, current.getYear(), current.getMonth(), current.getDay());

        datePickerDialog.show();
    }
}