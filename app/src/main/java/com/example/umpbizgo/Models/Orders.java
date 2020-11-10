package com.example.umpbizgo.Models;

public class Orders {
    private String cityaddress, date, homeaddress, name, phonenumber, state, time, totalAmount, oid, pid, productname, price, quantity, discount, productImage, sellerbusinessname, trackingno;

    public Orders() {
    }

    public Orders(String cityaddress, String date, String homeaddress, String name, String phonenumber, String state, String time, String totalAmount, String oid, String pid, String productname, String price, String quantity, String discount, String productImage, String sellerbusinessname, String trackingno) {
        this.cityaddress = cityaddress;
        this.date = date;
        this.homeaddress = homeaddress;
        this.name = name;
        this.phonenumber = phonenumber;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
        this.oid = oid;
        this.pid = pid;
        this.productname = productname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.productImage = productImage;
        this.sellerbusinessname = sellerbusinessname;
        this.trackingno = trackingno;
    }

    public String getTrackingno() {
        return trackingno;
    }

    public void setTrackingno(String trackingno) {
        this.trackingno = trackingno;
    }

    public String getSellerbusinessname() {
        return sellerbusinessname;
    }

    public void setSellerbusinessname(String sellerbusinessname) {
        this.sellerbusinessname = sellerbusinessname;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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


