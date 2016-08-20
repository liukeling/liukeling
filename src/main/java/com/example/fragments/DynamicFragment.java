package com.example.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.HttpTools;
import com.example.copyqq.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import comm.shuoshuo;
import comm.user;

public class DynamicFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener {

    float X = -1f;
    float Y = -1f;
    boolean shanghua = false;
    boolean load = false;
    private View main_View;
    RecyclerView recyclerView;
    TextView tv_load, myss;
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<shuoshuo> recyclerData;
    MyRecyclerViewAdapter adapter;

    private ScrollView scrollView;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //加载说说的数据 10010是上拉加载，1001011是下拉刷新
            if(msg.what == 10010 || msg.what == 1001011) {
                if(msg.what == 10010) {
                    //上拉加载完成设置
                    tv_load.setText("加载更多");
                }
                if(msg.what == 1001011){
                    //下拉刷新后设置处理
                    swipeRefreshLayout.setRefreshing(false);
                }
                String json = msg.obj.toString();
                try {
                    JSONObject job = new JSONObject(json);
                    String reason = job.getString("reason");
                    JSONArray ja = job.getJSONArray("result");
                    if ("获取成功".equals(reason)) {
                        if (ja.length() != 0) {
                            int length = ja.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject job1 = ja.getJSONObject(i);
                                shuoshuo ss = new shuoshuo();
                                ss.setForm_ssid(job1.getInt("fromssid"));
                                ss.setFabutime(job1.getString("fabutime"));

                                JSONObject userjson = job1.getJSONObject("user");
                                user suser = new user(userjson.getInt("userzhanghao"));
                                suser.setName(userjson.getString("username"));
                                ss.setSsuser(suser);
                                ss.setSsid(job1.getString("ssid"));
                                ss.setNeirong(job1.getString("contact"));
                                ss.setDianzanshu(job1.getString("dianzannumbeer"));
                                //如果是上拉加载就直接添加，否则将数据添加到上方
                                if(msg.what == 10010) {
                                    recyclerData.add(ss);
                                }else if(msg.what == 1001011){
                                    recyclerData.add(i, ss);
                                }
                            }
                        }
                        //数据更新完后更新布局
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public DynamicFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //主布局
        main_View = inflater.inflate(R.layout.fragment_dynamic, container, false);
        //初始化数据
        recyclerData = new ArrayList<>();
        //准备适配器
        adapter = new MyRecyclerViewAdapter();
        //找控件
        swipeRefreshLayout = (SwipeRefreshLayout) main_View.findViewById(R.id.swiperefresh);
        scrollView = (ScrollView) main_View.findViewById(R.id.scrollview);
        recyclerView = (RecyclerView) main_View.findViewById(R.id.recyclerView);
        tv_load = (TextView) main_View.findViewById(R.id.load);
        myss = (TextView) main_View.findViewById(R.id.Myss);
        //recyclerview设置layoutmanager和设置适配器,不设置layoutmanager更新不了布局
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        //设置各种监听
        swipeRefreshLayout.setOnRefreshListener(this);
        tv_load.setOnClickListener(this);
        myss.setOnClickListener(this);
        //根据用户屏幕的滑动来判断是否上滑，然后加载新数据
        scrollView.setOnTouchListener(this);

        loding(0);
        return main_View;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击“加载更多”
            case R.id.load:
                loding(0);
                break;
            case R.id.Myss:

                break;
        }
    }

    public void loding(int type) {
        //0是上拉加载，其他是下拉刷新
        if(type == 0) {
            if ("正在加载...".equals(tv_load.getText().toString())) {
                Toast.makeText(getContext(), "正在加载，请稍后", Toast.LENGTH_SHORT).show();
            } else if ("加载更多".equals(tv_load.getText().toString())) {
                tv_load.setText("正在加载...");
                String sid = "-1";
                if (recyclerData.size() != 0) {
                    sid = recyclerData.get(recyclerData.size() - 1).getSsid();
                }
                HttpTools.getShuoShuo("old", sid, handler);
            }
        }else{
            String sid = "0";
            if(recyclerData.size() != 0){
                sid = recyclerData.get(0).getSsid();
            }
            HttpTools.getShuoShuo("new", sid, handler);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            //自己写的根据scrollview是否滑到底以及用户是否是上滑来判断是否上拉加载
            case R.id.scrollview:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int scrollY = v.getScrollY();
                        int height = v.getHeight();
                        int scrollViewMeasuredHeight = scrollView.getChildAt(0).getMeasuredHeight();
                        if (scrollViewMeasuredHeight < scrollView.getHeight()) {
                            scrollViewMeasuredHeight = scrollView.getHeight();
                        }
                        if ((scrollY + height) == scrollViewMeasuredHeight) {

                            if (X == -1f && Y == -1f) {
                                X = event.getX();
                                Y = event.getY();
                            } else {
                                if (shanghua && (Math.abs((Y - event.getY()))) / 50 >= 0.9) {
                                    //上拉加载
                                    load = true;
                                } else {
                                    if ((Y - event.getY()) > Math.abs(X - event.getX())) {
                                        shanghua = true;
                                    } else {
                                        shanghua = false;
                                    }
                                    load = false;
                                }

                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        shanghua = false;
                        X = -1;
                        Y = -1;
                        if (load) {
                            loding(0);
                        }
                        load = false;
                        break;

                }
                break;

        }
        return false;
    }

    @Override
    public void onRefresh() {
        //下拉刷新
        loding(1);
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyHolder> {
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(getContext(), R.layout.dynamic_item, null);
            MyHolder holder = new MyHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            shuoshuo ss = recyclerData.get(position);
            holder.username.setText(ss.getSsuser().getName());
            holder.dz_count.setText("点赞数("+ss.getDianzanshu()+")");
            holder.sstime.setText(ss.getFabutime());
            holder.ss_contect.setText(ss.getNeirong());
        }

        @Override
        public int getItemCount() {
            return recyclerData.size();
        }
        class MyHolder extends RecyclerView.ViewHolder{

            public TextView username, sstime, dz_count, ss_contect;
            public EditText et_pl;

            public MyHolder(View itemView) {
                super(itemView);
                username = (TextView) itemView.findViewById(R.id.username);
                sstime = (TextView) itemView.findViewById(R.id.sstime);
                dz_count = (TextView) itemView.findViewById(R.id.dz_count);
                et_pl = (EditText) itemView.findViewById(R.id.et_pl);
                ss_contect = (TextView) itemView.findViewById(R.id.ss_contect);
            }
        }
    }

}
