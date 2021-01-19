package com.example.umpbizgo.Models;

public class ProductRatings {
    private String username, rate;

    public ProductRatings() {
    }

    public ProductRatings(String username, String rate) {
        this.username = username;
        this.rate = rate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
