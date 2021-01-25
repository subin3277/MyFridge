package com.example.application;

public class cartitem {
    String image;
    String link;
    String name;
    String price;

    public cartitem(){}

    public cartitem(String image,String link,String name,String price){
        this.image=image;
        this.link=link;
        this.name=name;
        this.price=price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }





}
