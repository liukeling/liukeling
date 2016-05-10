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
			if (what == 122411) {
				Toast.makeText(LoginActivity.this, "链接失败", Toast.LENGTH_SHORT).show();
			} else if (what == 1) {
				Response response = (Response) msg.obj;
				if (response == null) {
					Toast.makeText(LoginActivity.this, "链接服务器失败", Toast.LENGTH_SHORT).show();
				} else if (response.getResponse().contains("成功")) {

					if (remenber.isChecked()) {
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
						SharedPreferences spf = LoginActivity.this
								.getSharedPreferences("users",
										LoginActivity.MODE_PRIVATE);
						Editor edit = spf.edit();
						edit.clear();
						edit.commit();
					}
					resource.onLine(this);

				} else {
					Toast.makeText(LoginActivity.this, "登陆失败,用户名或密码错误", Toast.LENGTH_SHORT)
							.show();
				}
				login.setClickable(true);
				login.setBackgroundResource(R.drawable.login_buttonselector);
			} else if (what == 2) {
				Response response = (Response) msg.obj;
				String onLine = response.getResponse();
				if (onLine.contains("成功")) {
					resource.Myzhanghao = name;
					Intent intent = new Intent(LoginActivity.this,
							MainFragment.class);
					startActivity(intent);
				} else {
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
			Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
			startActivity(intent);
		} else if (key == forgets_id) {
			
			Intent intent = new Intent(LoginActivity.this, Forgetspswd.class);
			startActivity(intent);
			
		} else if (key == login_id) {
			login();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		resource.exitqq();
		super.onDestroy();
	}

	int back = 0;

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
	public void login() {
		if (resource.socket != null) {
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
