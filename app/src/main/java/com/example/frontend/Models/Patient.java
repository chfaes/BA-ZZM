package com.example.frontend.Models;

import java.io.Serializable;

public class Patient implements Serializable {

    private int id;

    private String shortname;

    private String gender;

    public Patient()  {

    }

    public Patient(String shortname, String gender) {
        this.shortname= shortname;
        this.gender = gender;
    }
    public Patient(int userId, String shortname, String gender) {
        this.id = userId;
        this.shortname= shortname;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getShortname() {
        return shortname;
    }

    public String getGender() {
        return gender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
