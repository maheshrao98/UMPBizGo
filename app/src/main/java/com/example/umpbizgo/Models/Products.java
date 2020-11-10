package com.example.umpbizgo.Models;

public class Products
{
    private String productname;
    private String description;
    private String price;
    private String image;
    private String category;
    private String pid;
    private String date;
    private String time;
    private String imagestorageurl;
    private String sellerbusinessname;
    private String sellerid;

    public Products()
    {

    }


    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public Products(String productname, String description, String price, String image, String category, String pid, String date, String time, String sellerbusinessname, String sellerid, String imagestorageurl) {
        this.productname = productname;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.sellerbusinessname = sellerbusinessname;
        this.sellerid = sellerid;
        this.imagestorageurl = imagestorageurl;
    }

    public String getImagestorageurl() {
        return imagestorageurl;
    }

    public void setImagestorageurl(String imagestorageurl) {
        this.imagestorageurl = imagestorageurl;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getsellerbusinessname() {
        return sellerbusinessname;
    }

    public void setsellerbusinessname(String sellerbusinessname) {
        this.sellerbusinessname = sellerbusinessname;
    }
}