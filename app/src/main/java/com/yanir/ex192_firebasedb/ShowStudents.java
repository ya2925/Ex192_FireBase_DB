package com.yanir.ex192_firebasedb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class ShowStudents extends AppCompatActivity implements View.OnCreateContextMenuListener {

    ArrayList<Student> students;
    ListView listView;
    StudentListViewAdapter adapter;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        students = new ArrayList<>();

        // connect the java listView to the xml file
        listView = (ListView) findViewById(R.id.studentsLV);

        // create an adapter for the listView
        adapter = new StudentListViewAdapter(this, students);

        // set the adapter to the listView
        listView.setAdapter(adapter);

        // register the students listView for the context menu
        registerForContextMenu(listView);

        // show the students
        showStudents();

        in = getIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showStudents();
    }

    public void showStudents() {
        // get all the students from the database
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
                            // add the student to the arrayList
                            students.add(student.getValue(Student.class));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // log the error
                Log.e("ShowStudents", error.getMessage());
            }
        });
    }

    public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // edit and delete the student
        menu.add("Edit student");
        menu.add("Delete student");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        // get the selected student
        Student student = students.get(position);

        // check if the user selected the edit student option
        if (item.getTitle().equals("Edit student")) {
            // open the edit student activity
            Intent si = new android.content.Intent(this, AddStudent.class);
            // put the student object in the intent to send it to the edit student activity
            si.putExtra("student", student);
            startActivity(si);
        }
        // check if the user selected the delete student option
        else if (item.getTitle().equals("Delete student")) {
            // delete the student from the database
            FireDB.getInstance().deleteStudent(student);
            // show the students
            showStudents();
        }

        return true;
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