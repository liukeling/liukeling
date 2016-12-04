package com.example.copyqq;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyViews.MyListView;
import com.example.Tools.resource;
import com.example.adapter.IDListViewAdapter;

public class IDSetActivity extends AppCompatActivity implements View.OnClickListener {

    private MyListView lv_IDs;
    private TextView tv_addID, back, IdEdit,ID_back;
    private IDListViewAdapter adapter;

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

        adapter = new IDListViewAdapter(this);
        lv_IDs.setAdapter(adapter);
        adapter.setCanEdit(false);
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
                                resource.outLine();
                                Intent intent = new Intent(IDSetActivity.this, LoginActivity.class);
                                intent.putExtra("lianjiefuwu", false);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .create();
                exitDialog.show();
                break;
        }
    }
}
