package com.example.application;

public class GrocerylistData {
    int idx;
    String name;

    GrocerylistData(){}

    GrocerylistData(int idx,String name){
        this.idx = idx;
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}
