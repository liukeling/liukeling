package com.example.copyqq;

import comm.Response;
import comm.example.tools.resource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class forgetspswd extends Activity {
	EditText username;
	EditText userzhanghao;
	EditText userpswd;
	Button forgets;

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 6){
				Response response = (Response) msg.obj;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgiets_activity);
		username = (EditText) findViewById(R.id.username);
		userzhanghao = (EditText) findViewById(R.id.userzhanghao);
		userpswd = (EditText) findViewById(R.id.usernewpswd);
		forgets = (Button) findViewById(R.id.forgetbutton);
		forgets.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast") @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = username.getText().toString();
				String zhanghao = userzhanghao.getText().toString();
				String pswd = userpswd.getText().toString();
				if (name != null && !"".equals(name) && zhanghao != null
						&& !"".equals(zhanghao) && pswd != null && !"".equals(pswd)){
					resource.modifyMima(name, pswd, zhanghao,handler);
				}else{
					Toast.makeText(forgetspswd.this, "�û��������뼰�˺Ų���Ϊ��", 1);
				}
			}

		});
	}
}
