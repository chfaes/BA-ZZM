package com.example.frontend.Models;

import com.google.gson.annotations.SerializedName;

public class PatientDocument {

    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("document_path")
    private String documentPath;

    public PatientDocument() {
    }

    public PatientDocument(int patientId, String documentPath) {
        this.patientId = patientId;
        this.documentPath = documentPath;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }
}
