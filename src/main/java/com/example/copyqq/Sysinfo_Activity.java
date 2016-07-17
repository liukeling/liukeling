package com.example.copyqq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;

import comm.SysInfo;

public class Sysinfo_Activity extends AppCompatActivity {

    private SysInfo sinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysinfo_);
        Intent intent = getIntent();
        sinfo = (SysInfo) intent.getSerializableExtra("sinfo");
        if(sinfo == null){
            Toast.makeText(Sysinfo_Activity.this, "未找到系统消息", Toast.LENGTH_SHORT).show();
            finish();
        }
        TextView tv_neirong = (TextView) findViewById(R.id.neirong);
        TextView time = (TextView) findViewById(R.id.time);
        Date date = sinfo.getSys_date();
        try {
            time.setText(date.toString()
                    + " 发布,发布者账号:"
                    + sinfo.getReleaseuser());
            tv_neirong.setText(sinfo.getNeirong());
        }catch(Exception e){
            e.printStackTrace();
        }
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
