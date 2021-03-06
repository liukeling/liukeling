package com.example.copyqq;

import comm.Response;
import comm.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkl.socketlibrary.tools.resource;

@SuppressLint("HandlerLeak") public class RegistActivity extends Activity {
	private EditText username;
	private EditText userpswd;
	private CheckBox agree;
	private Button registbutton;
	private String name;
	private ProgressDialog registprogress;
	private TextView tv_back;
	@SuppressLint("ShowToast") Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 5) {
				Response response = (Response) msg.obj;
				if (response != null && "注册成功".equals(response.getResponse())) {
					user newuser = response.getResponseUser();
					int zhanghao = newuser.getZhanghao();
					if (zhanghao != 0 && zhanghao != -1) {
						AlertDialog dialog = new AlertDialog.Builder(
								RegistActivity.this)
								.setTitle("注册成功")
								.setCancelable(false)
								.setMessage(
										"恭喜 " + name + " 注册成功，您的登陆账号为："
												+ newuser.getZhanghao())
								.setPositiveButton(
										"确定",
										new android.content.DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												RegistActivity.this.finish();
												Intent intent = new Intent(
														RegistActivity.this,
														LoginActivity.class);
												startActivity(intent);
											}
										}).create();
						dialog.show();
					} else {
						Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
				}

				registprogress.dismiss();
			}else if(msg.what == 122411){
				Toast.makeText(RegistActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
			}
		};
	};

	@SuppressLint("ShowToast") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist_layout);

		username = (EditText) findViewById(R.id.username);
		userpswd = (EditText) findViewById(R.id.userpswd);
		agree = (CheckBox) findViewById(R.id.agree);
		registbutton = (Button) findViewById(R.id.registButton);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					registbutton.setClickable(true);
				} else {
					registbutton.setClickable(false);
				}
			}
		});
		registbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = username.getText().toString();
				String pswd = userpswd.getText().toString();
				if (name != null && pswd != null && !"".equals(name)
						&& !"".equals(pswd)) {
					if (pswd.matches("\\w{1,20}")) {
						resource.regist(name, pswd, handler);
						registprogress = new ProgressDialog(RegistActivity.this);
						registprogress.setTitle("等待");
						registprogress.setMessage("正在注册");
						registprogress.setCancelable(false);
						registprogress.show();

					} else {
						Toast.makeText(RegistActivity.this, "密码含有非法字符", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(RegistActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
				}
			}
		});
		registbutton.setClickable(false);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(this, LoginActivity.class));
	}
}
