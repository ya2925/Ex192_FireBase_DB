package com.yanir.ex192_firebasedb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Queue;

public class Sorting extends AppCompatActivity {

    Spinner typeOfSorting, additionalData, moreAdditionalData;
    ArrayList<Student> students;
    ListView listView;
    StudentListViewAdapter adapterLv;
    ArrayList<String> sortingOptions, grades, classes;
    int typePos, gradePos, classPos;
    Student currentStudent;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);

        // connect the java spinner and listView to the xml file
        typeOfSorting = (Spinner) findViewById(R.id.typeOfSorting);
        additionalData = (Spinner) findViewById(R.id.additionalData);
        additionalData.setVisibility(View.GONE);
        moreAdditionalData = (Spinner) findViewById(R.id.moreAdditionalData);
        moreAdditionalData.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listView);


        // create the sorting options
        sortingOptions = new ArrayList<>();
        sortingOptions.add("Show all student vaccinated in class");
        sortingOptions.add("Show all student vaccinated in grade");
        sortingOptions.add("Show all student vaccinated in school");
        sortingOptions.add("Show all Unable to vaccinate students in school");

        // set the default values
        typePos = 0;
        gradePos = 0;
        classPos = 0;

        // create the grades options
        grades = new ArrayList<>();
        // add values from 1 to 12
        for (int i = 1; i <= 12; i++) {
            grades.add(String.valueOf(i));
        }

        // create the classes options
        classes = new ArrayList<>();
        // add values from A to L
        for (char i = 'A'; i <= 'L'; i++) {
            classes.add(String.valueOf(i));
        }

        // create and add the adapter to the spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortingOptions);
        typeOfSorting.setAdapter(adapter);
        ArrayAdapter<String> gradesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, grades);
        additionalData.setAdapter(gradesAdapter);
        ArrayAdapter<String> classesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classes);
        moreAdditionalData.setAdapter(classesAdapter);

        students = new ArrayList<>();

        // create an adapter for the listView
        adapterLv = new StudentListViewAdapter(this, students);

        // set the adapter to the listView
        listView.setAdapter(adapterLv);

        // show the students
        updateListView();

        in = getIntent();
    }


    public void updateListView() {
        // add a listener to the spinner with a func that will show the students or grades depending on the user choice
        typeOfSorting.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                typePos = position;
                if (position == 0) {
                    additionalData.setVisibility(View.VISIBLE);
                    moreAdditionalData.setVisibility(View.VISIBLE);
                    showVacsinatedInClass(Integer.parseInt(grades.get(gradePos)), classTOInt(classes.get(classPos)));
                } else if (position == 1) {
                    additionalData.setVisibility(View.VISIBLE);
                    moreAdditionalData.setVisibility(View.GONE);
                    showVacsinatedInGrade(Integer.parseInt(grades.get(gradePos)));
                } else if (position == 2) {
                    additionalData.setVisibility(View.GONE);
                    moreAdditionalData.setVisibility(View.GONE);
                    showVacsinatedInSchool();
                } else if (position == 3) {
                    additionalData.setVisibility(View.GONE);
                    moreAdditionalData.setVisibility(View.GONE);
                    showUnableToVaccinate();
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        additionalData.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                gradePos = position;
                if (typePos == 0) {
                    showVacsinatedInClass(Integer.parseInt(grades.get(gradePos)), classTOInt(classes.get(classPos)));
                } else if (typePos == 1) {
                    showVacsinatedInGrade(Integer.parseInt(grades.get(gradePos)));
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        moreAdditionalData.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                classPos = position;
                showVacsinatedInClass( Integer.parseInt(grades.get(gradePos)), classTOInt(classes.get(classPos)));
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void showUnableToVaccinate() {
        FireDB.getInstance().getStudents().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear the students arrayList
                students.clear();
                // get all the grades
                for (DataSnapshot grade : snapshot.getChildren()) {
                    // get all the classes
                    for (DataSnapshot classNumber : grade.getChildren()) {
                        // get all the students
                        for (DataSnapshot student : classNumber.getChildren()) {
                            currentStudent = student.getValue(Student.class);

                            if ((currentStudent != null && !currentStudent.isCanVaccinate())) {
                                students.add(currentStudent);
                            }
                        }
                    }
                }
                adapterLv.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // log the error
                Log.e("ShowStudents", error.getMessage());
            }
        });
    }

    private void showVacsinatedInSchool() {
        FireDB.getInstance().getStudents().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear the students arrayList
                students.clear();
                // get all the grades
                for (DataSnapshot grade : snapshot.getChildren()) {
                    // get all the classes
                    for (DataSnapshot classNumber : grade.getChildren()) {
                        // get all the students
                        for (DataSnapshot student : classNumber.getChildren()) {
                            currentStudent = student.getValue(Student.class);

                            if ((currentStudent != null && currentStudent.isCanVaccinate()) && currentStudent.isImmune()) {
                                students.add(currentStudent);
                            }
                        }
                    }
                }
                adapterLv.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // log the error
                Log.e("ShowStudents", error.getMessage());
            }
        });
    }

    public void showVacsinatedInClass(int grade, int classNumber) {
        Query query = FireDB.getInstance().getStudents(grade, classNumber).orderByChild("lastName");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear the students arrayList
                students.clear();
                // get all the students
                for (DataSnapshot student : snapshot.getChildren()) {
                    currentStudent = student.getValue(Student.class);

                    if ((currentStudent != null && currentStudent.isCanVaccinate()) && currentStudent.isImmune()) {
                        students.add(currentStudent);
                    }
                }
                adapterLv.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // log the error
                Log.e("ShowStudents", error.getMessage());
            }
        });
    }

    public void showVacsinatedInGrade(int grade) {
        // get all the students from the database
        FireDB.getInstance().getStudents(grade).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear the students arrayList
                students.clear();
                // get all the classes
                for (DataSnapshot classNumber : snapshot.getChildren()) {
                    // get all the students
                    for (DataSnapshot student : classNumber.getChildren()) {
                        currentStudent = student.getValue(Student.class);

                        if ((currentStudent != null && currentStudent.isCanVaccinate()) && currentStudent.isImmune()) {
                            students.add(currentStudent);
                        }
                    }
                }
                adapterLv.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // log the error
                Log.e("ShowStudents", error.getMessage());
            }
        });
    }
    public static int classTOInt(String classNumber){
        return classNumber.charAt(0) - 'A' + 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getTitle().toString().equals("Home")){
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
        }
        else if (item.getTitle().toString().equals("add student")){
            in.setClass(this, AddStudent.class);
            startActivity(in);
        }
        else if (item.getTitle().toString().equals("show data")){
            in.setClass(this, ShowStudents.class);
            startActivity(in);
        }
        else if (item.getTitle().toString().equals("filter data")){
            in.setClass(this, Sorting.class);
            startActivity(in);
        }
        else if (item.getTitle().toString().equals("credits")){
            in.setClass(this, credits.class);
            startActivity(in);
        }
        in.setClass(this, MainActivity.class);
        return super.onOptionsItemSelected(item);
    }
}