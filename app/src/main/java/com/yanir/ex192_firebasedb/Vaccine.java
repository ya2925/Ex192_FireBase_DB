package com.yanir.ex192_firebasedb;

import java.io.Serializable;
import java.util.Date;

public class Vaccine implements Serializable {
    private String date;
    private String location;

    public Vaccine() {
        date = "";
        location = "";
    }

    public Vaccine(String date, String location) {
        this.date = date;
        this.location = location;}

    // deep copy constructor
    public Vaccine(Vaccine vaccine) {
        this.date = vaccine.date;
        this.location = vaccine.location;
    }

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

    public boolean equals(Vaccine vaccine) {
        return this.date.equals(vaccine.date) && this.location.equals(vaccine.location);
    }

    public boolean isNull() {
        return date.equals("") && location.equals("");
    }
}
