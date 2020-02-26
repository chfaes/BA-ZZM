package com.example.frontend.Models;

public class WebsiteType {

    private int id;
    private String url;
    private String name;
    private String description;

    public WebsiteType() {
    }

    public WebsiteType(String url, String name, String description) {
        this.url = url;
        this.name = name;
        this.description = description;
    }

    public WebsiteType(int id, String url, String name, String description) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
