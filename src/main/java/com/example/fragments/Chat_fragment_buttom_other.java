package com.example.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.copyqq.R;

public class Chat_fragment_buttom_other extends Fragment {


    private View main_view;

    public Chat_fragment_buttom_other() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_chat_fragment_buttom_other, container, false);
        // Inflate the layout for this fragment
        return main_view;
    }


}
