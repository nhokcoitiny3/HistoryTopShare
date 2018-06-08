package com.example.nhokc.historytopshare.model;

public class Review {
    private Long dt;
    private Long long_time;
    private int view;

    public Review(Long dt, Long long_time, int view) {
        this.dt = dt;
        this.long_time = long_time;
        this.view = view;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public Long getLong_time() {
        return long_time;
    }

    public void setLong_time(Long long_time) {
        this.long_time = long_time;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
