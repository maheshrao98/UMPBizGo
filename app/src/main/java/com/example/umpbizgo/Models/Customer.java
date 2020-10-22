package com.example.umpbizgo.Models;

public class Customer {
    public String username, email, password, phone, image, homeaddress, cityaddress;

    public Customer() {
    }

    public Customer(String username, String email, String password, String phone, String image, String homeaddress, String cityaddress )
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.homeaddress = homeaddress;
        this.cityaddress = cityaddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHomeaddress() {
        return homeaddress;
    }

    public void setHomeaddress(String homeaddress) {
        this.homeaddress = homeaddress;
    }

    public String getCityaddress() {
        return cityaddress;
    }

    public void setCityaddress(String cityaddress) {
        this.cityaddress = cityaddress;
    }
}