package com.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class QqMessageService extends Service {
    public static boolean isLogin = false;
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
