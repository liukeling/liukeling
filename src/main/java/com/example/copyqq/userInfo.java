package com.example.copyqq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.resource;

import comm.user;

/**
 * Created by liukeling on 2016/4/24.
 */
public class UserInfo extends Activity implements View.OnClickListener {

    private TextView back, title, more, user_name, user_id;
    private user u;
    private Button tel,sendMessage,ok_add;

    private void initView(){
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
        user_id.setText(u.getZhanghao()+"");
        back.setOnClickListener(this);
        ok_add.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        tel.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        u = (user) intent.getSerializableExtra("user");
        if(u == null){
            Toast.makeText(UserInfo.this, "没找到该好友", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }else {
            initView();
            if(u.equals(resource.getMe())){
                title.setText("我的资料");
                tel.setVisibility(View.GONE);
                sendMessage.setVisibility(View.GONE);
                ok_add.setVisibility(View.GONE);
            }else{
                title.setText("个人资料");
                boolean isFrind = false;
                for(user frinds : resource.frindList.values()){
                    if(u.equals(frinds)){
                        isFrind = true;
                        break;
                    }
                }
                if(isFrind){
                    tel.setVisibility(View.VISIBLE);
                    sendMessage.setVisibility(View.VISIBLE);
                    ok_add.setVisibility(View.GONE);
                }else{
                    tel.setVisibility(View.GONE);
                    sendMessage.setVisibility(View.GONE);
                    ok_add.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.ok_add:
                Intent add_intent = new Intent(this, AddFrind_activity.class);
                startActivityForResult(add_intent, 0);
                break;
            case R.id.sendMessage:
                Intent intent = new Intent(this, Chat.class);
                intent.putExtra("frind", u);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == 0){
            int i = data.getIntExtra("result", -1);
            if(i != -1) {
                resource.sendAddFrindRequest(u.getZhanghao() + "", i, this);
            }
        }
    }
}
