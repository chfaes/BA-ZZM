package com.example.frontend.Models;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class PainSuperclass implements Serializable {
    //This class is here to encompass PainBeginning and PainCurrent such that
    //objects of either type can be passed as "PainSuperclass" arguments to functions.

    private Map<String, ArrayList<Float>> values = new HashMap();
    private String values_encoded;
    private String testString; //Zitrone
    private  ArrayList<String> pain_list = new ArrayList<String>(Arrays.asList("pulsating", "pulling", "numb", "stinging", "burning", "pinsandneedles", "tingling", "dull"));

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

    public void setTestString(String testString){
        this.testString = testString; //Zitrone
    }

    public String getTestString(){
        return testString; //Zitrone
    }

    public void setPainCoordinates(Float x, Float y, Float z, String painType){
        //Stores the x, y and z coordinates of a pain type in the hashmap.
        ArrayList<Float> templist = new ArrayList<>(Arrays.asList((float) x, y, z));
        values.put(painType, templist);
        encoding();
    }

    public ArrayList getPainCoordinates(String painType){
        //returns [-1.0, -1.0, -1.0] if painType does not exist in the hashmap.
        decoding();
        ArrayList<Float> templist = new ArrayList<>(Arrays.asList((float) -1.0f, -1.0f, -1.0f));
        if (values.get(painType) != null ){
            templist.clear();
            templist = values.get(painType);
        }
        return templist;
    }

    public boolean painIsSet(String painType){
        //checks if painType is part of the hashmap
        return values.get(painType) != null;
    }

    public String getValues_encoded(){
        return values_encoded;
    }

    public void setValues_encoded(String str){
        values_encoded = str;
    }

    public ArrayList getPainList(){return pain_list;}

    private void encoding(){
        //writes "values" to "values_encoded".
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(values);
            so.flush();
            values_encoded = Base64.encodeToString(bo.toByteArray(), Base64.NO_WRAP);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void decoding(){
        //updates "values" from "values_encoded"
        try {
            byte[] b = Base64.decode(values_encoded, Base64.NO_WRAP);
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            Map obj = (Map) si.readObject();
            values = obj;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
