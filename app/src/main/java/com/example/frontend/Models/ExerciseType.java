package com.example.frontend.Models;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExerciseType implements Serializable {

    private String title;
    private String explanation;

    public ExerciseType() {
    }

    public ExerciseType(String title, String explanation) {
        this.title = title;
        this.explanation = explanation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
