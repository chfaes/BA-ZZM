package com.example.frontend.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import android.util.Base64;

public class Note implements Serializable {

    private int id;
    @SerializedName("patient_id")
    private int patientId;
    @SerializedName("note_bytes")
    private String noteBytesString;
    private boolean selected;

    public Note() {
    }

    public Note(int patientId, String noteBytesString, boolean selected) {
        this.patientId = patientId;
        this.noteBytesString = noteBytesString;
        this.selected = selected;
    }

    public Note(int id, int patientId, String noteBytesString, boolean selected) {
        this.id = id;
        this.patientId = patientId;
        this.noteBytesString = noteBytesString;
        this.selected = selected;
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

    public byte[] getNoteBytes() {
        byte[] bytes = Base64.decode(noteBytesString, Base64.NO_WRAP);

        return bytes;
    }

    public void setNoteBytes(byte[] noteBytes) {
        String string = Base64.encodeToString(noteBytes, Base64.NO_WRAP);
        this.noteBytesString = string;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
