package com.example.Tools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.copyqq.Chat;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import comm.Modify_groupitem;
import comm.Request;
import comm.Response;
import comm.SysInfo;
import comm.qq_message;
import comm.user;

import com.dbdao.dbdao;
import com.example.copyqq.FrindInfo_More;

public class resource {
    private static Context context;
    public static Socket socket = null;
    //储存账号
    public static String Myzhanghao = "0";
    //循环监听服务器的消息开关
    public static boolean jieshouxiaoxi = false;
    //存放需要返回消息的handler
    private static Handler linshiHandler = null;
    public static ArrayList<SysInfo> Sysinfos = new ArrayList<>();
    public static ArrayList<HashMap<HashMap<Integer, String>, user>> frinds = new ArrayList<>();
    public static HashMap<user, Chat> user_chat = new HashMap<>();
    /**
     * 创建一级条目容器
     */
    public static ArrayList<Map<String, String>> gruops = new ArrayList<Map<String, String>>();
    /**
     * 存放内容, 以便显示在列表中
     */
    public static List<List<Map<String, user>>> childs = new ArrayList<List<Map<String, user>>>();
    //储存登陆的用户
    private static user me = null;

    //不允许实例化
    private resource() {

    }

    // 用于连接服务器
    public static void lianjie(final Handler handler) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    if (resource.socket == null) {
                        Socket socket = new Socket(dbdao.fuwuip, dbdao.qqduankou);
                        resource.socket = socket;
                    } else {
                        try {
                            resource.socket.shutdownInput();
                            resource.socket.shutdownOutput();
                            resource.socket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        resource.socket = new Socket(dbdao.fuwuip, dbdao.qqduankou);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 122411;
                    handler.sendMessage(msg);
                    resource.socket = null;
                    Log.e("lianjie", "错");
                }
                return null;
            }
        }.execute();
    }

    //用于搜索好友的方法
    public static void searchFrinds(Handler handler, String search_info) {
        linshiHandler = handler;

        Request request = new Request();
        request.setZhiling("搜索好友");
        request.setRequest(search_info);
        requestchuli(request, 0);
    }

    // 用于注册的方法
    public static void regist(final String username, final String userpswd,
                              final Handler handler) {
        if (socket == null) {
            new Thread() {
                public void run() {
                    try {
                        Socket socket = new Socket(dbdao.fuwuip,
                                dbdao.qqduankou);
                        resource.socket = socket;

                        Request request = new Request();
                        request.setZhiling("注册新用户");
                        request.setZhuceusername(username);
                        request.setZhucepswd(userpswd);
                        Response response = requestchuli(request, 1);
                        Message msg = new Message();
                        msg.what = 5;
                        msg.obj = response;
                        handler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = 122411;
                        handler.sendMessage(msg);
                        resource.socket = null;
                    }
                }

                ;
            }.start();
        } else {
            new Thread() {
                public void run() {
                    Request request = new Request();
                    request.setZhiling("注册新用户");
                    request.setZhuceusername(username);
                    request.setZhucepswd(userpswd);
                    Response response = requestchuli(request, 1);
                    Message msg = new Message();
                    msg.what = 5;
                    msg.obj = response;
                    handler.sendMessage(msg);
                }

                ;
            }.start();
        }
    }

    //移动好友
    public static void moveFrind(final user frind, final int groupId, Context context) {

        resource.context = context;
        new Thread() {
            public void run() {
                Request request = new Request();
                request.setZhiling("移动好友");
                request.setObj(frind);
                request.setGroupId(groupId);
                requestchuli(request, 0);
            }
        }.start();
    }

    // 登陆上线后用于随时接收服务器端发来的消息
    public static void jieshouxiaoxiThread(final Handler handler) {
        new Thread() {
            public void run() {

                responseUser(Integer.parseInt(Myzhanghao), 0);

                while (jieshouxiaoxi) {
                    try {
                        ObjectInputStream ois = new ObjectInputStream(
                                socket.getInputStream());
                        final Response response = (Response) ois.readObject();

                        final String res = response.getResponse();

                        if ("下线成功".equals(res)) {
                            responsechuli(res, response, handler);
                        } else {
                            Thread thread = new Thread() {
                                public void run() {
                                    responsechuli(res, response, handler);
                                }
                            };
                            thread.start();
                        }

                    } catch (Exception e) {
                        jieshouxiaoxi = false;
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();
    }

    private static void responsechuli(String res, final Response response, Handler handler) {
        if ("自动更新列表".equals(res)) {
            Object obj = response.getObj();
            frinds.clear();
            frinds.addAll((ArrayList<HashMap<HashMap<Integer, String>, user>>) obj);

            Message msg = new Message();
            msg.what = 3;

            handler.sendMessage(msg);

        } else if ("下线成功".equals(res)) {
            jieshouxiaoxi = false;
        } else if ("有消息来了".equals(res)) {
            user sendUser = response.getSendUser();
            Chat c = user_chat.get(sendUser);
            if (c != null) {

                Request request = new Request();
                request.setZhiling("获取未读聊天记录");
                request.setMyzhanghao(resource.Myzhanghao);
                request.setDuifangzhanghao(c.frind
                        .getZhanghao() + "");
                requestchuli(request, 0);

            } else {

                for (HashMap<HashMap<Integer, String>, user> hm : frinds) {
                    for (user u : hm.values()) {
                        if (u != null) {
                            if (u.equals(sendUser)) {
                                u.setHaveMassage("是");
                                break;
                            }
                        }
                    }
                }
                Message msg = new Message();
                msg.what = 3;
                msg.obj = "not one";
                handler.sendMessage(msg);
            }
        } else if ("给你未读聊天记录".equals(res)) {
            ArrayList<qq_message> al = (ArrayList<qq_message>) response.getObj();

            Chat c = user_chat.get(response.getSendUser());
            if (c != null) {
                Message msg = new Message();
                msg.what = 4;
                msg.obj = al;
                c.handler.sendMessage(msg);
            }
        } else if ("用户信息".equals(res)) {
            Message msg = new Message();
            if (Integer.parseInt(Myzhanghao) == response
                    .getResponseUser().getZhanghao()) {
                me = response.getResponseUser();
            } else {
                msg.what = 71;
                msg.obj = response.getResponseUser();
                handler.sendMessage(msg);
            }
        } else if ("搜索好友的结果".equals(res)) {
            if (linshiHandler != null) {
                Message msg = new Message();
                msg.obj = response.getObj();
                msg.what = 8;
                linshiHandler.sendMessage(msg);
            }
        } else if ("添加好友请求结果".equals(res)) {
            final boolean ok = (boolean) response.getObj();
            if (context != null) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ok) {
                            Toast.makeText(context, "请求已经发送", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "请求发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else if ("有系统消息".equals(res)) {
            //更新系统消息
            reflushSystemInfo(0);
        } else if ("所有系统消息".equals(res)) {
            ArrayList<SysInfo> al = (ArrayList<SysInfo>) response.getObj();
            Message msg = new Message();
            msg.what = 9;
            msg.obj = al;
            handler.sendMessage(msg);
        } else if ("好友请求回复完成".equals(res)) {
            Message msg = new Message();
            msg.what = 10;
            msg.obj = response.getObj();
            handler.sendMessage(msg);
        } else if ("删除好友成功".equals(res)) {
            String sys = (String) response.getObj();
            if ("添加系统消息失败".equals(sys)) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "删除成功, 但通知对方失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                }
            });
        } else if ("删除好友失败".equals(res)) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else if ("修改分组信息".equals(res)) {
            Message msg = new Message();
            msg.what = 11;
            msg.obj = response.getObj();
            linshiHandler.sendMessage(msg);
        } else if ("移动好友结果".equals(res)) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ((Boolean) response.getObj()) {
                        Toast.makeText(context, "移动成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "移动失败", Toast.LENGTH_SHORT).show();
                    }
                    if (context instanceof FrindInfo_More) {
                        ((FrindInfo_More) context).setGroupName();
                    }
                }
            });
        } else if ("消息记录".equals(res)) {
            ArrayList<qq_message> al = (ArrayList<qq_message>) response.getObj();

            Message msg = new Message();
            msg.what = 1231;
            msg.obj = al;
            linshiHandler.sendMessage(msg);
        }else if("刷新好友列表成功".equals(res)){
            Message msg = new Message();
            msg.what = 1232;
            handler.sendMessage(msg);
        }
    }

    //更新系统消息
    public static Response reflushSystemInfo(int type) {
        Request request = new Request();
        request.setZhiling("获取系统消息");
        if (type == 0) {
            requestchuli(request, type);
            return null;
        } else {
            Response response = requestchuli(request, type);
            return response;
        }
    }

    public static void getRecord(final String zhanghao, final int id, Handler handler) {
        linshiHandler = handler;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Request request = new Request();
                request.setZhiling("获取消息记录");
                request.setDuifangzhanghao(zhanghao);
                request.setObj(id);
                requestchuli(request, 0);
                return null;
            }
        }.execute();
    }

    //将系统消息标记为已读
    public static void setSysinfoRead(SysInfo sinfo) {
        Request request = new Request();
        request.setZhiling("将系统消息标记为已读");
        request.setObj(sinfo);
        requestchuli(request, 0);
    }

    // 发送消息
    public static void sendQq_Message(qq_message q_msg) {

        Request request = new Request();
        request.setZhiling("发送消息");
        request.setMyzhanghao(q_msg.getSendUser_zhanghao());
        request.setDuifangzhanghao(q_msg.getReciveUser_zhanghao());
        request.setSendMassage(q_msg.getMessage());
        requestchuli(request, 0);
    }

    //刷新好友列表
    public static void reflushFrindList(){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                Request request = new Request();
                request.setZhiling("刷新好友列表");
                requestchuli(request, 0);
                return null;
            }
        }.execute();
    }

    // 获取好友列表
    public static void getfrindListdata(final Handler handler) {
        // 用于一加载就获取好友列表与更新系统消息
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Request request = new Request();
                request.setZhiling("获取用户列表");
                Response response = requestchuli(request, 1);
                Response response1 = resource.reflushSystemInfo(1);


                Sysinfos.clear();
                Sysinfos.addAll((ArrayList<SysInfo>) response1.getObj());

                frinds.clear();
                frinds.addAll((ArrayList<HashMap<HashMap<Integer, String>, user>>) response.getObj());
                Message msg = new Message();
                msg.what = 3;
                msg.obj = "one";
                handler.sendMessage(msg);
                return null;
            }
        }.execute();

    }

    //分组管理
    public static void groupManager(final Modify_groupitem modifys, Handler handler) {
        resource.linshiHandler = handler;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Request request = new Request();
                request.setZhiling("分组管理");
                request.setObj(modifys);
                requestchuli(request, 0);
                return null;
            }
        }.execute();

    }

    //添加好友请求的回复
    public static void addFrindResponse(int type, SysInfo sinfo, int fenzu_item) {
        Request request = new Request();
        request.setZhiling("添加为好友的回复");
        request.setDuifangzhanghao(sinfo.getReleaseuser());
        ArrayList<Object> al = new ArrayList<>();

        switch (type) {
            case 0:
                //同意
                if (fenzu_item != -1) {
                    al.add(sinfo);
                    al.add(true);
                    request.setObj(al);
                } else {
                    //分组不明确
                    al.add(sinfo);
                    al.add(false);
                    request.setObj(al);
                }
                break;
            case 1:
                //拒绝
                al.add(sinfo);
                al.add(false);
                request.setObj(al);
                break;
        }
        al.add(fenzu_item);
        requestchuli(request, 0);
    }

    // 修改密码
    public static void modifyMima(final String name, final String pswd,
                                  final String zhanghao, final Handler handler) {
        new Thread() {
            public void run() {
                if (socket != null) {
                    Request request = new Request();
                    request.setZhiling("更改密码");
                    request.setMyzhanghao(zhanghao);
                    request.setZhucepswd(pswd);
                    request.setZhuceusername(name);
                    Response response = requestchuli(request, 1);
                    Message msg = new Message();
                    msg.what = 6;
                    msg.obj = response;
                    handler.sendMessage(msg);
                } else {

                }
            }
        }.start();
    }

    // 登陆请求
    public static void login(final String name, String pswd,
                             final Handler handler) {
        if (socket != null) {
            final Request request = new Request();
            request.setZhiling("查询用户是否存在");
            request.setMyzhanghao(name);
            request.setMima(pswd);
            new Thread() {
                public void run() {
                    Response response = requestchuli(request, 1);

                    // 登陆成功后发送信息给主线程
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = response;
                    handler.sendMessage(msg);
                }

                ;
            }.start();
        }
    }

    // 上线
    public static void onLine(final Handler handler) {
        if (socket != null) {
            new Thread() {

                public void run() {
                    // 登陆成功后上线 要是在其他地方已经上线，就不会让他继续登录
                    try {
                        Request request = new Request();
                        String ip = InetAddress.getLocalHost().getHostAddress();
                        request.setZhiling("上线");

                        request.setIp(ip);
                        Response response1 = requestchuli(request, 1);
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = response1;
                        handler.sendMessage(msg);

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }

                ;
            }.start();
        }
    }

    // 用于向服务器请求未读消息
    public static void getQq_Message(final String duifangzhanghao) {
        new Thread() {
            public void run() {
                Request request = new Request();
                request.setZhiling("获取未读聊天记录");
                request.setMyzhanghao(Myzhanghao);
                request.setDuifangzhanghao(duifangzhanghao);
                requestchuli(request, 0);
            }
        }.start();
    }


    // 退出qq
    public static void exitqq() {

        if (socket != null) {
            Request request = new Request();
            request.setZhiling("客户端退出");
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(resource.socket.getOutputStream());
                oos.writeObject(request);
                oos.flush();
                oos.close();
                socket.close();
                Myzhanghao = "";
            } catch (IOException e) {
            }
        }
    }

    //发送添加好友请求
    public static void sendAddFrindRequest(String duifangzhanghao, int fenzuitem, Context context) {
        if (socket != null) {
            Request request = new Request();
            request.setZhiling("添加好友_发送");
            request.setDuifangzhanghao(duifangzhanghao);
            request.setObj(fenzuitem);
            requestchuli(request, 0);
            resource.context = context;
        }
    }

    // 下线
    public static void outLine() {
        new Thread() {
            public void run() {
                Request request = new Request();
                request.setZhiling("下线");
                request.setMyzhanghao(resource.Myzhanghao);
                requestchuli(request, 0);
            }

            ;
        }.start();
    }

    // 用于发送request对象，接受response对象
    public static Response requestchuli(Request request, int type) {
        if (type == 1) {
            Response response = new Response();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(
                        socket.getOutputStream());
                oos.writeObject(request);
                oos.flush();
                ObjectInputStream ois = new ObjectInputStream(
                        socket.getInputStream());
                response = (Response) ois.readObject();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(
                        socket.getOutputStream());
                oos.writeObject(request);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static user getMe() {
        return me;
    }

    //用于删除好友
    public static void delFrind(final user frind, final Context context) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                Request request = new Request();
                request.setZhiling("删除好友");
                request.setObj(frind);
                requestchuli(request, 0);
                resource.context = context;
                return null;
            }
        }.execute();
    }

    // 用于从网络请求获取用户信息
    public static void responseUser(final int zhanghao, final int type) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                Request request = new Request();
                request.setZhiling("获取用户信息");
                request.setMyzhanghao(zhanghao + "");

                requestchuli(request, type);

                return null;
            }
        }.execute();
    }

}
