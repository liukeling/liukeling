package com.example.Tools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.copyqq.AddFrind_activity;
import com.example.copyqq.Chat;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import comm.Request;
import comm.Response;
import comm.SysInfo;
import comm.qq_message;
import comm.user;
import com.dbdao.dbdao;

public class resource {
	private static Context context;
	public static Socket socket;
	public static String Myzhanghao = "0";
	public static boolean jieshouxiaoxi = false;
	private static Handler linshiHandler = null;
	public static ArrayList<SysInfo> Sysinfos = new ArrayList<>();
	public static ArrayList<HashMap<HashMap<Integer, String>, user>> frinds = new ArrayList<HashMap<HashMap<Integer, String>, user>>();
	public static HashMap<user, Chat> user_chat = new HashMap<user, Chat>();
	//用于储存好友，根据分组编号和二级目录编号来得到好友对象
	public static HashMap<HashMap<Integer, Integer>, user> frindList = new HashMap<HashMap<Integer, Integer>, user>();
	/**
	 * 创建一级条目容器
	 */
	public static List<Map<String, String>> gruops = new ArrayList<Map<String, String>>();
	/**
	 * 存放内容, 以便显示在列表中
	 */
	public static List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
	//储存登陆的用户
	private static user me = null;

	private resource() {

	}

	public static ArrayList<HashMap<HashMap<Integer, String>, user>> getLinshiobj() {
		return frinds;
	}

	// 用于连接服务器
	public static void lianjie(final Handler handler) {
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {

				try {
					if(resource.socket == null) {
						Socket socket = new Socket(dbdao.fuwuip, dbdao.qqduankou);
						resource.socket = socket;
					}else{
						resource.socket.shutdownInput();
						resource.socket.shutdownOutput();
						resource.socket.close();
						resource.socket = new Socket(dbdao.fuwuip, dbdao.qqduankou);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 122411;
					handler.sendMessage(msg);
					resource.socket = null;
				}
				return null;
			}
		}.execute();
	}

	//TODO 用于搜索好友的方法
	public static void searchFrinds(Handler handler, String search_info){
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
				};
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
				};
			}.start();
		}
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

						if("下线成功".equals(res)) {
							responsechuli(res, response, handler);
						}else{
							Thread thread = new Thread(){
								public void run(){
									responsechuli(res, response, handler);
								}
							};
							thread.start();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						jieshouxiaoxi = false;
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	private static void responsechuli(String res, Response response, Handler handler){
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
						if (u.equals(sendUser)) {
							u.setHaveMassage("是");
							break;
						}
					}
				}
				Message msg = new Message();
				msg.what = 3;
				msg.obj = "not one";
				handler.sendMessage(msg);
			}
		} else if ("给你未读聊天记录".equals(res)) {
			ArrayList<String> jilus = response
					.getLiaotianjilu();
			ArrayList<qq_message> al = new ArrayList<qq_message>();
			for (String message : jilus) {
				qq_message q_msg = new qq_message();
				q_msg.setMessage(message);
				q_msg.setSendUser_zhanghao(response
						.getSendUser().getZhanghao() + "");
				al.add(q_msg);
			}

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
		}else if("搜索好友的结果".equals(res)){
			//TODO
			if(linshiHandler != null){
				Message msg = new Message();
				msg.obj = response.getObj();
				msg.what = 8;
				linshiHandler.sendMessage(msg);
			}
		}else if("添加好友请求结果".equals(res)){
			final boolean ok = (boolean) response.getObj();
			if(context != null){
				((Activity)context).runOnUiThread(new Runnable() {
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
		}else if("有系统消息".equals(res)){
			//更新系统消息
//			Log.e("ceshi", "xitongxiaoxi");
			reflushSystemInfo(0);
		}else if("所有系统消息".equals(res)){
			ArrayList<SysInfo> al = (ArrayList<SysInfo>) response.getObj();
			Message msg = new Message();
			msg.what = 9;
			msg.obj = al;
			handler.sendMessage(msg);
		}else if("好友请求回复完成".equals(res)){
			Message msg = new Message();
			msg.what = 10;
			msg.obj = response.getObj();
			handler.sendMessage(msg);
		}else if("删除好友成功".equals(res)){
			String sys = (String) response.getObj();
			if("添加系统消息失败".equals(sys)){
				 // TODO
			}

			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
				}
			});
		}else if("删除好友失败".equals(res)){
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	//更新系统消息
	public static Response reflushSystemInfo(int type){
		Request request = new Request();
		request.setZhiling("获取系统消息");
		if(type == 0) {
			requestchuli(request, type);
			return null;
		}else{
			Response response = requestchuli(request, type);
			return response;
		}
	}
	//将系统消息标记为已读
	public static void setSysinfoRead(SysInfo sinfo){
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

	// 获取好友列表
	public static void getfrindListdata(final Handler handler) {
		// 用于一加载就获取好友列表与更新系统消息
		new Thread() {
			public void run() {

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
			};
		}.start();
	}

	//添加好友请求的回复
	public static void addFrindResponse(int type, SysInfo sinfo, int fenzu_item){
		Request request = new Request();
		request.setZhiling("添加为好友的回复");
		request.setDuifangzhanghao(sinfo.getReleaseuser());
		ArrayList<Object> al = new ArrayList<>();

		switch (type){
			case 0:
				//同意
				if(fenzu_item != -1) {
					al.add(sinfo);
					al.add(true);
					request.setObj(al);
				}else{
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
				};
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
				};
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
				socket = null;
				Myzhanghao = "";
			} catch (IOException e) {
			}
		}

	}

	//发送添加好友请求
	public static void sendAddFrindRequest(String duifangzhanghao, int fenzuitem, Context context){
		if(socket != null){
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
			};
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
	public static void delFrind(final user frind, final Context context){

		new AsyncTask<Void, Void, Void>(){

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
				// TODO Auto-generated method stub

				Request request = new Request();
				request.setZhiling("获取用户信息");
				request.setMyzhanghao(zhanghao + "");

				requestchuli(request, type);
				// TODO

				return null;
			}
		}.execute();
	}

}
