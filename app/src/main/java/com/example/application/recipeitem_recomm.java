package com.example.application;

public class recipeitem_recomm {
    String idx;
    String title;
    String imgURL;

    public recipeitem_recomm(){}

    public recipeitem_recomm(String idx, String title, String imgURL){
        this.idx=idx;
        this.title=title;
        this.imgURL=imgURL;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

}
