package com.example.frontend.Models;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExercisePhoto implements Serializable {

    private int id;
    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("photo")
    private String photoBytesString;

    public ExercisePhoto() {
    }

    public ExercisePhoto(int patientId, String photoBytesString) {
        this.patientId = patientId;
        this.photoBytesString = photoBytesString;
    }

    public ExercisePhoto(int id, int patientId, String photoBytesString) {
        this.id = id;
        this.patientId = patientId;
        this.photoBytesString = photoBytesString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public byte[] getPhotoBytes() {
        byte[] bytes = Base64.decode(photoBytesString, Base64.NO_WRAP);

        return bytes;
    }

    public void setPhotoBytes(byte[] noteBytes) {
        String string = Base64.encodeToString(noteBytes, Base64.NO_WRAP);
        this.photoBytesString = string;
    }

}
