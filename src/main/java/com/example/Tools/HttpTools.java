package com.example.Tools;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.dbdao.dbdao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpTools {
    /*
        10010:返回自己的下拉刷新的说说
        1001011:返回自己的上拉加载的说说
        10000:所有说说的下拉刷新的说说
        10001:所有说说的上拉加载的说说
        53：得到某条说说
        1030：添加评论
        1020:点赞与取消点赞
        1010：转发说说
        1008611：删除说说
        10086:添加说说
        548:获取某条说说所有点赞的用户
        555:打印接收的json
        377:网络连接错误
        5464:得到所有评论
     */
    //查询说说
    public static void getShuoShuo(final String selectType, final String ssid, final Handler handler, final boolean getme) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao + "&type=select&ssid=" + ssid + "&selecttype=" + selectType+"&me="+getme);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(500);
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
                        if(!"onlyOne".equals(selectType)) {
                            if (!getme) {
                                if ("old".equals(selectType)) {
                                    msg.what = 10010;
                                } else if ("new".equals(selectType)) {
                                    msg.what = 1001011;
                                }
                            } else {
                                if ("old".equals(selectType)) {
                                    msg.what = 10000;
                                } else if ("new".equals(selectType)) {
                                    msg.what = 10001;
                                }
                            }
                        }else{
                            msg.what = 53;
                        }
                        msg.obj = jso;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 377;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 377;
                    handler.sendMessage(msg);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
    //添加评论
    public static void addpl(final String ssid, final String contect, final Handler handler){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String encode_contect = URLEncoder.encode(contect, "UTF-8");
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao+"&type=addpl&ssid="+ssid+"&contect="+encode_contect);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(500);
                    urlConnection.connect();
                    int code = urlConnection.getResponseCode();
                    if (code == 200) {
                        InputStream ips = urlConnection.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] byt = new byte[1024];
                        int len = 0;
                        while(((len = ips.read(byt)) != -1)){
                            bos.write(byt, 0, len);
                        }
                        ips.close();
                        String jso = bos.toString();
                        Message msg = new Message();
                        msg.what = 1030;
                        msg.obj = jso;
                        handler.sendMessage(msg);
                    } else {

                        Message msg = new Message();
                        msg.what = 377;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 377;
                    handler.sendMessage(msg);
                }
                return null;
            }
        }.execute();
    }
    //点赞与取消点赞
    public static void dz(final String ssid, final Handler handler){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao+"&type=dz&ssid="+ssid);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(500);
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
                        msg.what = 1020;
                        msg.obj = jso;
                        handler.sendMessage(msg);
                    } else {

                        Message msg = new Message();
                        msg.what = 377;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 377;
                    handler.sendMessage(msg);
                }
                return null;
            }
        }.execute();
    }

    //转发说说
    public static void forwardss(final String ssid, final Handler handler){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try{
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao+"&type=replace&yuanssid="+ssid);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(500);
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
                        msg.what = 1010;
                        msg.obj = jso;
                        handler.sendMessage(msg);
                    } else {

                        Message msg = new Message();
                        msg.what = 377;
                        handler.sendMessage(msg);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 377;
                    handler.sendMessage(msg);
                }
                return null;
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
                    connection.setConnectTimeout(500);
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

                        Message msg = new Message();
                        msg.what = 377;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 377;
                    handler.sendMessage(msg);
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
                    String nr = URLEncoder.encode(neirong, "UTF-8");
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao + "&type=add&contect="+nr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(500);
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
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
                        /*
                        Message msg1 = new Message();
                        msg1.what = 555;
                        msg1.obj = neirong+"///"+nr;
                        handler.sendMessage(msg1);
                        */
                    } else {

                        Message msg = new Message();
                        msg.what = 377;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 377;
                    handler.sendMessage(msg);
                }
                return null;
            }
        }.execute();
    }
    //得到所有点赞用户
    public static void getAllDZUser(final Handler handler, final String ssid){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try{
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao+"&type=selectdzuser&ssid="+ssid);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(500);
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
                        msg.what = 548;
                        msg.obj = jso;
                        handler.sendMessage(msg);
                    } else {

                        Message msg = new Message();
                        msg.what = 377;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 377;
                    handler.sendMessage(msg);
                }
                return null;
            }
        }.execute();
    }
    //得到所有评论
    public static void getAllPL(final Handler handler, final String ssid){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try{
                    URL url = new URL("http://" + dbdao.fuwuip + ":8080/qqkongjian/servlet/ShuoShuoJsonServer?MyId=" + resource.Myzhanghao+"&type=selectpl&plselecttype=selectall&ssid="+ssid);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(500);
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
                        msg.what = 5464;
                        msg.obj = jso;
                        handler.sendMessage(msg);
                    } else {

                        Message msg = new Message();
                        msg.what = 377;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 377;
                    handler.sendMessage(msg);
                }
                return null;
            }
        }.execute();
    }
}
