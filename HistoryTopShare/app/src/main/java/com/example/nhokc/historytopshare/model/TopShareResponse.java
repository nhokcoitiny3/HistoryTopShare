package com.example.nhokc.historytopshare.model;

import java.util.List;

public class TopShareResponse {
    private int total_view;
    private List<Review> reviews;

    public TopShareResponse(int total_view, List<Review> reviews) {
        this.total_view = total_view;
        this.reviews = reviews;
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
