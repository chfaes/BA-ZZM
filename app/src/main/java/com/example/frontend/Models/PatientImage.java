package com.example.frontend.Models;

import com.google.gson.annotations.SerializedName;

public class PatientImage {

    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("image_path")
    private String imagePath;

    public PatientImage() {
    }

    public PatientImage(int patientId, String imagePath) {
        this.patientId = patientId;
        this.imagePath = imagePath;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
