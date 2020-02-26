package com.example.frontend.Models;

import java.io.Serializable;

public class DiagnosisType implements Serializable {

    private int id;
    private String name;
    private String type;
    private String description;

    public DiagnosisType() {
    }

    public DiagnosisType(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public DiagnosisType(int id, String name, String type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
