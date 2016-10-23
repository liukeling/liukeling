package com.example.copyqq;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.HttpTools;
import com.example.Tools.resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import comm.pinglun;
import comm.shuoshuo;
import comm.user;

public class ShuoShuoInfo extends AppCompatActivity implements View.OnClickListener {

    private String ssid;
    private shuoshuo ss;
    private TextView username, sstime, contact, todo, tv_dzusers;
    private ImageView iv_dz, iv_pl, iv_okpl;
    private RecyclerView rv_pl;
    private EditText pl;
    private ArrayList<pinglun> data;
    private MyAdapter adapter;

    private ArrayList<String> ss_Ids;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 548:
                    //获取某条说说所有点赞的用户
                    try {
                        JSONObject job = new JSONObject((String) msg.obj);
                        String reason = job.getString("reason");
                        if ("获取成功".equals(reason)) {
                            JSONArray ja = job.getJSONArray("result");
                            if (ja.length() > 0) {
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject j = ja.getJSONObject(i);
                                    if (i != 0) {
                                        tv_dzusers.setText(tv_dzusers.getText().toString() + "、" + j.getString("username"));
                                    } else {
                                        tv_dzusers.setText(j.getString("username"));
                                    }
                                }
                            }
                        }else{
                            Toast.makeText(ShuoShuoInfo.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 53:
                    //得到某条说说
                    try {
                        JSONObject job = new JSONObject((String) msg.obj);
                        String reason = job.getString("reason");
                        if ("获取成功".equals(reason)) {
                            JSONObject j = job.getJSONObject("result");
                            JSONObject userJ = j.getJSONObject("user");

                            shuoshuo ss = new shuoshuo();
                            user u = new user(userJ.getInt("userzhanghao"));
                            u.setName(userJ.getString("username"));
                            ss.setDz(j.getBoolean("isdz"));
                            ss.setSsuser(u);
                            ss.setDianzanshu(j.getString("dianzannumbeer"));
                            ss.setFabutime(j.getString("fabutime"));
                            ss.setForm_ssid(j.getInt("fromssid"));
                            ss.setNeirong(j.getString("contact"));
                            ss.setSsid(j.getString("ssid"));

                            ShuoShuoInfo.this.ss = ss;

                            username.setText(u.getName());
                            sstime.setText(ss.getFabutime());
                            contact.setText(ss.getNeirong());
                            if (ss.getSsuser().getZhanghao() == Integer.parseInt(resource.Myzhanghao)) {
                                todo.setText("删除");
                            } else {
                                todo.setText("转发");
                            }
                            if (ss.isDz()) {
                                iv_dz.setImageResource(R.mipmap.ss_dz);
                            } else {
                                iv_dz.setImageResource(R.mipmap.ss_dz_ok);
                            }
                        }else{
                            Toast.makeText(ShuoShuoInfo.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5464:
                    //得到所有评论
                    try {
                        JSONObject job = new JSONObject((String)msg.obj);
                        String reason = job.getString("reason");
                        if ("获取成功".equals(reason)) {
                            data.clear();
                            JSONArray ja = job.getJSONArray("result");
                            if(ja.length() > 0){
                                for(int i = 0; i < ja.length(); i++){
                                    JSONObject j = ja.getJSONObject(i);
                                    JSONObject userJ = j.getJSONObject("user");
                                    pinglun pl = new pinglun();
                                    user pluser = new user(userJ.getInt("userid"));
                                    pluser.setName(userJ.getString("username"));
                                    pl.setPlUser(pluser);
                                    pl.setPlnr(j.getString("plnr"));
                                    pl.setPltime(j.getString("pltime"));
                                    pl.setSsid(j.getString("ssid"));
                                    data.add(pl);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }else{

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1020:
                    try {
                        JSONObject job = new JSONObject((String)msg.obj);
                        String reason = job.getString("reason");
                        if("点赞成功".equals(reason)){
                                iv_dz.setImageResource(R.mipmap.ss_dz);
                        }else if("取消点赞成功".equals(reason)){
                            iv_dz.setImageResource(R.mipmap.ss_dz_ok);
                        }
                        Toast.makeText(ShuoShuoInfo.this, reason+"", Toast.LENGTH_SHORT).show();
                        //用于更新返回界面的说说
                        updateNext();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1030:
                    try {
                        JSONObject job = new JSONObject((String)msg.obj);
                        String reason = job.getString("reason");
                        Toast.makeText(ShuoShuoInfo.this, ""+reason, Toast.LENGTH_SHORT).show();
                        if(reason.contains("成功")){
                            updateNext();
                            //更新评论
                            HttpTools.getAllPL(this, ssid);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1010:
                    try {
                        JSONObject job = new JSONObject((String)msg.obj);
                        String reason = job.getString("reason");
                        Toast.makeText(ShuoShuoInfo.this, ""+reason, Toast.LENGTH_SHORT).show();
                        if(reason.contains("成功")){
                            setResult(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1008611:
                    try {
                        JSONObject job = new JSONObject((String)msg.obj);
                        String reason = job.getString("reason");
                        Toast.makeText(ShuoShuoInfo.this, ""+reason, Toast.LENGTH_SHORT).show();
                        if(reason.contains("成功")){
                            delNext();
                            onBackPressed();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private void delNext(){
        Intent data = new Intent();
        ArrayList<shuoshuo> al = new ArrayList<>();
        al.add(ss);
        data.putExtra("delss", al);
        setResult(1, data);
    }
    private void updateNext(){
        //用于更新返回界面的说说
        Intent resultdata = new Intent();
        ArrayList<String> ids = new ArrayList<>();
        ids.add(ssid);
        resultdata.putExtra("Ids", ids);
        setResult(2,resultdata);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuo_shuo_info);
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        ssid = getIntent().getStringExtra("ssid");
        if ("".equals(ssid) || ssid == null) {
            Toast.makeText(ShuoShuoInfo.this, "没找到该说说!!!", Toast.LENGTH_SHORT).show();
        } else {
            //找到各种控件
            username = (TextView) findViewById(R.id.username);
            sstime = (TextView) findViewById(R.id.sstime);
            contact = (TextView) findViewById(R.id.contact);
            todo = (TextView) findViewById(R.id.todo);
            tv_dzusers = (TextView) findViewById(R.id.tv_dzusers);
            iv_dz = (ImageView) findViewById(R.id.iv_dz);
            iv_pl = (ImageView) findViewById(R.id.iv_pl);
            iv_okpl = (ImageView) findViewById(R.id.iv_okpl);
            rv_pl = (RecyclerView) findViewById(R.id.rv_pl);
            pl = (EditText) findViewById(R.id.pl);
            //获取说说
            HttpTools.getShuoShuo("onlyOne", ssid, handler, false);
            //获取所有点赞用户
            HttpTools.getAllDZUser(handler, ssid);
            //初始化数据
            ss_Ids = new ArrayList<>();
            //获取所有评论
            HttpTools.getAllPL(handler, ssid);
            //设置评论数据
            data = new ArrayList<>();
            adapter = new MyAdapter();
            //recyclerview设置layoutmanager和设置适配器,不设置layoutmanager更新不了布局
            rv_pl.setLayoutManager(new LinearLayoutManager(ShuoShuoInfo.this));
            rv_pl.setAdapter(adapter);
            //设置监听
            iv_dz.setOnClickListener(this);
            iv_pl.setOnClickListener(this);
            iv_okpl.setOnClickListener(this);
            todo.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_dz:
                HttpTools.dz(ssid, handler);
                break;
            case R.id.iv_pl:
                //设置pl获取焦点
                pl.setFocusable(true);
                pl.setFocusableInTouchMode(true);
                pl.requestFocus();
                pl.requestFocusFromTouch();
                break;
            case R.id.iv_okpl:
                String pl_contect = pl.getText().toString();
                if("".equals(pl_contect) || pl_contect == null){
                    Toast.makeText(ShuoShuoInfo.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    HttpTools.addpl(ssid, pl_contect, handler);
                }
                pl.setText("");
                break;
            case R.id.todo:
                String td = todo.getText().toString();
                if("转发".equals(td)){
                    HttpTools.forwardss(ssid, handler);
                }else if("删除".equals(td)){
                    AlertDialog dialog = new AlertDialog.Builder(ShuoShuoInfo.this)
                            .setTitle("提示")
                            .setMessage("确定删除该说说?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HttpTools.delShuoShuo(ssid, handler);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
                break;
            case R.id.tv_back:
                onBackPressed();
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(ShuoShuoInfo.this, R.layout.pl_item, null);
            MyHolder myHolder = new MyHolder(itemView);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            pinglun pl = data.get(position);
            holder.pl_name.setText(pl.getPlUser().getName());
            holder.pl_contect.setText(pl.getPlnr());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            public TextView pl_name, pl_contect;

            public MyHolder(View itemView) {
                super(itemView);
                pl_name = (TextView) itemView.findViewById(R.id.pl_name);
                pl_contect = (TextView) itemView.findViewById(R.id.pl_contect);
            }
        }
    }
}
