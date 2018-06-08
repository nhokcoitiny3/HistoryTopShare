package com.example.nhokc.historytopshare.view;

import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nhokc.historytopshare.databinding.ActivityVideoChartBinding;
import com.example.nhokc.historytopshare.model.BaseVideoResponse;
import com.example.nhokc.historytopshare.model.Review;
import com.example.nhokc.historytopshare.model.VideoResponse;
import com.example.nhokc.historytopshare.presenter.IVideoh;
import com.example.nhokc.historytopshare.R;
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


public class VideoChartActivity extends AppCompatActivity implements OnChartGestureListener, View.OnClickListener {

    private static final String TAG = "";

    private String[] arrChoose;
    private ArrayAdapter<String> arrayAdapterSpinner;
    private String videoId;
    private String videoName;
    private String times;
    private List<String> time;
    private List<String> duration;
    private ActivityVideoChartBinding binding;
    private VideoResponse videoResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_chart);
        initialize();
    }

    public void initialize() {
        videoId = getIntent().getStringExtra("VIDEO_ID");
        getData(videoId, "48");

        binding.txtNameVideo.setText(videoName);

        binding.btnBack.setOnClickListener(this);

        arrChoose = new String[]{
                "48 giờ trước",
                "7 ngày qua( 1 tuần)",
                "28 ngày qua( 4 tuần)"
        };

        arrayAdapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrChoose);
        arrayAdapterSpinner.setDropDownViewResource(R.layout.item_checked);
        binding.spinnerChoose.setAdapter(arrayAdapterSpinner);
        binding.spinnerChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        times = "48";
                        getData(videoId, times);
                        break;

                    case 1:
                        times = "168";
                        getData(videoId, times);
                        break;

                    case 2:
                        times = "672";
                        getData(videoId, times);
                        break;

                    default:
                        times = "48";
                        getData(videoId, times);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void getData(String videoId, final String times) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.103:1999/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        IVideoh iVideoh = retrofit.create(IVideoh.class);
        Map<String, String> data = new HashMap<>();
        data.put("videoid", videoId);
        data.put("time", times);
        iVideoh.getVideo(data).enqueue(new Callback<BaseVideoResponse>()

        {
            @Override
            public void onResponse(Call<BaseVideoResponse> call, Response<BaseVideoResponse> response) {
                BaseVideoResponse baseResponse = response.body();
                videoResponse = (VideoResponse) baseResponse.getDataList().get(0);

                duration = new ArrayList<>();
                time = new ArrayList<>();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
                format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

                for (Review review : videoResponse.getReviews()) {
                    String formatted = format.format(review.getLong_time());
                    time.add(formatted);

                    duration.add(String.valueOf(review.getLong_time() / 1000));

                }
                Log.d(TAG, "onResponse: " + time);
                initializeChart();
            }

            @Override
            public void onFailure(Call<BaseVideoResponse> call, Throwable t) {
            }
        });

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
                } else if (value == duration.size()) {
                    return "Bây giờ";
                } else {
                    return "";
                }
            }

        };

        XAxis xAxis = binding.chartVideo.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setAxisMinimum(-1f);
        xAxis.setAxisMaximum(duration.size() + 5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(formatter);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = binding.chartVideo.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);

        YAxis rightAxis = binding.chartVideo.getAxisRight();
        rightAxis.setEnabled(false);

        LineData data = new LineData(dataset);
        data.setDrawValues(false);
        binding.chartVideo.setData(data);
        Description description = new Description();
        description.setText("");
        description.setTextAlign(Paint.Align.CENTER);
        binding.chartVideo.setDescription(description);
        binding.chartVideo.animateX(1000);
        binding.chartVideo.animateY(1000, Easing.EasingOption.EaseInBack);
        binding.chartVideo.setOnChartGestureListener(this);
        binding.chartVideo.setDrawMarkers(false);
        Legend legend = binding.chartVideo.getLegend();
        legend.setEnabled(false);
        binding.chartVideo.setBorderWidth(10);
        binding.chartVideo.getXAxis().setDrawGridLines(false);
        binding.chartVideo.invalidate();
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
    public void onClick(View v) {
        this.finish();
    }

}
