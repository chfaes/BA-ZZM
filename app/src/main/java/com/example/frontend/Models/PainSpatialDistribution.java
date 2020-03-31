package com.example.frontend.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PainSpatialDistribution implements Serializable {
    //This class is an Attribute in PainBeginning and PainCurrent.

    private Map<String, ArrayList<Float>> Values = new HashMap();

    public void setPain(Float x, Float y, Float z, String painType){
        //Stores the x, y and z coordinates of a pain type in the hashmap.
        ArrayList<Float> templist = new ArrayList<>(Arrays.asList(x, y, z));
        Values.put(painType, templist);
    }

    public ArrayList getCoordinates(String painType){
        //returns [-1.0, -1.0, -1.0] if painType does not exist in the hashmap.
        ArrayList<Float> templist = new ArrayList(Arrays.asList(-1.0, -1.0, -1.0));
        if (Values.get(painType) != null ){
            templist.clear();
            templist = Values.get(painType);
        }
        return templist;
    }

    public boolean painIsSet(String painType){
        //checks if painType is part of the hashmap
        return Values.get(painType) != null;
    }

}
