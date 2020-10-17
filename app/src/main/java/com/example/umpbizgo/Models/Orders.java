package com.example.umpbizgo.Models;

public class Orders {
    private String cityaddress, date, homeaddress, name, phonenumber, state, time, totalAmount, oid;

    public Orders() {
    }

    public Orders(String cityaddress, String date, String homeaddress, String name, String phonenumber, String state, String time, String totalAmount, String oid) {
        this.cityaddress = cityaddress;
        this.date = date;
        this.homeaddress = homeaddress;
        this.name = name;
        this.phonenumber = phonenumber;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
        this.oid = oid;

    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCityaddress() {
        return cityaddress;
    }

    public void setCityaddress(String cityaddress) {
        this.cityaddress = cityaddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomeaddress() {
        return homeaddress;
    }

    public void setHomeaddress(String homeaddress) {
        this.homeaddress = homeaddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

}


