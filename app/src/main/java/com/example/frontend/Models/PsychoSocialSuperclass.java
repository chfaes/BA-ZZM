package com.example.frontend.Models;

import android.util.Base64;
import android.util.Log;
import android.widget.Button;

import com.example.frontend.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class PsychoSocialSuperclass implements Serializable {
    private Map<String, ArrayList<Integer>> values = new HashMap<>();
    private Map<String, String> texts = new HashMap<>();
    private String values_encoded;
    private String texts_encoded;

    public void setValues(String tag, int x, int y, int size, int colour){
        //Notice the structure: Indices 0 and 1 for coordinates, 2 for size, 3 for colour.
        ArrayList<Integer> templist = new ArrayList<>(Arrays.asList(x, y, size, colour));
        values.put(tag, templist);
        encoding(0);
    }

    public void setText(String tag, String text){
        texts.put(tag, text);
        encoding(1);
    }

    public String getText(String tag){
        decoding(1);
        return texts.get(tag).toString();
    }

    public void setCoordinates(String tag, int x, int y){
        //Smaller function used after a button has been moved. Sets x, y for a certain tag.
        decoding(0);
        ArrayList<Integer> templist = values.get(tag);
        templist.set(0, x);
        templist.set(1, y);
        values.put(tag, templist);
        encoding(0);
    }

    public void flipColour(String tag){
        //Turns color 1 (red) to 0 (green); turns 0 to 1.
        decoding(0);
        ArrayList<Integer> templist = values.get(tag);
        if (templist.get(3) == 0){
            templist.set(3, 1);
        } else {
            templist.set(3, 0);
        }
        values.put(tag, templist);
        encoding(0);
    }

    public int getByTagAndIndex(String tag, int idx){
        //0 for x, 1 for y, 2 for size and 3 for colour.
        decoding(0);
        return (int) values.get(tag).get(idx);
    }

    public void setSize(String tag, int size){
        //Index 2: Here, the size is stored.
        decoding(0);
        ArrayList<Integer> templist = values.get(tag);
        templist.set(2, size);
        values.put(tag, templist);
        encoding(0);
    }

    public int getNextTag(){
        //Searches values for the highest tag; returns tag+1.
        decoding(0);
        int tag = 0;
        Iterator it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int current_value = Integer.parseInt(pair.getKey().toString());
            if(current_value >= tag) {
                tag = current_value;
                tag++;
            }
        }
        return tag;
    }

    public Map getValues(){
        decoding(0);
        return values;
    }

    public String getValues_encoded(){
        return values_encoded;
    }

    public void setValues_encoded(String str){
        values_encoded = str;
    }

    public String getTexts_encoded(){
        return texts_encoded;
    }

    public void setTexts_encoded(String str){
        texts_encoded = str;
    }

    private void encoding(int type){
        //type = 0: writes "values" to "values_encoded".
        //type = 1: writes "texts" to "texts_encoded".
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            if(type == 0){
                so.writeObject(values);
            }else{
                so.writeObject(texts);
            }
            so.flush();
            if (type == 0){
                values_encoded = Base64.encodeToString(bo.toByteArray(), Base64.NO_WRAP);
            } else {
                texts_encoded = Base64.encodeToString(bo.toByteArray(), Base64.NO_WRAP);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void decoding(int type){
        //type = 0: updates "values" from "values_encoded"
        //type = 1: updates "texts" from "texts_encoded"
        try {
            String temp_string = "";
            if (type == 0){
                temp_string = values_encoded;
            } else {
                temp_string = texts_encoded;
            }
            byte[] b = Base64.decode(temp_string, Base64.NO_WRAP);

            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            Map obj = (Map) si.readObject();

            if (type == 0){
                values = obj;
            } else {
                texts = obj;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
