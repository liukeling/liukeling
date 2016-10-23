package com.example.copyqq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.Tools.resource;

import java.util.ArrayList;
import java.util.HashMap;

import comm.user;

//这是个有返回值的activity，
public class CheckGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private int position;
    private TextView back, send;
    private Spinner gruop_spinner;
    //储存分组，以分组的id为键，分组名为值得键值对集合
    private HashMap<Integer, String> gruops = new HashMap<>();
    //用于储存分组集合的"键"
    private ArrayList<Integer> keys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addfrind);
        //储存分组"键值对",integer是分组的id，String是分组的名称，user是该分组的用户
        for (HashMap<HashMap<Integer, String>, user> hm1 : resource.frinds) {
            for (HashMap<Integer, String> hm2 : hm1.keySet()) {
                gruops.putAll(hm2);
            }
        }
        keys.addAll(gruops.keySet());
        //找到各种控件
        back = (TextView) findViewById(R.id.back);
        gruop_spinner = (Spinner) findViewById(R.id.gruop_spinner);
        send = (TextView) findViewById(R.id.send);
        //设置点击监听
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        //group_spinner的适配器
        MySpinnerAdapter adapter = new MySpinnerAdapter();
        //设置适配器
        gruop_spinner.setAdapter(adapter);
        //条目选择监听
        gruop_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CheckGroupActivity.this.position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent data = new Intent();
        data.putExtra("result", -1);
        setResult(0, data);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.send:
                Intent data = new Intent();
                data.putExtra("result", keys.get(position));
                setResult(0, data);
                finish();
                break;
        }

    }

    private class MySpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return gruops.size();
        }

        @Override
        public Object getItem(int position) {
            return keys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(CheckGroupActivity.this, R.layout.spinner_item, null);
            }
            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText("" + gruops.get(keys.get(position)));
            return view;
        }
    }
}
