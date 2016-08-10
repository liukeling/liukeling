package com.example.copyqq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.resource;

import comm.user;

public class Chatset_Activity extends Activity implements View.OnClickListener {
    private RelativeLayout user_title;
    private TextView back, create_taolun;
    private user intentUser;
    private Button del_frind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentUser = (user) getIntent().getSerializableExtra("user");

        setContentView(R.layout.activity_chatset_);
        user_title = (RelativeLayout) findViewById(R.id.user_title);
        back = (TextView) findViewById(R.id.back);
        create_taolun = (TextView) findViewById(R.id.create_taolun);
        del_frind = (Button) findViewById(R.id.del_frind);

        back.setOnClickListener(this);
        user_title.setOnClickListener(this);
        create_taolun.setOnClickListener(this);
        del_frind.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_title:
                Intent info_intent = new Intent(this, UserInfo.class);
                info_intent.putExtra("user", intentUser);
                startActivity(info_intent);
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.create_taolun:
                Intent create_intent = new Intent(this, ChoiceFrinds.class);
                if(intentUser != null){

                }
                create_intent.putExtra("user", intentUser);
                startActivity(create_intent);
                break;
            case R.id.del_frind:

                AlertDialog alertDialog = new AlertDialog.Builder(Chatset_Activity.this)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resource.delFrind(intentUser, Chatset_Activity.this);
                                setResult(5);
                                onBackPressed();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setTitle("确定删除该好友？")
                        .create();
                alertDialog.show();
                break;
        }
    }
}
