package com.example.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.copyqq.R;

public class DynamicFragment extends Fragment {

    float X = -1f;
    float Y = -1f;
    boolean shanghua = false;
    private View main_View;
    RecyclerView recyclerView;

    public DynamicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        main_View = inflater.inflate(R.layout.fragment_dynamic, container, false);
        android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) main_View.findViewById(R.id.swiperefresh);
        final ScrollView scrollView = (ScrollView) main_View.findViewById(R.id.scrollview);
        recyclerView = (RecyclerView) main_View.findViewById(R.id.recyclerView);
        //根据用户屏幕的滑动来判断是否上滑，然后加载新数据
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int scrollY = v.getScrollY();
                        int height = v.getHeight();
                        int scrollViewMeasuredHeight = scrollView.getChildAt(0).getMeasuredHeight();

                        if ((scrollY + height) == scrollViewMeasuredHeight) {
                            if (X == -1f && Y == -1f) {
                                X = event.getX();
                                Y = event.getY();
                            } else {
                                if (shanghua && (Math.abs((Y-event.getY())))/50 >= 0.9) {
                                    //上拉加载

                                } else {
                                    if ((Y - event.getY()) > Math.abs(X - event.getX())) {
                                        shanghua = true;
                                    }else{
                                        shanghua = false;
                                    }
                                }

                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        shanghua = false;
                        X = -1;
                        Y = -1;
                        break;
                }
                return false;
            }
        });
        return main_View;
    }

    private class MyRecyclerViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

}
