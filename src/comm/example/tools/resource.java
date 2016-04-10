package comm.example.tools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.copyqq.Chat;

import android.os.Handler;
import android.os.Message;

import comm.Request;
import comm.Response;
import comm.qq_message;
import comm.user;
import comm.dbdao.dbdao;

public class resource {
	public static Socket socket;
	public static String Myzhanghao = "";
	public static boolean jieshouxiaoxi = false;
	public static ArrayList<user> frind_list = new ArrayList<user>();
	public static HashMap<user, Chat> user_chat = new HashMap<user, Chat>();

	private resource() {

	}

	// �������ӷ�����
	public static void lianjie(final Handler handler) {
		new Thread() {
			public void run() {
				try {
					Socket socket = new Socket(dbdao.fuwuip, dbdao.qqduankou);
					resource.socket = socket;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 122411;
					handler.sendMessage(msg);
					resource.socket = null;
				}
			};
		}.start();
	}

	// ����ע��ķ���
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
						request.setZhiling("ע�����û�");
						request.setZhuceusername(username);
						request.setZhucepswd(userpswd);
						Response response = requestchuli(request, 1);
						Message msg = new Message();
						msg.what = 5;
						msg.obj = response;
						handler.sendMessage(msg);

					} catch (Exception e) {
						// TODO Auto-generated catch block
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
					request.setZhiling("ע�����û�");
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

	// ��½���ߺ�������ʱ���շ������˷�������Ϣ
	public static void jieshouxiaoxiThread(final Handler handler) {
		new Thread() {
			public void run() {
				while (jieshouxiaoxi) {
					try {
						ObjectInputStream ois = new ObjectInputStream(
								socket.getInputStream());
						Response response = (Response) ois.readObject();

						String res = response.getResponse();

						if ("�Զ������б�".equals(res)) {

							frind_list.clear();
							ArrayList<user> list = response.getFriends();

							frind_list.addAll(list);
							Message msg = new Message();
							msg.what = 3;

							handler.sendMessage(msg);

						} else if ("���߳ɹ�".equals(res)) {
							jieshouxiaoxi = false;
						} else if ("����Ϣ����".equals(res)) {
							user sendUser = response.getSendUser();
							Chat c = user_chat.get(sendUser);
							if (c != null) {

								Request request = new Request();
								request.setZhiling("��ȡδ�������¼");
								request.setMyzhanghao(resource.Myzhanghao);
								request.setDuifangzhanghao(c.frind
										.getZhanghao() + "");
								requestchuli(request, 0);

							} else {
								for (user al_frind : frind_list) {
									if (al_frind.equals(sendUser)) {
										al_frind.setHaveMassage("��");
										break;
									}
								}
								Message msg = new Message();
								msg.what = 3;
								msg.obj = "not one";
								handler.sendMessage(msg);
							}
						} else if ("����δ�������¼".equals(res)) {
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

	// ������Ϣ
	public static void sendQq_Message(qq_message q_msg) {

		Request request = new Request();
		request.setZhiling("������Ϣ");
		request.setMyzhanghao(q_msg.getSendUser_zhanghao());
		request.setDuifangzhanghao(q_msg.getReciveUser_zhanghao());
		request.setSendMassage(q_msg.getMessage());
		requestchuli(request, 0);
	}

	// ��ȡ�����б�
	public static void getfrindListdata(final Handler handler) {
		// ����һ���ؾͻ�ȡ�����б�
		new Thread() {
			public void run() {

				Request request = new Request();
				request.setZhiling("��ȡ�û��б�");
				Response response = requestchuli(request, 1);
				ArrayList<user> frinds = response.getFriends();
				frind_list.clear();
				frind_list.addAll(frinds);
				Message msg = new Message();
				msg.what = 3;
				msg.obj = "one";
				handler.sendMessage(msg);
			};
		}.start();
	}

	// �޸�����
	public static void modifyMima(final String name, final String pswd,
			final String zhanghao, final Handler handler) {
		new Thread() {
			public void run() {
				if (socket != null) {
					Request request = new Request();
					request.setZhiling("��������");
					request.setMyzhanghao(zhanghao);
					request.setZhucepswd(pswd);
					request.setZhuceusername(name);
					Response response = requestchuli(request, 1);
					Message msg = new Message();
					msg.what = 6;
					msg.obj = response;
					handler.sendMessage(msg);
				}else{
					
				}
			}
		}.start();
	}

	// ��½����
	public static void login(final String name, String pswd,
			final Handler handler) {
		if (socket != null) {
			final Request request = new Request();
			request.setZhiling("��ѯ�û��Ƿ����");
			request.setMyzhanghao(name);
			request.setMima(pswd);
			new Thread() {
				public void run() {
					Response response = requestchuli(request, 1);

					// ��½�ɹ�������Ϣ�����߳�
					Message msg = new Message();
					msg.what = 1;
					msg.obj = response;
					handler.sendMessage(msg);
				};
			}.start();
		}
	}

	// ����
	public static void onLine(final Handler handler) {
		if (socket != null) {
			new Thread() {

				public void run() {
					// ��½�ɹ������� Ҫ���������ط��Ѿ����ߣ��Ͳ�������������¼
					try {
						Request request = new Request();
						String ip = InetAddress.getLocalHost().getHostAddress();
						request.setZhiling("����");

						request.setIp(ip);
						Response response1 = requestchuli(request, 1);
						Message msg = new Message();
						msg.what = 2;
						msg.obj = response1;
						handler.sendMessage(msg);

					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
		}
	}

	// ���������������δ����Ϣ
	public static void getQq_Message(final String duifangzhanghao) {
		new Thread() {
			public void run() {
				Request request = new Request();
				request.setZhiling("��ȡδ�������¼");
				request.setMyzhanghao(Myzhanghao);
				request.setDuifangzhanghao(duifangzhanghao);
				requestchuli(request, 0);
			}
		}.start();
	}

	// �˳�qq
	public static void exitqq() {

		if (socket != null) {
			Request request = new Request();
			request.setZhiling("�ͻ����˳�");
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(resource.socket.getOutputStream());
				oos.writeObject(request);
				oos.close();
				socket.close();
				socket = null;
				Myzhanghao = "";
			} catch (IOException e) {
			}
		}

	}

	// ����
	public static void outLine() {
		new Thread() {
			public void run() {
				Request request = new Request();
				request.setZhiling("����");
				request.setMyzhanghao(resource.Myzhanghao);
				requestchuli(request, 0);
			};
		}.start();
	}

	// ���ڷ���request���󣬽���response����
	public static Response requestchuli(Request request, int type) {
		if (type == 1) {
			Response response = new Response();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				oos.writeObject(request);
				ObjectInputStream ois = new ObjectInputStream(
						socket.getInputStream());
				response = (Response) ois.readObject();
				return response;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				oos.writeObject(request);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

}
