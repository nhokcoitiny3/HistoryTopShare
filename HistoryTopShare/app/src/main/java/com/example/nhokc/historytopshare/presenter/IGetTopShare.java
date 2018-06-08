package com.example.nhokc.historytopshare.presenter;

import com.example.nhokc.historytopshare.model.BaseTotalResponse;
import com.example.nhokc.historytopshare.model.BaseVideoResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface IGetTopShare {
    @GET("/getTopShare")
    Call<BaseTotalResponse> getTopShare(
            @QueryMap Map<String, String> options
    );
}
