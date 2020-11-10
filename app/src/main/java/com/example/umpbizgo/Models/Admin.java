package com.example.umpbizgo.Models;

public class Admin {
    private String userID, email, password, username ;

    public Admin () {

    }

    public Admin(String userID, String username, String email, String password)
    {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
