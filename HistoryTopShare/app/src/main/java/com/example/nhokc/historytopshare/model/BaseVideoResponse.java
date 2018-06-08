package com.example.nhokc.historytopshare.model;

import java.util.List;

public class BaseVideoResponse {

    private int statusCode;
    private String msg;
    private List<VideoResponse> data;

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

    public List<VideoResponse> getDataList() {
        return data;
    }

    public void setDataList(List<VideoResponse> dataList) {
        this.data = dataList;
    }
}
