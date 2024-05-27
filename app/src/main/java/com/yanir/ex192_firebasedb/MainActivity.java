package com.yanir.ex192_firebasedb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textTotalVaccinated, textTotalUnvaccinated;
    public static FirebaseDatabase database;
    ArrayList<Student> students;
    int count;


    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTotalVaccinated = findViewById(R.id.textTotalVaccinated);
        textTotalUnvaccinated = findViewById(R.id.textTotalUnvaccinated);

        // Initialize the database
        database = FirebaseDatabase.getInstance();
        in = getIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countStudents(true);
    }

    public void goToAddStudent(View view) {
        in = new Intent(this, AddStudent.class);
        startActivity(in);
    }

    public void goToShowStudents(View view) {
        in = new Intent(this, ShowStudents.class);
        startActivity(in);
    }

    public void goToSortStudents(View view) {
        in = new Intent(this, Sorting.class);
        startActivity(in);
    }


    public void countStudents(boolean Vaccinate) {
        count = 0;
        FireDB.getInstance().getStudents().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get all the grades
                for (DataSnapshot grade : snapshot.getChildren()) {
                    // get all the classes
                    for (DataSnapshot classNumber : grade.getChildren()) {
                        // get all the students
                        for (DataSnapshot student : classNumber.getChildren()) {
                            if (Vaccinate) {
                                if (student.getValue(Student.class).isImmune()) {
                                    count++;

                                }
                            } else {
                                if (!student.getValue(Student.class).isImmune()) {
                                    count++;
                                }

                            }
                        }
                    }
                    if (Vaccinate) {
                        textTotalVaccinated.setText(count+"");
                        countStudents(false);
                    } else {
                        textTotalUnvaccinated.setText(count+"");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // log the error
                Log.e("ShowStudents", error.getMessage());
            }
        });
    }


    /**
     * This function presents the options menu for moving between activities.
     * @param menu The options menu in which you place your items.
     * @return true in order to show the menu, otherwise false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getTitle().toString().equals("Home")){
            in.setClass(this, MainActivity.class);
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