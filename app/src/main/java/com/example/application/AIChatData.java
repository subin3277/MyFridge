package com.example.application;

public class AIChatData {

    private String msg;
    private String response;

    public AIChatData(){}

    public AIChatData(String msg,String response){
        this.msg=msg;
        this.response=response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
