package com.yanir.ex192_firebasedb;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowStudents extends AppCompatActivity {

    ArrayList<Student> students;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        students = new ArrayList<>();

        // connect the java listView to the xml file
        listView = (ListView) findViewById(R.id.studentsLV);

        // create an adapter for the listView
        StudentListViewAdapter adapter = new StudentListViewAdapter(this, students);

        // set the adapter to the listView
        listView.setAdapter(adapter);

        // show the students
        showStudents();
    }

    public void showStudents() {
        // get all the students from the database
        FireDB.getInstance().getStudents().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear the students arrayList
                students = new ArrayList<>();
                // get all the grades
                for (DataSnapshot grade : snapshot.getChildren()) {
                    // get all the classes
                    for (DataSnapshot classNumber : grade.getChildren()) {
                        // get all the students
                        for (DataSnapshot student : classNumber.getChildren()) {
                            // add the student to the arrayList
                            students.add(student.getValue(Student.class));
                        }
                    }
                }
                // create an adapter for the listView
                StudentListViewAdapter adapter = new StudentListViewAdapter(ShowStudents.this, students);
                // set the adapter to the listView
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // log the error
                Log.e("ShowStudents", error.getMessage());
            }
        });

    }
}