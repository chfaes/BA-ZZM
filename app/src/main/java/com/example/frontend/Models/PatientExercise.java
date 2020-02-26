package com.example.frontend.Models;

import com.google.gson.annotations.SerializedName;

public class PatientExercise {

    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("exercisetype_title")
    private String exerciseTypeTitle;

    public PatientExercise() {
    }

    public PatientExercise(int patientId, String exerciseTypeTitle) {
        this.patientId = patientId;
        this.exerciseTypeTitle = exerciseTypeTitle;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getExerciseTypeTitle() {
        return exerciseTypeTitle;
    }

    public void setExerciseTypeTitle(String exerciseTypeTitle) {
        this.exerciseTypeTitle = exerciseTypeTitle;
    }
}
