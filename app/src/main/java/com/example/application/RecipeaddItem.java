package com.example.application;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeaddItem implements Serializable {




    @SerializedName("res_State")
    private String res_State;

    @SerializedName("res_Msg")
    private String res_Msg;

    public String getRes_State() {
        return res_State;
    }

    public void setRes_State(String res_State) {
        this.res_State = res_State;
    }

    public String getRes_Msg() {
        return res_Msg;
    }

    public void setRes_Msg(String res_Msg) {
        this.res_Msg = res_Msg;
    }

}