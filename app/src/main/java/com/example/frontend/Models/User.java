package com.example.frontend.Models;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;

    public User()  {
    }

    public User(String username, String password) {
        this.username= username;
        this.password = password;
    }
    public User(int userId, String username, String password) {
        this.id = userId;
        this.username= username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
