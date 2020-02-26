package com.example.frontend.Models;

import java.io.Serializable;

public class ImprovementReason implements Serializable {

    private int patient_id;
    private boolean drugs;
    private boolean exercises;
    private boolean awareness;
    private boolean other_reason;
    private String other_reason_text;

    public ImprovementReason() {
    }

    public ImprovementReason(int patient_id, boolean drugs, boolean exercises, boolean awareness, boolean other_reason, String other_reason_text) {
        this.patient_id = patient_id;
        this.drugs = drugs;
        this.exercises = exercises;
        this.awareness = awareness;
        this.other_reason = other_reason;
        this.other_reason_text = other_reason_text;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public boolean isDrugs() {
        return drugs;
    }

    public void setDrugs(boolean drugs) {
        this.drugs = drugs;
    }

    public boolean isExercises() {
        return exercises;
    }

    public void setExercises(boolean exercises) {
        this.exercises = exercises;
    }

    public boolean isAwareness() {
        return awareness;
    }

    public void setAwareness(boolean awareness) {
        this.awareness = awareness;
    }

    public boolean isOther_reason() {
        return other_reason;
    }

    public void setOther_reason(boolean other_reason) {
        this.other_reason = other_reason;
    }

    public String getOther_reason_text() {
        return other_reason_text;
    }

    public void setOther_reason_text(String other_reason_text) {
        this.other_reason_text = other_reason_text;
    }
}
