package com.example.application;

public class RecipeCookItem {
    String Idx;
    String des;
    String imageurl;

    public RecipeCookItem(String Idx,String des,String imageurl){
        this.Idx=Idx;
        this.des=des;
        this.imageurl=imageurl;

    }
    public String getIdx() {
        return Idx;
    }

    public void setIdx(String idx) {
        Idx = idx;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
