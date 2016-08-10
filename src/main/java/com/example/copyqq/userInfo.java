package com.example.copyqq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comm.user;

public class UserInfo extends Activity implements View.OnClickListener {

    private TextView back, title, more, user_name, user_id;
    private user u;
    private Button tel, sendMessage, ok_add;

    private void initView() {
        View v = View.inflate(this, R.layout.userinfo_layout, null);
        setContentView(v);
        back = (TextView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        more = (TextView) findViewById(R.id.more);
        tel = (Button) findViewById(R.id.tel);
        sendMessage = (Button) findViewById(R.id.sendMessage);
        ok_add = (Button) findViewById(R.id.ok_add);
        user_name = (TextView) findViewById(R.id.user_name);
        user_id = (TextView) findViewById(R.id.user_zhanghao);


        user_name.setText(u.getName());
        user_id.setText(u.getZhanghao() + "");
        back.setOnClickListener(this);
        ok_add.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        tel.setOnClickListener(this);
        more.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //得到要查看的好友
        u = (user) intent.getSerializableExtra("user");
        //好友为空直接返回
        if (u == null) {
            Toast.makeText(UserInfo.this, "没找到该好友", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            initView();
            if (u.equals(resource.getMe())) {
                title.setText("我的资料");
                tel.setVisibility(View.GONE);
                sendMessage.setVisibility(View.GONE);
                ok_add.setVisibility(View.GONE);
            } else {
                title.setText("个人资料");
                boolean isFrind = false;

                for (List<Map<String, user>> al : resource.childs) {
                    for (Map<String, user> m : al) {
                        user frind = m.get("child");
                        if (u.equals(frind)) {
                            isFrind = true;
                            break;
                        }
                    }
                }
                //判断是否已经是好友
                if (isFrind) {
                    tel.setVisibility(View.VISIBLE);
                    sendMessage.setVisibility(View.VISIBLE);
                    ok_add.setVisibility(View.GONE);
                } else {
                    tel.setVisibility(View.GONE);
                    sendMessage.setVisibility(View.GONE);
                    ok_add.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回键
            case R.id.back:
                onBackPressed();
                break;
            //添加好友
            case R.id.ok_add:
                Intent add_intent = new Intent(this, CheckGroupActivity.class);
                startActivityForResult(add_intent, 0);
                break;
            //发送消息
            case R.id.sendMessage:
                Intent intent = new Intent(this, Chat.class);
                intent.putExtra("frind", u);
                startActivity(intent);
                break;
            //更多
            case R.id.more:
                Intent intent2 = new Intent(this, FrindInfo_More.class);
                intent2.putExtra("user", u);
                startActivityForResult(intent2, 1);
                break;
        }

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {

        super.onActivityReenter(resultCode, data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //添加好友的返回码判断
        if (requestCode == 0 && resultCode == 0) {
            int i = data.getIntExtra("result", -1);
            if (i != -1) {
                resource.sendAddFrindRequest(u.getZhanghao() + "", i, this);
            }
        }
        //更多界面删除好友的返回码
        if (requestCode == 1 && resultCode == 5) {
            onBackPressed();
        }
    }


}
