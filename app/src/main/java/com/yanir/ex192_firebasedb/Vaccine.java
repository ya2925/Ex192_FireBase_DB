package com.yanir.ex192_firebasedb;

import java.util.Date;

public class Vaccine {
    private String date;
    private String location;

    public Vaccine() {
        date = "";
        location = "";
    }

    public Vaccine(String date, String location) {
        this.date = date;
        this.location = location;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
