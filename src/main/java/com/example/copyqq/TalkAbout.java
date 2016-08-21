package com.example.copyqq;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.HttpTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import comm.shuoshuo;
import comm.user;

public class TalkAbout extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    //用于记录手指滑动一开始的坐标
    float X = -1;
    float Y = -1;
    boolean shanghua = false;
    boolean loading = false;
    ArrayList<shuoshuo> delshuoshuos;
    shuoshuo delshuoshuo = null;
    EditText contact;
    Button add;
    TextView load;
    ScrollView scrollview;
    RecyclerView recyclerView;

    ArrayList<shuoshuo> data;
    MyRecyclerViewAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10000 || msg.what == 10001) {
                //上拉加载
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
                                if (msg.what == 10000) {
                                    data.add(ss);
                                } else if (msg.what == 10001) {
                                    data.add(i, ss);
                                }
                            }
                        }
                        //数据更新完后更新布局
                        adapter.notifyDataSetChanged();
                        load.setText("加载更多");
                    } else {
                        Toast.makeText(TalkAbout.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == 10086){
                contact.setText("");
                try {
                    JSONObject job = new JSONObject((String)msg.obj);
                    Toast.makeText(TalkAbout.this, ""+job.getString("reason"), Toast.LENGTH_SHORT).show();
                    setResult(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading(1);
            }else if(msg.what == 1008611){
                contact.setText("");
                try {
                    JSONObject job = new JSONObject((String)msg.obj);
                    Toast.makeText(TalkAbout.this, ""+job.getString("reason"), Toast.LENGTH_SHORT).show();
                    if(job.getString("reason") != null && job.getString("reason").contains("成功") && delshuoshuo != null){
                        data.remove(delshuoshuo);
                        adapter.notifyDataSetChanged();
                        delshuoshuos.add(delshuoshuo);
                        Intent intent = new Intent();
                        intent.putExtra("delss", delshuoshuos);
                        setResult(1, intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_about);
        add = (Button) findViewById(R.id.add);
        contact = (EditText) findViewById(R.id.contact);
        load = (TextView) findViewById(R.id.load);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        data = new ArrayList<>();
        adapter = new MyRecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        scrollview.setOnTouchListener(this);
        load.setOnClickListener(this);
        add.setOnClickListener(this);
        loading(0);
        delshuoshuos = new ArrayList<>();
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
                        int scrollViewMeasuredHeight = scrollview.getChildAt(0).getMeasuredHeight();
                        if (scrollViewMeasuredHeight < scrollview.getHeight()) {
                            scrollViewMeasuredHeight = scrollview.getHeight();
                        }
                        if ((scrollY + height) == scrollViewMeasuredHeight) {

                            if (X == -1f && Y == -1f) {
                                X = event.getX();
                                Y = event.getY();
                            } else {
                                if (shanghua && (Math.abs((Y - event.getY()))) / 50 >= 0.9) {
                                    //上拉加载是否开始
                                    loading = true;
                                } else {
                                    if ((Y - event.getY()) > Math.abs(X - event.getX())) {
                                        //判断一开始是否是上滑
                                        shanghua = true;
                                    } else {
                                        shanghua = false;
                                    }
                                    loading = false;
                                }

                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        shanghua = false;
                        X = -1;
                        Y = -1;
                        if (loading) {
                            loading(0);
                        }
                        loading = false;
                        break;

                }
                break;

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load:
                loading(0);
                break;
            case R.id.add:
                String neirong = contact.getText().toString();
                if("".equals(neirong) || neirong == null){
                    Toast.makeText(TalkAbout.this, "说说内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    HttpTools.addShuoShuo(neirong, handler);
                }
                break;
        }
    }

    public void loading(int type) {
        if(type == 0) {
            if ("加载更多".equals(load.getText().toString())) {
                load.setText("正在加载...");
                String ssid = "-1";
                if (data.size() != 0) {
                    ssid = data.get(data.size() - 1).getSsid();
                }
                HttpTools.getShuoShuo("old", ssid, handler, true);
            } else if ("正在加载...".equals(load.getText().toString())) {
                Toast.makeText(this, "正在加载，请稍后", Toast.LENGTH_SHORT).show();
            }
        }else{
            String ssid = "-1";
            if(data.size() != 0){
                ssid = data.get(0).getSsid();
            }
            HttpTools.getShuoShuo("new", ssid, handler, true);
        }
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyHolder> {
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(TalkAbout.this, R.layout.dynamic_item, null);
            MyHolder holder = new MyHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            final shuoshuo ss = data.get(position);
            holder.username.setText(ss.getSsuser().getName());
            holder.dz_count.setText("点赞数(" + ss.getDianzanshu() + ")");
            holder.sstime.setText(ss.getFabutime());
            holder.ss_contect.setText(ss.getNeirong());
            holder.todo.setText("删除");
            holder.todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpTools.delShuoShuo(ss.getSsid(), handler);
                    delshuoshuo = ss;
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            public TextView username, todo, sstime, dz_count, ss_contect;
            public EditText et_pl;

            public MyHolder(View itemView) {
                super(itemView);
                username = (TextView) itemView.findViewById(R.id.username);
                sstime = (TextView) itemView.findViewById(R.id.sstime);
                dz_count = (TextView) itemView.findViewById(R.id.dz_count);
                et_pl = (EditText) itemView.findViewById(R.id.et_pl);
                ss_contect = (TextView) itemView.findViewById(R.id.ss_contect);
                todo = (TextView) itemView.findViewById(R.id.todo);
            }
        }
    }

}
