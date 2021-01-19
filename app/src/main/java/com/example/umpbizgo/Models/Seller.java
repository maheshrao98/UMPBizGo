package com.example.umpbizgo.Models;

public class Seller {
    public String name, businessName, email, password, phone, address,logo;

    public Seller() {
    }

    public Seller(String password, String phone, String address, String logo) {
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.logo = logo;
    }

    public Seller(String name, String businessName, String email) {
        this.name = name;
        this.businessName = businessName;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
