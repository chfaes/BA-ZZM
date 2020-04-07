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

    public void setValues(Button btn, int x, int y, int size, int colour){
        ArrayList<Integer> templist = new ArrayList<>(Arrays.asList(x, y, size, colour));
        values.put(String.valueOf(btn.getTag()), templist);
        texts.put(String.valueOf(btn.getTag()), btn.getText().toString());
        encoding();

    }

    public int getNextTag(){
        //Searches values for the highest tag; returns tag+1.
        decoding();
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
        decoding();
        return values;
    }

    public String getValues_encoded(){
        return values_encoded;
    }

    public void setValues_encoded(String str){
        values_encoded = str;
    }

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
