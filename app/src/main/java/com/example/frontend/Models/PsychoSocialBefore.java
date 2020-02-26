package com.example.frontend.Models;

import java.io.Serializable;

public class PsychoSocialBefore implements Serializable {

    private int patient_id;

    private int pain_xpos;
    private int pain_ypos;
    private int pain_color;
    private int pain_size;

    private int family_xpos;
    private int family_ypos;
    private int family_color;
    private int family_size;

    private int work_xpos;
    private int work_ypos;
    private int work_color;
    private int work_size;

    private int finance_xpos;
    private int finance_ypos;
    private int finance_color;
    private int finance_size;

    private int event_xpos;
    private int event_ypos;
    private int event_color;
    private int event_size;

    public PsychoSocialBefore() {
    }

    public PsychoSocialBefore(int patient_id, int pain_xpos, int pain_ypos, int pain_color, int pain_size,
                              int family_xpos, int family_ypos, int family_color, int family_size,
                              int work_xpos, int work_ypos, int work_color, int work_size,
                              int finance_xpos, int finance_ypos, int finance_color, int finance_size,
                              int event_xpos, int event_ypos, int event_color, int event_size) {
        this.patient_id = patient_id;
        this.pain_xpos = pain_xpos;
        this.pain_ypos = pain_ypos;
        this.pain_color = pain_color;
        this.pain_size = pain_size;
        this.family_xpos = family_xpos;
        this.family_ypos = family_ypos;
        this.family_color = family_color;
        this.family_size = family_size;
        this.work_xpos = work_xpos;
        this.work_ypos = work_ypos;
        this.work_color = work_color;
        this.work_size = work_size;
        this.finance_xpos = finance_xpos;
        this.finance_ypos = finance_ypos;
        this.finance_color = finance_color;
        this.finance_size = finance_size;
        this.event_xpos = event_xpos;
        this.event_ypos = event_ypos;
        this.event_color = event_color;
        this.event_size = event_size;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getPain_xpos() {
        return pain_xpos;
    }

    public void setPain_xpos(int pain_xpos) {
        this.pain_xpos = pain_xpos;
    }

    public int getPain_ypos() {
        return pain_ypos;
    }

    public void setPain_ypos(int pain_ypos) {
        this.pain_ypos = pain_ypos;
    }

    public int getFamily_xpos() {
        return family_xpos;
    }

    public void setFamily_xpos(int family_xpos) {
        this.family_xpos = family_xpos;
    }

    public int getFamily_ypos() {
        return family_ypos;
    }

    public void setFamily_ypos(int family_ypos) {
        this.family_ypos = family_ypos;
    }

    public int getWork_xpos() {
        return work_xpos;
    }

    public void setWork_xpos(int work_xpos) {
        this.work_xpos = work_xpos;
    }

    public int getWork_ypos() {
        return work_ypos;
    }

    public void setWork_ypos(int work_ypos) {
        this.work_ypos = work_ypos;
    }

    public int getFinance_xpos() {
        return finance_xpos;
    }

    public void setFinance_xpos(int finance_xpos) {
        this.finance_xpos = finance_xpos;
    }

    public int getFinance_ypos() {
        return finance_ypos;
    }

    public void setFinance_ypos(int finance_ypos) {
        this.finance_ypos = finance_ypos;
    }

    public int getEvent_xpos() {
        return event_xpos;
    }

    public void setEvent_xpos(int event_xpos) {
        this.event_xpos = event_xpos;
    }

    public int getEvent_ypos() {
        return event_ypos;
    }

    public void setEvent_ypos(int event_ypos) {
        this.event_ypos = event_ypos;
    }

    public int getPain_color() {
        return pain_color;
    }

    public void setPain_color(int pain_color) {
        this.pain_color = pain_color;
    }

    public int getPain_size() {
        return pain_size;
    }

    public void setPain_size(int pain_size) {
        this.pain_size = pain_size;
    }

    public int getFamily_color() {
        return family_color;
    }

    public void setFamily_color(int family_color) {
        this.family_color = family_color;
    }

    public int getFamily_size() {
        return family_size;
    }

    public void setFamily_size(int family_size) {
        this.family_size = family_size;
    }

    public int getWork_color() {
        return work_color;
    }

    public void setWork_color(int work_color) {
        this.work_color = work_color;
    }

    public int getWork_size() {
        return work_size;
    }

    public void setWork_size(int work_size) {
        this.work_size = work_size;
    }

    public int getFinance_color() {
        return finance_color;
    }

    public void setFinance_color(int finance_color) {
        this.finance_color = finance_color;
    }

    public int getFinance_size() {
        return finance_size;
    }

    public void setFinance_size(int finance_size) {
        this.finance_size = finance_size;
    }

    public int getEvent_color() {
        return event_color;
    }

    public void setEvent_color(int event_color) {
        this.event_color = event_color;
    }

    public int getEvent_size() {
        return event_size;
    }

    public void setEvent_size(int event_size) {
        this.event_size = event_size;
    }
}
