package com.yanir.ex192_firebasedb;

public class Student {
    private String firstName;
    private String lastName;
    private int grade;
    private int classNumber;
    private int ID;
    private boolean canVaccinate;
    private Vaccine firstVaccine;
    private Vaccine secondVaccine;

    public Student() {
        firstName = "";
        lastName = "";
        grade = 0;
        classNumber = 0;
        ID = 0;
        canVaccinate = false;
        firstVaccine = new Vaccine();
        secondVaccine = new Vaccine();
    }

    public Student(String firstName, String lastName, int grade, int classNumber, int ID, boolean canVaccinate, Vaccine firstVaccine, Vaccine secondVaccine) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
        this.classNumber = classNumber;
        this.ID = ID;
        this.canVaccinate = canVaccinate;
        this.firstVaccine = firstVaccine;
        this.secondVaccine = secondVaccine;
    }

    public void setStudent(String firstName, String lastName, int grade, int classNumber, int ID, boolean canVaccinate, Vaccine firstVaccine, Vaccine secondVaccine) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
        this.classNumber = classNumber;
        this.ID = ID;
        this.canVaccinate = canVaccinate;
        this.firstVaccine = firstVaccine;
        this.secondVaccine = secondVaccine;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isCanVaccinate() {
        return canVaccinate;
    }

    public void setCanVaccinate(boolean canVaccinate) {
        this.canVaccinate = canVaccinate;
    }

    public Vaccine getFirstVaccine() {
        return firstVaccine;
    }

    public void setFirstVaccine(Vaccine firstVaccine) {
        this.firstVaccine = firstVaccine;
    }

    public Vaccine getSecondVaccine() {
        return secondVaccine;
    }

    public void setSecondVaccine(Vaccine secondVaccine) {
        this.secondVaccine = secondVaccine;
    }

}
