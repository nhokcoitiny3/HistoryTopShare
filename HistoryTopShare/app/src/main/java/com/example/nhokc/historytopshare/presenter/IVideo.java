package com.example.nhokc.historytopshare.presenter;

import com.example.nhokc.historytopshare.model.BaseVideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IVideo {
    @GET("/videos")
    Call<BaseVideoResponse> getVideo();

}

