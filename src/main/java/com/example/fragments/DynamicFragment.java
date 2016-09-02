package com.example.fragments;


import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dbdao.dbdao;
import com.example.Tools.HttpTools;
import com.example.Tools.resource;
import com.example.copyqq.R;
import com.example.copyqq.ShuoShuoInfo;
import com.example.copyqq.TalkAbout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

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

    //记录scrollview当前位置
    int[] position = new int[2];

    private ScrollView scrollView;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //加载说说的数据 10010是上拉加载，1001011是下拉刷新
            if (msg.what == 10010 || msg.what == 1001011) {
                if (msg.what == 10010) {
                    //上拉加载完成设置
                    tv_load.setText("加载更多");
                }
                if (msg.what == 1001011) {
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
                                ss.setDz(job1.getBoolean("isdz"));
                                //如果是上拉加载就直接添加，否则将数据添加到上方
                                if (msg.what == 10010) {
                                    recyclerData.add(ss);
                                } else if (msg.what == 1001011) {
                                    recyclerData.add(i, ss);
                                }
                            }
                        }
                        //数据更新完后更新布局
                        adapter.notifyDataSetChanged();
                        //设置scrollview到之前的位置
                        scrollView.smoothScrollTo(position[0], position[1]);
                    } else {
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == 1020){
                try {
                    JSONObject job = new JSONObject((String)msg.obj);
                    String reason = job.getString("reason");
                    Toast.makeText(getContext(), ""+reason, Toast.LENGTH_SHORT).show();
                    String ssid = job.getString("result");
                    //更新本Activity的说说
                    updateSS(ssid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == 1010){
                String json = (String)msg.obj;
                try {
                    JSONObject job = new JSONObject(json);
                    String reason = job.getString("reason");
                    Toast.makeText(DynamicFragment.this.getContext(), ""+reason, Toast.LENGTH_SHORT).show();
                    loding(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == 1030){
                try {
                    JSONObject job = new JSONObject((String)msg.obj);
                    String pl_msg = job.getString("reason");
                    adapter.notifyDataSetChanged();
                    //设置scrollview到之前的位置
                    scrollView.smoothScrollTo(position[0], position[1]);
                    Toast.makeText(DynamicFragment.this.getContext(), pl_msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == 555){
                Toast.makeText(getContext(), (String)msg.obj, Toast.LENGTH_LONG).show();
            }
        }
    };

    public DynamicFragment() {
    }

    private void updateSS(final String ssid){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao+"&type=select&selecttype=onlyOne&ssid="+ssid);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream ips = connection.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] byt = new byte[1024];
                        int len = 0;
                        while(((len = ips.read(byt)) != -1)){
                            bos.write(byt, 0, len);
                        }
                        ips.close();
                        String jso = bos.toString();
                        JSONObject job = new JSONObject(jso);
                        JSONObject job1 = job.getJSONObject("result");
                        final shuoshuo ss = new shuoshuo();
                        ss.setForm_ssid(job1.getInt("fromssid"));
                        ss.setFabutime(job1.getString("fabutime"));

                        JSONObject userjson = job1.getJSONObject("user");
                        user suser = new user(userjson.getInt("userzhanghao"));
                        suser.setName(userjson.getString("username"));
                        ss.setSsuser(suser);
                        ss.setSsid(job1.getString("ssid"));
                        ss.setNeirong(job1.getString("contact"));
                        ss.setDz(job1.getBoolean("isdz"));
                        ss.setDianzanshu(job1.getString("dianzannumbeer"));

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (shuoshuo ss1 : recyclerData) {
                                    if (ss.getSsid().equals(ss1.getSsid())) {
                                        ss1.setDz(ss.isDz());
                                        ss1.setDianzanshu(ss.getDianzanshu());
                                        break;
                                    } else {
                                        continue;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                //设置scrollview到之前的位置
                                scrollView.smoothScrollTo(position[0], position[1]);
                            }
                        });
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
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
                Intent intent = new Intent(getContext(), TalkAbout.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case 0:
                    loding(1);
                    break;
                case 1:
                    ArrayList<shuoshuo> delss = (ArrayList<shuoshuo>) data.getSerializableExtra("delss");
                    Iterator<shuoshuo> iterator = recyclerData.iterator();
                    while (iterator.hasNext()) {
                        shuoshuo s = iterator.next();
                        for (shuoshuo ss : delss) {
                            if (ss.getSsid().equals(s.getSsid())) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                    loding(1);
                    break;
                case 2:
                    ArrayList<String> ids = (ArrayList<String>) data.getSerializableExtra("Ids");
                    for(String ssid : ids){
                        updateSS(ssid);
                    }
                    break;
            }
        }
    }

    public void loding(int type) {
        //0是上拉加载，其他是下拉刷新
        if (type == 0) {
            if ("正在加载...".equals(tv_load.getText().toString())) {
                Toast.makeText(getContext(), "正在加载，请稍后", Toast.LENGTH_SHORT).show();
            } else if ("加载更多".equals(tv_load.getText().toString())) {
                tv_load.setText("正在加载...");
                String sid = "-1";
                if (recyclerData.size() != 0) {
                    sid = recyclerData.get(recyclerData.size() - 1).getSsid();
                }
                HttpTools.getShuoShuo("old", sid, handler, false);
            }
        } else {
            String sid = "0";
            if (recyclerData.size() != 0) {
                sid = recyclerData.get(0).getSsid();
            }
            HttpTools.getShuoShuo("new", sid, handler, false);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        position[1] = scrollView.getScrollY();
        position[0] = scrollView.getScrollX();
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
        public void onBindViewHolder(final MyHolder holder, int position) {
            //得到对应的说说对象
            final shuoshuo ss = recyclerData.get(position);
            //设置各种属性值
            holder.username.setText(ss.getSsuser().getName());
            holder.dz_count.setText("点赞数(" + ss.getDianzanshu() + ")");
            holder.sstime.setText(ss.getFabutime());
            holder.ss_contect.setText(ss.getNeirong());
            //判断是否点赞，来改变相应的图片
            if(ss.isDz()) {
                holder.iv_dz.setImageResource(R.mipmap.ss_dz);
            }else{
                holder.iv_dz.setImageResource(R.mipmap.ss_dz_ok);
            }
            holder.iv_dz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpTools.dz(ss.getSsid(), handler);
                }
            });
            //如果说说是自己的那么就不可以转发否则可以转发
            if ((ss.getSsuser().getZhanghao() + "").equals(resource.Myzhanghao)) {
                holder.todo.setVisibility(View.GONE);
            } else {
                holder.todo.setText("转发");
                holder.todo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpTools.forwardss(ss.getSsid(), handler);
                    }
                });
            }
            //发表评论
            holder.iv_pl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pl_contect = holder.et_pl.getText().toString();
                    HttpTools.addpl(ss.getSsid(), pl_contect, handler);
                    holder.et_pl.setText("");
                }
            });
            //显示评论
            showPL(holder, ss);
        }



        @Override
        public int getItemCount() {
            return recyclerData.size();
        }

        //获取最后一条评论并显示
        private void showPL(final MyHolder holder, final shuoshuo ss){
            new AsyncTask<Void, Void, String>(){

                @Override
                protected String doInBackground(Void... params) {
                    try {
                        URL url = new URL("http://"+dbdao.fuwuip+":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao+"&type=selectpl&plselecttype=selectlast&ssid="+ss.getSsid());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();
                        int code = connection.getResponseCode();
                        if (code == 200) {
                            InputStream ips = connection.getInputStream();
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] byt = new byte[1024];
                            int len = 0;
                            while(((len = ips.read(byt)) != -1)){
                                bos.write(byt, 0, len);
                            }
                            ips.close();
                            String jso = bos.toString();
                            return jso;
                        } else {
                            return "err";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "err";
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    if(!"err".equals(s)){
                        try {
                            JSONObject job = new JSONObject(s);
                            String reason = job.getString("reason");
                            String result = job.getString("result");
                            if("获取成功".equals(reason) && !"null".equals(result)){
                                JSONObject j = new JSONObject(result);
                                JSONObject userJ = j.getJSONObject("user");
                                holder.pl_contect.setText(j.getString("plnr"));
                                holder.pl_name.setText(userJ.getString("username")+":");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(s);
                }
            }.execute();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            public TextView username, todo, sstime, dz_count, ss_contect, pl_name, pl_contect;
            public ImageView iv_dz, iv_pl;
            public EditText et_pl;

            public MyHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                        Intent intent = new Intent(DynamicFragment.this.getContext(), ShuoShuoInfo.class);
                        startActivity(intent);
                    }
                });
                username = (TextView) itemView.findViewById(R.id.username);
                sstime = (TextView) itemView.findViewById(R.id.sstime);
                dz_count = (TextView) itemView.findViewById(R.id.dz_count);
                et_pl = (EditText) itemView.findViewById(R.id.et_pl);
                ss_contect = (TextView) itemView.findViewById(R.id.ss_contect);
                todo = (TextView) itemView.findViewById(R.id.todo);
                iv_dz = (ImageView) itemView.findViewById(R.id.iv_dz);
                iv_pl = (ImageView) itemView.findViewById(R.id.iv_pl);
                pl_contect = (TextView) itemView.findViewById(R.id.pl_contect);
                pl_name = (TextView) itemView.findViewById(R.id.pl_name);
            }
        }
    }

}
