package com.example.copyqq;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.resource;

import java.util.HashMap;

import comm.user;

//"更多"界面
public class FrindInfo_More extends AppCompatActivity implements View.OnClickListener {
    //返回键
    TextView tv_back, groupName;
    //好友分组所在的线性布局
    LinearLayout ll_group;
    //删除好友的按钮
    Button btn_del;
    //好友
    user intentUser;
    //分组
    HashMap<Integer, String> group = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frindinfo__more);
        //获取到传递的数据
        intentUser = (user) getIntent().getSerializableExtra("user");
        //判断intentuser是否为空
        if (intentUser != null) {
            tv_back = (TextView) findViewById(R.id.back);
            ll_group = (LinearLayout) findViewById(R.id.ll_group);
            btn_del = (Button) findViewById(R.id.del);
            groupName = (TextView) findViewById(R.id.groupName);

            tv_back.setOnClickListener(this);
            btn_del.setOnClickListener(this);
            ll_group.setOnClickListener(this);

            setGroupName();

        } else {
            Toast.makeText(FrindInfo_More.this, "好友为空!!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    public void setGroupName() {

        for (HashMap<HashMap<Integer, String>, user> hm : resource.frinds) {
            user u = hm.values().iterator().next();
            if (u.equals(intentUser)) {
                group = hm.keySet().iterator().next();
                break;
            }
        }
        groupName.setText(group.values().iterator().next());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.back:
                onBackPressed();
                break;
            //删除
            case R.id.del:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resource.delFrind(intentUser, FrindInfo_More.this);
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
            //好友分组
            case R.id.ll_group:
                Intent intent = new Intent(this, CheckGroupActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 0) {
            int result = data.getIntExtra("result", -1);
            if (result == -1) {
                return;
            } else {
                if (group.keySet().iterator().next() == result) {
                    Toast.makeText(FrindInfo_More.this, "已在该分组中", Toast.LENGTH_SHORT).show();
                } else {
                    resource.moveFrind(intentUser, result, this);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
