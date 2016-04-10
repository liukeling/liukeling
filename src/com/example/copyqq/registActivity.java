package com.example.copyqq;

import comm.Response;
import comm.user;
import comm.example.tools.resource;

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
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class registActivity extends Activity {
	private EditText username;
	private EditText userpswd;
	private CheckBox agree;
	private Button registbutton;
	private String name;
	private ProgressDialog registprogress;
	@SuppressLint("ShowToast") Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 5) {
				Response response = (Response) msg.obj;
				if (response != null && "ע��ɹ�".equals(response.getResponse())) {
					user newuser = response.getResponseUser();
					int zhanghao = newuser.getZhanghao();
					if (zhanghao != 0 && zhanghao != -1) {
						AlertDialog dialog = new AlertDialog.Builder(
								registActivity.this)
								.setTitle("ע��ɹ�")
								.setCancelable(false)
								.setMessage(
										"��ϲ " + name + " ע��ɹ������ĵ�½�˺�Ϊ��"
												+ newuser.getZhanghao())
								.setPositiveButton(
										"ȷ��",
										new android.content.DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												registActivity.this.finish();
												Intent intent = new Intent(
														registActivity.this,
														loginActivity.class);
												startActivity(intent);
											}
										}).create();
						dialog.show();
					} else {
						Toast.makeText(registActivity.this, "ע��ʧ��", 1).show();
					}
				}else{
					Toast.makeText(registActivity.this, "ע��ʧ��", 1).show();
				}

				registprogress.dismiss();
			}else if(msg.what == 122411){
				Toast.makeText(registActivity.this, "���ӷ�����ʧ��", 1).show();
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
						registprogress = new ProgressDialog(registActivity.this);
						registprogress.setTitle("�ȴ�");
						registprogress.setMessage("����ע��");
						registprogress.setCancelable(false);
						registprogress.show();

					} else {
						Toast.makeText(registActivity.this, "���뺬�зǷ��ַ�", 1)
								.show();
					}
				} else {
					Toast.makeText(registActivity.this, "�û��������벻��Ϊ��", 1).show();
				}
			}
		});
		registbutton.setClickable(false);
	}
}
