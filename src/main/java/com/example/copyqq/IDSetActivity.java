package com.example.copyqq;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyViews.MyListView;
import com.example.Tools.resource;
import com.example.adapter.IDListViewAdapter;

import java.util.HashSet;
import java.util.Set;

public class IDSetActivity extends AppCompatActivity implements View.OnClickListener {

    private MyListView lv_IDs;
    private TextView tv_addID, back, IdEdit,ID_back;
    private IDListViewAdapter adapter;
    public static Handler handler;
    private AlertDialog whaitDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView(){
        setContentView(R.layout.activity_idset);
        lv_IDs = (MyListView) findViewById(R.id.IDs);
        tv_addID = (TextView) findViewById(R.id.tv_addID);
        back = (TextView) findViewById(R.id.back);
        IdEdit = (TextView) findViewById(R.id.IdEdit);
        ID_back = (TextView) findViewById(R.id.ID_back);

        back.setOnClickListener(this);
        tv_addID.setOnClickListener(this);
        IdEdit.setOnClickListener(this);
        ID_back.setOnClickListener(this);
        Set<String> Ids = new HashSet<>();
        Ids.addAll(resource.idSet);
        if(!Ids.contains(resource.Myzhanghao)){
            Ids.add(resource.Myzhanghao);
        }
        adapter = new IDListViewAdapter(this,Ids);
        lv_IDs.setAdapter(adapter);
        adapter.setCanEdit(false);
        whaitDialog = new AlertDialog.Builder(this)
                .setTitle("正在退出。。。")
                .create();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 13:
                        if (whaitDialog != null){
                            whaitDialog.dismiss();
                        }
                        Toast.makeText(IDSetActivity.this, "成功退出", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 14:
                        if (whaitDialog != null){
                            whaitDialog.dismiss();
                        }
                        Toast.makeText(IDSetActivity.this, "退出失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_addID:

                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.IdEdit:
                String edit_string = IdEdit.getText().toString();
                if("编辑".equals(edit_string)){
                    adapter.setCanEdit(true);
                    IdEdit.setText("完成");
                }else if("完成".equals(edit_string)){
                    adapter.setCanEdit(false);
                    IdEdit.setText("编辑");
                }
                break;
            case R.id.ID_back:
                AlertDialog exitDialog = new AlertDialog.Builder(this)
                        .setTitle("确定退出qq？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //qq下线
                                resource.outLine();
                                if (whaitDialog != null){
                                    whaitDialog.show();
                                }
                                dialog.dismiss();
                            }
                        })
                        .create();
                exitDialog.show();
                break;
        }
    }
}
