package com.example.copyqq;

import comm.Response;
import com.example.Tools.resource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements OnClickListener {

	TextView regist;
	TextView forgets;

	Button login;

	EditText username;
	EditText userpswd;

	String name = "";

	CheckBox remenber;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressLint("ShowToast")
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			//链接服务器失败码
			if (what == 122411) {
				Toast.makeText(LoginActivity.this, "链接失败", Toast.LENGTH_SHORT).show();
			} else if (what == 1) {
				//请求登录码
				Response response = (Response) msg.obj;
				//响应为空说明链接服务器失败
				if (response == null) {
					Toast.makeText(LoginActivity.this, "链接服务器失败", Toast.LENGTH_SHORT).show();
				} else if (response.getResponse().contains("成功")) {
				//登录成功
					if (remenber.isChecked()) {
					//判断是否记住用户名和密码
						SharedPreferences spf = LoginActivity.this
								.getSharedPreferences("users",
										LoginActivity.MODE_PRIVATE);
						Editor edit = spf.edit();
						edit.putString("username", username.getText()
								.toString());
						edit.putString("userpswd", userpswd.getText()
								.toString());
						edit.commit();
					} else {
					//没有记住用户名和密码，就将之前的标记删除
						SharedPreferences spf = LoginActivity.this
								.getSharedPreferences("users",
										LoginActivity.MODE_PRIVATE);
						Editor edit = spf.edit();
						edit.clear();
						edit.commit();
					}
					resource.onLine(this);

				} else {
				//登录失败
					Toast.makeText(LoginActivity.this, "登陆失败,用户名或密码错误", Toast.LENGTH_SHORT)
							.show();
				}
				//登录请求响应后设置登录按钮可点击,并设置背景
				login.setClickable(true);
				login.setBackgroundResource(R.drawable.login_buttonselector);
			} else if (what == 2) {
			//登录成功后上线响应码
				Response response = (Response) msg.obj;
				String onLine = response.getResponse();
				if (onLine.contains("成功")) {
				//上线成功则跳转到主界面
					resource.Myzhanghao = name;
					Intent intent = new Intent(LoginActivity.this,
							MainFragment.class);
					startActivity(intent);
					finish();
				} else {
				//上线失败说明账号在其他地方已经登录。
					Toast.makeText(LoginActivity.this, "登陆失败,该账号在其他地方登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		resource.jieshouxiaoxi = false;

		resource.lianjie(handler);
		regist = (TextView) findViewById(R.id.regist);
		forgets = (TextView) findViewById(R.id.forgets);
		login = (Button) findViewById(R.id.login);
		username = (EditText) findViewById(R.id.username);
		userpswd = (EditText) findViewById(R.id.userpswd);
		remenber = (CheckBox) findViewById(R.id.remenber);
		regist.setOnClickListener(this);
		forgets.setOnClickListener(this);
		login.setOnClickListener(this);

		SharedPreferences spf = LoginActivity.this.getSharedPreferences(
				"users", LoginActivity.MODE_PRIVATE);
		String name = spf.getString("username", "");
		String pswd = spf.getString("userpswd", "");
		username.setText(name);
		userpswd.setText(pswd);
		if (!"".equals(name) && !"".equals(pswd)) {
			remenber.setChecked(true);
		} else {
			remenber.setChecked(false);
		}
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int key = v.getId();
		int regist_id = regist.getId();
		int forgets_id = forgets.getId();
		int login_id = login.getId();
		if (key == regist_id) {
			//注册
			Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
			startActivity(intent);
		} else if (key == forgets_id) {
			//忘记密码
			Intent intent = new Intent(LoginActivity.this, Forgetspswd.class);
			startActivity(intent);
			
		} else if (key == login_id) {
			//登陆
			login();
		}
	}
	int back = 0;
//双击退出
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (back == 1) {
			super.onBackPressed();
		} else {
			new Thread() {
				public void run() {
					back = 1;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					back = 0;
				};
			}.start();
			Toast.makeText(LoginActivity.this, "再按一次退出", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@SuppressLint("ShowToast")
	//登录按钮的点击方法
	public void login() {
		if (resource.socket != null) {
			//用户名密码
			name = username.getText().toString();
			String pswd = userpswd.getText().toString();
			if (name != null && !"".equals(name) && pswd != null
					&& !"".equals(pswd)) {
				boolean b = name.matches("[0-9]{1,20}");
				if (b) {
					boolean c = pswd.matches("\\w{1,20}");
					if (c) {
						resource.login(name, pswd, handler);
						login.setClickable(false);
						login.setBackgroundResource(R.drawable.login_button2);
					} else {
						Toast.makeText(this, "密码不能包含非法字符", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "用户名只能是数字，且长度最长为20", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
			}
		} else {
			resource.socket = null;
			resource.lianjie(handler);
			Toast.makeText(this, "与服务器断开了，正在重连", Toast.LENGTH_SHORT).show();
		}
	}
}
