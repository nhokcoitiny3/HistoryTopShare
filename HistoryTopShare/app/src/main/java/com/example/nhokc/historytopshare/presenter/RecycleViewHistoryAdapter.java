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
import com.example.nhokc.historytopshare.databinding.ItemRcvHistoryBinding;
import com.example.nhokc.historytopshare.model.VideoResponse;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewHistoryAdapter extends Adapter<RecycleViewHistoryAdapter.RecycleViewHolder> {
    private IOnRecyclerViewItemClickListener listener;
    private ItemRcvHistoryBinding binding;
    private IVideoresponse iVideoresponse;
    public void setIdata(IVideoresponse iVideoresponse) {
        this.iVideoresponse = iVideoresponse;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_rcv_history,parent,false);
        return new RecycleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, final int position) {
        VideoResponse videoResponse = iVideoresponse.getVideoResponse(position);
        holder.binding.txtName.setText(videoResponse.getTitle());
        holder.binding.txtNumberTime.setText(Integer.toString((int) videoResponse.getLong_time()));
        holder.binding.txtNumberPlayback.setText(Integer.toString(videoResponse.getTotal_view()));
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#e9e9e9"));
        }

    }

    @Override
    public int getItemCount() {
        return this.iVideoresponse.getCount();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        public ItemRcvHistoryBinding binding;
        public VideoResponse videoResponse;

        public RecycleViewHolder(ItemRcvHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }
    public interface IVideoresponse{
        int getCount();
        VideoResponse  getVideoResponse(int position);
    }
    public void setOnItemClickListener(IOnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }
}
