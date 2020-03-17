package com.example.frontend.Models;

import android.util.Base64;

import java.io.Serializable;

public abstract class PainSuperclass implements Serializable {
    //This class is simply here to encompass PainBeginning and PainCurrent such that
    //objects of either type can be passed as "PainSuperclass" arguments to functions.

    public abstract int getPatient_id();

    public abstract void setPatient_id(int patient_id);

    public abstract int getIntensity();

    public abstract void setIntensity(int intensity);

    public abstract byte[] getLocation_teeth();

    public abstract void setLocation_teeth(byte[] location_teeth);

    public abstract byte[] getLocation_face_left();

    public abstract void setLocation_face_left(byte[] location_face_left);

    public abstract byte[] getLocation_face_right();

    public abstract void setLocation_face_right(byte[] location_face_right);

    public abstract String getPain_pattern();

    public abstract void setPain_pattern(String pain_pattern);

    public abstract boolean isDull();

    public abstract void setDull(boolean dull);

    public abstract boolean isPulling();

    public abstract void setPulling(boolean pulling);

    public abstract boolean isStinging();

    public abstract void setStinging(boolean stinging);

    public abstract boolean isPulsating();

    public abstract void setPulsating(boolean pulsating);

    public abstract boolean isBurning();

    public abstract void setBurning(boolean burning);

    public abstract boolean isPinsneedles();

    public abstract void setPinsneedles(boolean pinsneedles);

    public abstract boolean isTingling();

    public abstract void setTingling(boolean tingling);

    public abstract boolean isNumb();

    public abstract void setNumb(boolean numb);

    public abstract String getComment();

    public abstract void setComment(String comment);
}
