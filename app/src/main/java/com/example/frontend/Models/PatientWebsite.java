package com.example.frontend.Models;

import com.google.gson.annotations.SerializedName;

public class PatientWebsite {

    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("website_id")
    private int websiteTypeId;

    public PatientWebsite() {
    }

    public PatientWebsite(int patientId, int websiteTypeId) {
        this.patientId = patientId;
        this.websiteTypeId = websiteTypeId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getWebsiteTypeId() {
        return websiteTypeId;
    }

    public void setWebsiteTypeId(int websiteTypeId) {
        this.websiteTypeId = websiteTypeId;
    }
}
