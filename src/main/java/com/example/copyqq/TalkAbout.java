package com.example.copyqq;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dbdao.dbdao;
import com.example.Tools.HttpTools;
import com.example.Tools.resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import comm.shuoshuo;
import comm.user;

public class TalkAbout extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    //用于记录手指滑动一开始的坐标
    float X = -1;
    float Y = -1;
    boolean shanghua = false;
    boolean loading = false;

    //用于储存删除的说说
    ArrayList<shuoshuo> delshuoshuos;
    //用于记录当前要删的说说
    shuoshuo delshuoshuo = null;
    //用于记录更新的说说的id
    ArrayList<String> updateIDs;
    //内容显示的控件
    EditText contact;
    //发表按钮
    Button add;
    //上拉加载的状态显示
    TextView load;
    ScrollView scrollview;
    RecyclerView recyclerView;
    //recyclerView需要的数据
    ArrayList<shuoshuo> data;
    MyRecyclerViewAdapter adapter;
    //记录scrollview当前位置
    int[] position = new int[2];
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10000 || msg.what == 10001) {
                //上拉加载
                String json = msg.obj.toString();
                try {
                    //获取到json
                    JSONObject job = new JSONObject(json);
                    //从json中得到结果参数
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
                                ss.setDz(job1.getBoolean("isdz"));
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
                        //设置scrollview到之前的位置
                        scrollview.smoothScrollTo(position[0], position[1]);
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
                        //设置scrollview到之前的位置
                        scrollview.smoothScrollTo(position[0], position[1]);
                        delshuoshuos.add(delshuoshuo);
                        Intent intent = new Intent();
                        intent.putExtra("delss", delshuoshuos);
                        setResult(1, intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == 1020){
                try {
                    JSONObject job = new JSONObject((String)msg.obj);
                    String reason = job.getString("reason");
                    Toast.makeText(TalkAbout.this, ""+reason, Toast.LENGTH_SHORT).show();
                    String ssid = job.getString("result");
                    //用于更新fragment的说说
                    updateIDs.add(ssid);
                    Intent data = new Intent();
                    data.putExtra("Ids", updateIDs);
                    setResult(2, data);
                    //更新本Activity的说说
                    updateSS(ssid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (msg.what == 1030) {
                try {
                    JSONObject job = new JSONObject((String)msg.obj);
                    String reason = job.getString("reason");
                    adapter.notifyDataSetChanged();
                    //设置scrollview到之前的位置
                    scrollview.smoothScrollTo(position[0], position[1]);
                    Toast.makeText(TalkAbout.this, ""+reason, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == 555){
                Toast.makeText(TalkAbout.this, (String)msg.obj, Toast.LENGTH_LONG).show();
            }else if(msg.what == 377){
                Toast.makeText(TalkAbout.this, "网络请求错误", Toast.LENGTH_SHORT).show();
                if("正在加载...".equals(load.getText().toString())){
                    load.setText("加载更多");
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
        updateIDs = new ArrayList<>();
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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(shuoshuo ss1 : data){
                                    if(ss.getSsid().equals(ss1.getSsid())){
                                        ss1.setDz(ss.isDz());
                                        ss1.setDianzanshu(ss.getDianzanshu());
                                        break;
                                    }else{
                                        continue;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                //设置scrollview到之前的位置
                                scrollview.smoothScrollTo(position[0], position[1]);
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
    public boolean onTouch(View v, MotionEvent event) {
        position[1] = scrollview.getScrollY();
        position[0] = scrollview.getScrollX();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            switch (resultCode){
                case 1:
                    //删除
                    //获取到要删除的说说
                    ArrayList<shuoshuo> dels = (ArrayList<shuoshuo>) data.getSerializableExtra("delss");
                    //将这些说说从list数据中删掉
                    for(shuoshuo ss : dels){
                        TalkAbout.this.data.remove(ss);
                    }
                    //更新适配器
                    adapter.notifyDataSetChanged();
                    //滑到之前的位置
                    scrollview.smoothScrollTo(position[0], position[1]);
                    //添加到要删除的集合中
                    delshuoshuos.addAll(dels);
                    //通过intent和result通知之前的activity
                    Intent intent = new Intent();
                    intent.putExtra("delss", delshuoshuos);
                    setResult(1, intent);

                    break;
                case 2:
                    //更新
                    ArrayList<String> ssids = data.getStringArrayListExtra("Ids");
                    //更新前面fragemnt的说说
                    updateIDs.addAll(ssids);
                    Intent updata = new Intent();
                    updata.putExtra("Ids", updateIDs);
                    setResult(2, updata);
                    //更新自己的说说
                    for(String ssid : ssids){
                        updateSS(ssid);
                    }
                    break;
            }
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
        public void onBindViewHolder(final MyHolder holder, final int position) {
            final shuoshuo ss = data.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TalkAbout.this, ShuoShuoInfo.class);
                    intent.putExtra("ssid", ss.getSsid());
                    TalkAbout.this.startActivityForResult(intent, 1);
                }
            });
            holder.username.setText(ss.getSsuser().getName());
            holder.dz_count.setText("点赞数(" + ss.getDianzanshu() + ")");
            holder.sstime.setText(ss.getFabutime());
            holder.ss_contect.setText(ss.getNeirong());
            holder.todo.setText("删除");
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
            //删除点击事件
            holder.todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpTools.delShuoShuo(ss.getSsid(), handler);
                    delshuoshuo = ss;
                }
            });
            //发表评论
            holder.iv_pl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pl_contect = holder.et_pl.getText().toString();
                    HttpTools.addpl(ss.getSsid(), pl_contect, handler);
                    holder.et_pl.setText("");
                }
            });
            showPL(holder, ss);
        }

        @Override
        public int getItemCount() {
            return data.size();
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
                        Toast.makeText(TalkAbout.this, "网络请求错误", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(s);
                }
            }.execute();
        }
        class MyHolder extends RecyclerView.ViewHolder {

            public TextView username, todo, sstime, dz_count, ss_contect, pl_name, pl_contect;
            public EditText et_pl;
            public ImageView iv_dz, iv_pl;

            public MyHolder(View itemView) {
                super(itemView);
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
