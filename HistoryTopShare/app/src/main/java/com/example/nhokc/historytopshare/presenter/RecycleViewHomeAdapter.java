package com.example.nhokc.historytopshare.presenter;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nhokc.historytopshare.R;
import com.example.nhokc.historytopshare.databinding.ItemRcvHomeBinding;
import com.example.nhokc.historytopshare.model.VideoResponse;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewHomeAdapter extends Adapter<RecycleViewHomeAdapter.RecycleViewHolder> {
    private IOnRecyclerViewItemClickListener listener;
    private ItemRcvHomeBinding binding;
    private IVideoresponse iVideoresponse;
    public void setIdata(IVideoresponse iVideoresponse) {
        this.iVideoresponse = iVideoresponse;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_rcv_home,parent,false);
        return new RecycleViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(RecycleViewHolder holder, final int position) {
        VideoResponse videoResponse = iVideoresponse.getVideoResponse(position);
        holder.binding.txtName.setText(videoResponse.getTitle());

        holder.binding.txtTotalTime.setText(Integer.toString((int)videoResponse.getLong_time()));
        holder.binding.txtAvgTime.setText(Integer.toString((int) videoResponse.getAverage_time()));
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E9e9e9"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecyclerViewItemClicked(position, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iVideoresponse.getCount();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        public ItemRcvHomeBinding binding;
        public RecycleViewHolder(ItemRcvHomeBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }
    public void setOnItemClickListener(IOnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public interface IVideoresponse{
        int getCount();
        VideoResponse  getVideoResponse(int position);
    }
}
