package com.yanir.ex192_firebasedb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class StudentListViewAdapter extends ArrayAdapter<Student> {
    private final Activity context;
    private final ArrayList<Student> students;

    public StudentListViewAdapter(Activity context, ArrayList<Student> students) {
        super(context, R.layout.student_item, students);

        this.context = context;
        this.students = students;
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.student_item, null, true);

        TextView name = (TextView) rowView.findViewById(R.id.stuName);
        TextView grade = (TextView) rowView.findViewById(R.id.stuInfo);
        ImageView canVaccinate = (ImageView) rowView.findViewById(R.id.canVac);
        ImageView firstVaccine = (ImageView) rowView.findViewById(R.id.firstVac);
        ImageView secondVaccine = (ImageView) rowView.findViewById(R.id.secVac);

        name.setText(students.get(position).getFirstName() + " " + students.get(position).getLastName());
        // convert the grade to string 1 - A , 2 - B , 3 - C , 4 - D , 5 - F and so on until 12
        grade.setText((char) (students.get(position).getGrade() + 64) + " " + students.get(position).getClassNumber());
        // check if the student can vaccinate
        if (students.get(position).isCanVaccinate()) {
            canVaccinate.setImageResource(R.drawable.candovac);
            // check if the student got the first vaccine
            if (!students.get(position).getFirstVaccine().getDate().isEmpty()) {
                firstVaccine.setImageResource(R.drawable.donevac);
                // check if the student got the second vaccine
                if (!students.get(position).getSecondVaccine().getDate().isEmpty()) {
                    secondVaccine.setImageResource(R.drawable.donevac);
                } else {
                    secondVaccine.setImageResource(R.drawable.notdonevac);
                }
            } else {
                firstVaccine.setImageResource(R.drawable.notdonevac);
                secondVaccine.setImageResource(R.drawable.notdonevac);
            }
            // check if the student got the second vaccine

        } else {
            canVaccinate.setImageResource(R.drawable.cnotdovac);
            //set the first vaccine image and the second vaccine image to invisible
            firstVaccine.setVisibility(View.INVISIBLE);
            secondVaccine.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }
}