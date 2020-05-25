package com.example.frontend.Models;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PainBeginning implements Serializable {
    private int patient_id;
    private int intensity;
    private String location_teeth;
    private String location_face_left;
    private String location_face_right;
    private String pain_pattern;
    private boolean dull;
    private boolean pulling;
    private boolean stinging;
    private boolean pulsating;
    private boolean burning;
    private boolean pinsneedles;
    private boolean tingling;
    private boolean numb;
    private boolean electric;
    private boolean pressing;
    private String comment;
    private static final long serialVersionUID = -5066376132252162292L; //dirty fix: copied the value from the error message; couldn't decode properly.
    private ProxyBitmap Photography;
    private Map<String, ArrayList<Float>> values = new HashMap();
    private  ArrayList<String> pain_list = new ArrayList<String>(Arrays.asList("pulsating",
            "pulling", "numb", "stinging", "burning", "tingling", "pins and needles", "dull", "electric", "pressing"));

    public PainBeginning() {
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public byte[] getLocation_teeth() {
        byte[] bytes = Base64.decode(location_teeth, Base64.NO_WRAP);
        return bytes;
    }

    public void setLocation_teeth(byte[] location_teeth) {
        String string = Base64.encodeToString(location_teeth, Base64.NO_WRAP);
        this.location_teeth = string;
    }

    public byte[] getLocation_face_left() {
        byte[] bytes = Base64.decode(location_face_left, Base64.NO_WRAP);
        return bytes;
    }

    public void setLocation_face_left(byte[] location_face_left) {
        String string = Base64.encodeToString(location_face_left, Base64.NO_WRAP);
        this.location_face_left = string;
    }

    public byte[] getLocation_face_right() {
        byte[] bytes = Base64.decode(location_face_right, Base64.NO_WRAP);
        return bytes;
    }

    public void setLocation_face_right(byte[] location_face_right) {
        String string = Base64.encodeToString(location_face_right, Base64.NO_WRAP);
        this.location_face_right = string;
    }

    public String getPain_pattern() {
        return pain_pattern;
    }

    public void setPain_pattern(String pain_pattern) {
        this.pain_pattern = pain_pattern;
    }

    public boolean isDull() {
        return dull;
    }

    public void setDull(boolean dull) {
        this.dull = dull;
    }

    public boolean isPulling() {
        return pulling;
    }

    public void setPulling(boolean pulling) {
        this.pulling = pulling;
    }

    public boolean isStinging() {
        return stinging;
    }

    public void setStinging(boolean stinging) {
        this.stinging = stinging;
    }

    public boolean isPulsating() {
        return pulsating;
    }

    public void setPulsating(boolean pulsating) {
        this.pulsating = pulsating;
    }

    public boolean isBurning() {
        return burning;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    public boolean isPinsneedles() {
        return pinsneedles;
    }

    public void setPinsneedles(boolean pinsneedles) {
        this.pinsneedles = pinsneedles;
    }

    public boolean isTingling() {
        return tingling;
    }

    public void setTingling(boolean tingling) {
        this.tingling = tingling;
    }

    public boolean isNumb() {
        return numb;
    }

    public void setNumb(boolean numb) {
        this.numb = numb;
    }

    public boolean isElectric() {
        return electric;
    }

    public void setElectric(boolean electric) {
        this.electric = electric;
    }

    public boolean isPressing() {
        return pressing;
    }

    public void setPressing(boolean pressing) {
        this.pressing = pressing;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPhoto(Bitmap bmp){
        Photography = new ProxyBitmap(bmp);
    }

    public Bitmap getPhoto(){
        return Photography.getBitmap();
    }

    public boolean existsPhoto(){
        return Photography!=null;
    }

    public void setPainCoordinates(Float x, Float y, Float z, Float t, String painType){
        //Stores the x, y, z and t coordinates of a pain type in the hashmap.
        ArrayList<Float> templist = new ArrayList<>(Arrays.asList((float) x, y, z, t));
        values.put(painType, templist);
    }

    public void deletePainCoordinates(String painType){
        values.remove(painType);
    }

    public ArrayList getPainCoordinates(String painType){
        //returns [-1.0, -1.0, -1.0, -1.0] if painType does not exist in the hashmap.
        ArrayList<Float> templist = new ArrayList<>(Arrays.asList((float) -1.0f, -1.0f, -1.0f, -1.0f));
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

    public ArrayList getPainList(){return pain_list;}


}