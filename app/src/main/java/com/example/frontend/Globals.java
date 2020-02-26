package com.example.frontend;

import android.graphics.Bitmap;

import com.example.frontend.Models.Patient;
import com.example.frontend.Models.User;

import java.util.Locale;

public class Globals {
    private static Globals instance;

    //Global variables
    private int fragmentWidth = 200;
    private int fragmentHeight = 200;
    Patient patient = new Patient();
    User user = new User();
    String language = Locale.getDefault().getLanguage();;

    // Restrict the constructor from being instantiated
    private Globals(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFragmentWidth() {
        return fragmentWidth;
    }

    public void setFragmentWidth(int fragmentWidth) {
        this.fragmentWidth = fragmentWidth;
    }

    public int getFragmentHeight() {
        return fragmentHeight;
    }

    public void setFragmentHeight(int fragmentHeight) {
        this.fragmentHeight = fragmentHeight;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
