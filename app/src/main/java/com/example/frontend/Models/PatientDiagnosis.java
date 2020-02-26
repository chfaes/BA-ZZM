package com.example.frontend.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PatientDiagnosis implements Serializable {

    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("diagnosistype_id")
    private int diagnosisId;
    private String comment;
    private int priority;

    public PatientDiagnosis() {
    }

    public PatientDiagnosis(int patientId, int diagnosisId, String comment, int priority) {
        this.patientId = patientId;
        this.diagnosisId = diagnosisId;
        this.comment = comment;
        this.priority = priority;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(int diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
