package com.example.frontend.Models;

import java.io.Serializable;

public class DrugType implements Serializable {

    private int id;
    private String name;
    private String description;

    public DrugType() {
    }

    public DrugType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public DrugType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
