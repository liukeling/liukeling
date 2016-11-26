package com.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.Tools.resource;
import com.example.fragments.FrindListmain_fragment;
import com.example.fragments.SysInfolist_fragmnet;

import java.util.ArrayList;
import java.util.HashMap;

import comm.SysInfo;
import comm.user;

public class QqMessageService extends Service {
    // 用于接受信息的handler
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if (what == 3) {

            }
//            else if (what == 71) {
//                //获取到好友的信息
//                user frind_info = (user) msg.obj;
//            }
            else if (what == 9) {

            } else if (what == 10) {

            }
            //TODO
//            else if (what == 1232 && curr_fragment instanceof FrindListmain_fragment) {
//                ((FrindListmain_fragment) curr_fragment).freshed();
//            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        // 获取好友列表数据
//        resource.getfrindListdata(handler);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(QqMessageService.this, "bind", Toast.LENGTH_SHORT).show();

        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(QqMessageService.this, "unbind", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }
}
