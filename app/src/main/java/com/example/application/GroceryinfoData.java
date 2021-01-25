package com.example.application;

public class GroceryinfoData {
    int idx;
    String name;
    int expiration;
    String season;
    String tipPurchase;
    String tipCook;
    String tipStorage;

    public int getIdx(){
        return idx;
    }
    public void setIdx(){this.idx=idx;}

    public String getname(){
        return name;
    }
    public void setname(){
        this.name=name;
    }

    public int getExpiration(){
        return expiration;
    }
    public void setExpiration(){this.expiration=expiration;}

    public String getSeason(){
        return season;
    }
    public void setSeason(){
        this.season=season;
    }

    public String getTipPurchase(){
        return tipPurchase;
    }
    public void setTipPurchase(){
        this.tipPurchase=tipPurchase;
    }

    public String getTipCook(){
        return tipCook;
    }
    public void setTipCook(){
        this.tipCook=tipCook;
    }

    public String getTipStorage(){
        return tipStorage;
    }
    public void setTipStorage(){
        this.tipStorage=tipStorage;
    }



    public GroceryinfoData(int idx,String name, int expiration,String season,String tipPurchase,String tipCook,String tipStorage){
        this.idx=idx;
        this.name=name;
        this.expiration=expiration;
        this.season=season;
        this.tipPurchase=tipPurchase;
        this.tipCook=tipCook;
        this.tipStorage=tipStorage;
    }
}
