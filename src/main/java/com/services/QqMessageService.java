package com.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.example.Tools.resource;
import com.example.copyqq.MainFragment;
import com.example.copyqq.R;
import com.example.fragments.FrindListmain_fragment;
import com.example.fragments.SysInfolist_fragmnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import comm.SysInfo;
import comm.user;

public class QqMessageService extends Service {
    public static Handler MFHandler;
    private SoundPool soundPool;
    // 用于接受信息的handler
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (MFHandler != null) {
                Message MFmsg = new Message();
                MFmsg.what = msg.what;
                MFmsg.obj = msg.obj;
                MFHandler.sendMessage(MFmsg);
            }
            int what = msg.what;
            if (what == 3) {
                if ("one".equals(msg.obj)) {
                    //开始接受消息
                    resource.jieshouxiaoxiThread(handler);
                    //如果有未读的系统消息则震动
                    for (SysInfo sinfo : resource.Sysinfos) {
                        if (!sinfo.isRead()) {
                            //震动
                            shock();
                            if(MFHandler == null){
                                addNotification("仿qq通知","有系统消息！！！",MainFragment.class, 1);
                            }
                            break;
                        }
                    }
                }
                for (HashMap<HashMap<Integer, String>, user> hm : resource.frinds) {
                    Collection<user> collection = hm.values();
                    if (collection != null && collection.size() >= 1) {
                        user u = collection.iterator().next();
                        if(u != null && "是".equals(u.getHaveMassage())) {
                            playSound();
                            if(MFHandler == null){
                                addNotification("仿qq通知","有未读消息！！！",MainFragment.class, 2);
                            }
                            break;
                        }
                    }
                }
            }else if (what == 9) {
                ArrayList<SysInfo> al = (ArrayList<SysInfo>) msg.obj;
                resource.Sysinfos.clear();
                resource.Sysinfos.addAll(al);
                for (SysInfo sinfo : al) {
                    if (!sinfo.isRead()) {
                        //震动
                        shock();
                        break;
                    }
                }
            }
        }
    };
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //初始化声音
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(getApplicationContext(), R.raw.qq_message, 1);
        // 获取好友列表数据
        resource.getfrindListdata(handler);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void addNotification(String Title, String contact, Class resultActivity, int cur){
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(""+Title)
                .setContentText(""+contact)
                .setSmallIcon(R.mipmap.ic_launcher);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        Intent resultIntent = new Intent(this, resultActivity);
        resultIntent.putExtra("fragment_cur",cur);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainFragment.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(0, builder.build());
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void playSound() {
        soundPool.play(1, 1, 1, 1, 0, 1);
    }
    public void shock(){
        Vibrator vibrator = (Vibrator) QqMessageService.this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        Toast.makeText(QqMessageService.this, "有未读的系统消息", Toast.LENGTH_SHORT).show();
    }
}
