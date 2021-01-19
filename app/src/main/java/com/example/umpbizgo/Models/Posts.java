package com.example.umpbizgo.Models;

public class Posts {
    private String poid;
    private String date, time, description, image, title;

    public Posts() {
    }

    public Posts(String poid, String date, String time, String description, String image, String title) {
        this.poid = poid;
        this.date = date;
        this.time = time;
        this.description = description;
        this.image = image;
        this.title = title;
    }

    public String getPoid() {
        return poid;
    }

    public void setPoid(String poid) {
        this.poid = poid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
