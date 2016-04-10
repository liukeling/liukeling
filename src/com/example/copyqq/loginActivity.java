package com.example.copyqq;

import comm.Response;
import comm.example.tools.resource;

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

@SuppressLint("HandlerLeak") public class loginActivity extends Activity implements OnClickListener {

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
				Toast.makeText(loginActivity.this, "链接失败", 1).show();
			} else if (what == 1) {
				Response response = (Response) msg.obj;
				if (response == null) {
					Toast.makeText(loginActivity.this, "链接服务器失败", 1).show();
				} else if (response.getResponse().contains("成功")) {

					if (remenber.isChecked()) {
						SharedPreferences spf = loginActivity.this
								.getSharedPreferences("users",
										loginActivity.MODE_PRIVATE);
						Editor edit = spf.edit();
						edit.putString("username", username.getText().toString());
						edit.putString("userpswd", userpswd.getText().toString());
						edit.commit();
					}else{
						SharedPreferences spf = loginActivity.this
								.getSharedPreferences("users",
										loginActivity.MODE_PRIVATE);
						Editor edit = spf.edit();
						edit.clear();
						edit.commit();
					}
					resource.onLine(this);

				} else {
					Toast.makeText(loginActivity.this, "登陆失败,用户名或密码错误", 1).show();
				}
			}else if(what == 2){
				Response response = (Response) msg.obj;
				String onLine = response.getResponse();
				if(onLine.contains("成功")){
					resource.Myzhanghao = name;
					Intent intent = new Intent(loginActivity.this, Myfrinds.class);
					startActivity(intent);
				}else{
					Toast.makeText(loginActivity.this, "登陆失败,该账号在其他地方登录", 1).show();
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

		SharedPreferences spf = loginActivity.this
				.getSharedPreferences("users",
						loginActivity.MODE_PRIVATE);
		String name = spf.getString("username", "");
		String pswd = spf.getString("userpswd", "");
		username.setText(name);
		userpswd.setText(pswd);
		if(!"".equals(name) && !"".equals(pswd)){
			remenber.setChecked(true);
		}else{
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
			Intent intent = new Intent(loginActivity.this, registActivity.class);
			startActivity(intent);
		} else if (key == forgets_id) {
			
			
			
			Toast.makeText(this, "忘记密码", 1).show();
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
					} else {
						Toast.makeText(this, "密码不能包含非法字符", 1).show();
					}
				} else {
					Toast.makeText(this, "用户名只能是数字，且长度最长为20", 1).show();
				}
			} else {
				Toast.makeText(this, "用户名或密码不能为空", 1).show();
			}
		} else {
			resource.lianjie(handler);
			Toast.makeText(this, "与服务器断开了，正在重连", 1).show();
		}
	}
}
