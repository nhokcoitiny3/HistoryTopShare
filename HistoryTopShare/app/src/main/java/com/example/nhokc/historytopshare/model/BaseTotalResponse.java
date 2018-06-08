package com.example.nhokc.historytopshare.model;

import java.util.List;

public class BaseTotalResponse {

    private int statusCode;
    private String msg;
    private TopShareResponse data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TopShareResponse getData() {
        return data;
    }

    public void setDataList(TopShareResponse data) {
        this.data = data;
    }
}
