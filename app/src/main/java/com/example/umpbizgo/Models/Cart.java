package com.example.umpbizgo.Models;

public class Cart {private String pid, productname, price, quantity, discount , image;

    public Cart() {

    }

    public Cart(String pid, String productname, String price, String quantity, String discount, String image) {
        this.pid = pid;
        this.productname = productname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
