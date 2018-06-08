package com.example.nhokc.historytopshare.view;

import android.support.v4.app.Fragment;
import android.view.View;

import com.example.nhokc.historytopshare.presenter.IOnRecyclerViewItemClickListener;

public abstract class BaseFragment extends Fragment implements IOnRecyclerViewItemClickListener, View.OnClickListener {
    public abstract void initialize();

}
