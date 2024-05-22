package com.yanir.ex192_firebasedb;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FireDB {
    private static FirebaseDatabase database;
    private static FireDB instance;
    public FireDB() {
        initDB();
    }
    public static void initDB() {
        // check if there is a DB connection
        if (database == null) {
            // Create a new database instance
            database = FirebaseDatabase.getInstance();
        }
    }

    public static FireDB getInstance() {
        if (instance == null) {
            instance = new FireDB();
        }
        return instance;
    }



    public Boolean addStudent(Student student) {
        DatabaseReference redGrades = database.getReference("grades");
        try {
            redGrades.child(student.getGrade() + "").child(student.getClassNumber() + "").child(student.getID() + "").setValue(student);
            if (redGrades.child(student.getGrade() + "").child(student.getClassNumber() + "").child(student.getID() + "").getKey() != null) {
                return true;
            }
        } catch (Exception e) {
            // log the exception
            Log.e("FireDB", e.getMessage());
        }
        return false;
    }

    public DatabaseReference getStudents() {
        DatabaseReference redGrades = database.getReference("grades");
        return redGrades;
    }

    public void deleteStudent(Student student) {
        DatabaseReference redGrades = database.getReference("grades");
        redGrades.child(student.getGrade() + "").child(student.getClassNumber() + "").child(student.getID() + "").removeValue();
    }
}
