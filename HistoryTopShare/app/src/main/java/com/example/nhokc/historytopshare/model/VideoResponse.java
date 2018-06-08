package com.example.nhokc.historytopshare.model;


import java.util.List;


public class VideoResponse {

    private String videoid;
    private String title;
    private long long_time;

    private long average_time;
    private int total_view;
    private List<Review> reviews;

    public VideoResponse(String videoid, String title, long long_time, long average_time, int total_view, List<Review> reviews) {
        this.videoid = videoid;
        this.title = title;
        this.long_time = long_time;
        this.average_time = average_time;
        this.total_view = total_view;
        this.reviews = reviews;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLong_time() {
        return long_time;
    }

    public void setLong_time(long long_time) {
        this.long_time = long_time;
    }

    public long getAverage_time() {
        return average_time;
    }

    public void setAverage_time(long average_time) {
        this.average_time = average_time;
    }

    public int getTotal_view() {
        return total_view;
    }

    public void setTotal_view(int total_view) {
        this.total_view = total_view;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
