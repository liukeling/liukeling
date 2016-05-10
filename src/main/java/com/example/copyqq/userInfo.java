package com.example.copyqq;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/4/24.
 */
public class UserInfo extends Activity {
    private void initView(){
        View v = View.inflate(this, R.layout.userinfo_layout, null);
        setContentView(v);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();



    }
}
