package com.example.Tools;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dbdao.dbdao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MBENBEN on 2016/8/19.
 */
public class HttpTools {
    //查询说说
    public static void getShuoShuo(final String selectType, final String ssid, final Handler handler, final boolean getme) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao + "&type=select&ssid=" + ssid + "&selecttype=" + selectType+"&me="+getme);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream ips = connection.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] byt = new byte[1024];
                        int len = 0;
                        while(((len = ips.read(byt)) != -1)){
                            bos.write(byt, 0, len);
                        }
                        ips.close();
                        String jso = bos.toString();
                        Message msg = new Message();
                        if(!getme) {
                            if ("old".equals(selectType)) {
                                msg.what = 10010;
                            } else if ("new".equals(selectType)) {
                                msg.what = 1001011;
                            }
                        }else{
                            if("old".equals(selectType)){
                                msg.what = 10000;
                            }else if("new".equals(selectType)){
                                msg.what = 10001;
                            }
                        }
                        msg.obj = jso;
                        handler.sendMessage(msg);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
    //删除说说
    public static void delShuoShuo(final String ssid, final Handler handler){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try{
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao+"&type=del&ssid="+ssid);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream ips = connection.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] byt = new byte[1024];
                        int len = 0;
                        while(((len = ips.read(byt)) != -1)){
                            bos.write(byt, 0, len);
                        }
                        ips.close();
                        String jso = bos.toString();
                        Message msg = new Message();
                        msg.what = 1008611;
                        msg.obj = jso;
                        handler.sendMessage(msg);
                    } else {

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
    //添加说说
    public static void addShuoShuo(final String neirong, final Handler handler){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao + "&type=add&contect="+neirong);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream ips = connection.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] byt = new byte[1024];
                        int len = 0;
                        while(((len = ips.read(byt)) != -1)){
                            bos.write(byt, 0, len);
                        }
                        ips.close();
                        String jso = bos.toString();
                        Message msg = new Message();
                        msg.what = 10086;
                        msg.obj = jso;
                        handler.sendMessage(msg);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
