package com.example.application;

public class carttorefriitem {

    String ingre_idx;
    String ingre_name;

    public carttorefriitem(){
    }
    public carttorefriitem(String ingre_idx,String ingre_name){
        this.ingre_idx=ingre_idx;
        this.ingre_name=ingre_name;
    }

    public String getIngre_idx() {
        return ingre_idx;
    }

    public void setIngre_idx(String ingre_idx) {
        this.ingre_idx = ingre_idx;
    }

    public String getIngre_name() {
        return ingre_name;
    }

    public void setIngre_name(String ingre_name) {
        this.ingre_name = ingre_name;
    }
}
