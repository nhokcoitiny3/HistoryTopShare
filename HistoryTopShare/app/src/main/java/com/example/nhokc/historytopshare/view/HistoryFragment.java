package com.example.nhokc.historytopshare.view;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nhokc.historytopshare.databinding.FragmentHistoryBinding;
import com.example.nhokc.historytopshare.model.BaseVideoResponse;
import com.example.nhokc.historytopshare.model.Review;
import com.example.nhokc.historytopshare.model.VideoResponse;
import com.example.nhokc.historytopshare.presenter.IVideoP;
import com.example.nhokc.historytopshare.R;
import com.example.nhokc.historytopshare.presenter.RecycleViewHistoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryFragment extends BaseFragment implements RecycleViewHistoryAdapter.IVideoresponse {
    private String[] arrChoose;
    private ArrayAdapter<String> arrayAdapterSpinner;
    private RecycleViewHistoryAdapter recycleViewHistoryAdapter;
    private List<VideoResponse> videoList = new ArrayList<>();
    private VideoResponse videoResponse;
    private int page = 1;
    private int time = 48;
    private FragmentHistoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        getData(page, time);
        initialize();
        return binding.getRoot();
    }

    public void initialize() {
        arrChoose = new String[]{
                "48 giờ trước",
                "7 ngày qua( 1 tuần)",
                "28 ngày qua( 4 tuần)"
        };

        arrayAdapterSpinner = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, arrChoose);
        arrayAdapterSpinner.setDropDownViewResource(R.layout.item_checked);
        binding.spinnerChoose.setAdapter(arrayAdapterSpinner);
        binding.spinnerChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        time = 48;
                        getData(page, time);
                        break;
                    case 1:
                        time = 168;
                        getData(page, time);
                        break;
                    case 2:
                        time = 672;
                        getData(page, time);
                        break;
                    default:
                        time = 48;
                        getData(page, time);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (binding.btnMore.getVisibility() == View.INVISIBLE) {
            binding.btnMore.setVisibility(View.VISIBLE);
        }
        binding.btnMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_more:
                page++;
                getData(page, time);
                break;
        }
    }

    public void initializeRCV() {
        recycleViewHistoryAdapter = new RecycleViewHistoryAdapter();
        recycleViewHistoryAdapter.setIdata(this);
        binding.rcvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvHistory.setAdapter(recycleViewHistoryAdapter);
    }

    @Override
    public void onRecyclerViewItemClicked(int position, int id) {

    }

    public void getData(int page, final int time) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:1999/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> data = new HashMap<>();
        data.put("page", String.valueOf(page));
        IVideoP iVideoP = retrofit.create(IVideoP.class);
        iVideoP.getVideo(data).enqueue(new Callback<BaseVideoResponse>() {
            @Override
            public void onResponse(Call<BaseVideoResponse> call, Response<BaseVideoResponse> response) {

                BaseVideoResponse baseResponse = response.body();
                videoList = new ArrayList<>();
                videoList = baseResponse.getDataList();


                for (VideoResponse videoResponse : videoList) {
                    int longTime = 0;
                    int view = 0;
                    List<Review> reviews = videoResponse.getReviews();
                    if (reviews.size() <= time) {
                        for (int j = 0; j < reviews.size(); j++) {
                            longTime += reviews.get(j).getLong_time();
                            view += reviews.get(j).getView();
                        }
                    } else {
                        for (int j = 0; j < time; j++) {
                            longTime += reviews.get(j).getLong_time();
                            view += reviews.get(j).getView();
                        }

                    }
                    videoResponse.setLong_time(longTime);
                    videoResponse.setTotal_view(view);
                }
                initializeRCV();
            }

            @Override
            public void onFailure(Call<BaseVideoResponse> call, Throwable t) {
            }
        });


    }


    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public VideoResponse getVideoResponse(int position) {
        return videoList.get(position);
    }
}
