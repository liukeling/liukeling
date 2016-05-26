package com.example.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.MyViews.RefreshableView;
import com.example.copyqq.R;

/**
 * Created by Administrator on 2016/4/23.
 */
public class Frindlist_fragmnet extends Fragment {

    RefreshableView refreshable_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getContext(), R.layout.frindlistfragment_layout, null);

        refreshable_view = (RefreshableView) v.findViewById(R.id.refreshable_view);


        return v;
    }
}
