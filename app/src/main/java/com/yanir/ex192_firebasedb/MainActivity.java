package com.yanir.ex192_firebasedb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView statusTextView;
    public static FirebaseDatabase database;
    ArrayList<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database
        database = FirebaseDatabase.getInstance();
    }

    public void goToAddStudent(View view) {
        Intent intent = new Intent(this, AddStudent.class);
        startActivity(intent);
    }

    public void goToShowStudents(View view) {
        Intent intent = new Intent(this, ShowStudents.class);
        startActivity(intent);
    }

    public void printStudents(View view) {
    }


}