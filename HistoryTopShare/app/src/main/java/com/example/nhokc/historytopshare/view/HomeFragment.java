package com.example.nhokc.historytopshare.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.MergedDataBinderMapper;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nhokc.historytopshare.databinding.FragmentHomeBinding;
import com.example.nhokc.historytopshare.model.BaseTotalResponse;
import com.example.nhokc.historytopshare.model.BaseVideoResponse;
import com.example.nhokc.historytopshare.model.Review;
import com.example.nhokc.historytopshare.model.TopShareResponse;
import com.example.nhokc.historytopshare.model.VideoResponse;
import com.example.nhokc.historytopshare.presenter.IGetTopShare;
import com.example.nhokc.historytopshare.presenter.IVideoP;
import com.example.nhokc.historytopshare.R;
import com.example.nhokc.historytopshare.presenter.RecycleViewHomeAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends BaseFragment implements OnChartGestureListener, RecycleViewHomeAdapter.IVideoresponse {

    private String[] arrChoose;
    private ArrayAdapter<String> arrayAdapterSpinner;
    private RecycleViewHomeAdapter recycleViewHomAdapter;
    private List<VideoResponse> videoList = new ArrayList<>();
    private TopShareResponse topShareResponses;
    private int page = 1;
    private List<String> duration = new ArrayList<>();
    private List<String> time = new ArrayList<>();
    private String times = "48";
    private FragmentHomeBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initialize();
        getData(page, times);
        getDataAll(times);
        return binding.getRoot();
    }

    @SuppressLint("ResourceType")
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
                        times = "48";
                        getDataAll(times);
                        getData(page, times);
                        break;
                    case 1:
                        times = "168";
                        getDataAll(times);
                        getData(page, times);
                        break;
                    case 2:
                        times = "672";
                        getDataAll(times);
                        getData(page, times);
                        break;
                    default:
                        times = "48";
                        getDataAll(times);
                        getData(page, times);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnMore.setOnClickListener(this);

    }

    public void initializeRCV() {
        recycleViewHomAdapter = new RecycleViewHomeAdapter();
        recycleViewHomAdapter.setIdata(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(false);
        binding.rcvHome.setLayoutManager(linearLayoutManager);
        binding.rcvHome.setAdapter(recycleViewHomAdapter);
        recycleViewHomAdapter.setOnItemClickListener(this);

    }

    public void initializeChart() {


        ArrayList<Entry> entriesAvg = new ArrayList<>();
        for (int i = 0; i < duration.size(); i++) {
            entriesAvg.add(new Entry(i, Float.parseFloat(duration.get(i))));
        }
        LineDataSet dataset = new LineDataSet(entriesAvg, "");
        dataset.setLineWidth(2);
        dataset.setDrawCircles(false);
        dataset.setCircleColor(R.color.colorPrimary);
        final String[] quarters = new String[time.size()];

        for (int i = 0; i < time.size(); i++) {
            quarters[i] = time.get(i);
        }
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value == 0) {
                    if (times == "48") {
                        return "48 giờ trước";
                    } else if (times == "168") {
                        return "7 ngày trước";
                    } else {
                        return "28 ngày trước";
                    }
                } else if (value >= duration.size() - 5) {
                    return "Bây giờ";
                } else {
                    return "";
                }
            }

        };
        XAxis xAxis = binding.lineChart.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(quarters.length - 0.5f);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(formatter);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = binding.lineChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);

        YAxis rightAxis = binding.lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        LineData data = new LineData(dataset);
        dataset.setDrawCircles(false);
        data.setDrawValues(false);
        binding.lineChart.setData(data);
        Description description = new Description();
        description.setText("");
        description.setTextAlign(Paint.Align.CENTER);
        binding.lineChart.setDescription(description);
        binding.lineChart.animateX(1000);
        binding.lineChart.animateY(1000, Easing.EasingOption.EaseInBack);
        binding.lineChart.setOnChartGestureListener(this);
        binding.lineChart.setDrawMarkers(false);
        Legend legend = binding.lineChart.getLegend();
        legend.setEnabled(false);
        binding.lineChart.setDrawMarkers(false);
        binding.lineChart.getXAxis().setDrawGridLines(false);
        binding.lineChart.invalidate();

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onRecyclerViewItemClicked(int position, int id) {

        Intent intent = new Intent(getActivity(), VideoChartActivity.class);
        intent.putExtra("VIDEO_ID", videoList.get(position).getVideoid());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_more:
                page++;
                getData(page, times);
                break;
        }
    }

    public void getData(int page, final String times) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:1999/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> data = new HashMap<>();
        data.put("page", String.valueOf(page));
        //data.put("time", times);
        IVideoP iVideoP = retrofit.create(IVideoP.class);
        iVideoP.getVideo(data).enqueue(new Callback<BaseVideoResponse>() {
            @Override
            public void onResponse(Call<BaseVideoResponse> call, Response<BaseVideoResponse> response) {

                BaseVideoResponse baseResponse = response.body();
                videoList = baseResponse.getDataList();

                for (int i = 0; i < videoList.size(); i++) {
                    int longTime = 0;
                    int avgTime = 0;
                    if (videoList.get(i).getReviews().size() <= Integer.parseInt(times)) {
                        for (int j = 0; j < videoList.get(i).getReviews().size(); j++) {
                            longTime += videoList.get(i).getReviews().get(j).getLong_time();
                        }
                    } else {
                        for (int j = 0; j < Integer.parseInt(times); j++) {
                            longTime += videoList.get(i).getReviews().get(j).getLong_time();
                        }
                    }
                    videoList.get(i).setLong_time(longTime);
                    avgTime = longTime / videoList.get(i).getTotal_view();
                    videoList.get(i).setAverage_time(avgTime);
                }
                initializeRCV();
            }

            @Override
            public void onFailure(Call<BaseVideoResponse> call, Throwable t) {
            }
        });

    }

    public void getDataAll(String times) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:1999/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> data = new HashMap<>();
        data.put("time", times);
        IGetTopShare iGetTopShare = retrofit.create(IGetTopShare.class);
        iGetTopShare.getTopShare(data).enqueue(new Callback<BaseTotalResponse>() {
            @Override
            public void onResponse(Call<BaseTotalResponse> call, Response<BaseTotalResponse> response) {
                BaseTotalResponse baseTotalResponse = response.body();
                topShareResponses = baseTotalResponse.getData();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
                format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                for (Review review : topShareResponses.getReviews()) {
                    duration.add(String.valueOf(review.getLong_time() / 1000));
                    String formatted = format.format(review.getLong_time());
                    time.add(formatted);

                }
                initializeChart();
                Log.d("", "onResponse: ");
            }

            @Override
            public void onFailure(Call<BaseTotalResponse> call, Throwable t) {
                Log.d("", "onFailure: ");
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
