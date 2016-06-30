package com.example.copyqq;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyViews.MyListView;
import com.example.Tools.resource;

import java.util.ArrayList;

import comm.user;

public class Search_frinds extends AppCompatActivity implements View.OnClickListener {

    private EditText string_select;
    private View main;
    private MyListView myListView;
    private boolean back = true;

    private ArrayList<user> listViewData = new ArrayList<>();

    private Myadapter adapter;
//    private
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 8){
                ArrayList<user> al = (ArrayList<user>) msg.obj;
                listViewData.clear();
                if(al != null) {
                    listViewData.addAll(al);
                }
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = View.inflate(this, R.layout.activity_search_frinds, null);
        setContentView(main);
        string_select = (EditText) main.findViewById(R.id.string_select);
        myListView = (MyListView) main.findViewById(R.id.mylistView);
        final Button btn_ok = (Button) main.findViewById(R.id.btn_ok);
        final ImageView ed_quxiao = (ImageView) main.findViewById(R.id.ed_quxiao);
        adapter = new Myadapter();

        myListView.setAdapter(adapter);

        ed_quxiao.setVisibility(View.INVISIBLE);
        string_select.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String e_s = string_select.getText().toString();
                if (e_s != null && !"".equals(e_s)) {
                    btn_ok.setText("搜索");
                    ed_quxiao.setVisibility(View.VISIBLE);
                } else {
                    btn_ok.setText("取消");
                    ed_quxiao.setVisibility(View.INVISIBLE);
                }
            }
        });
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user seach_u = listViewData.get(position);
                Intent intent = new Intent(Search_frinds.this, UserInfo.class);
                intent.putExtra("user", seach_u);
                startActivity(intent);
            }
        });
        btn_ok.setOnClickListener(this);
        ed_quxiao.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if(back) {
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, AddNewFrind.y * 9);
            animation.setDuration(500);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    back = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    startActivity(new Intent(Search_frinds.this, AddNewFrind.class));
                    Search_frinds.super.onBackPressed();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setFillAfter(true);
            main.startAnimation(animation);

        }else{

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ed_quxiao:
                string_select.setText("");
                break;
            case R.id.btn_ok:
                String s = string_select.getText().toString();
                if("".equals(s) || s == null){
                    onBackPressed();
                }else{
                    resource.searchFrinds(handler, s);
                }
                break;
        }
    }
    private class Myadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listViewData.size();
        }

        @Override
        public Object getItem(int position) {
            return listViewData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv_name;
            TextView tv_id;
            if(convertView == null){
                convertView = View.inflate(Search_frinds.this, R.layout.searchuser_item, null);
            }
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_id = (TextView) convertView.findViewById(R.id.tv_id);

            user u = listViewData.get(position);
            tv_name.setText(u.getName());
            tv_id.setText("("+u.getZhanghao()+")");

            return convertView;
        }
    }
}
