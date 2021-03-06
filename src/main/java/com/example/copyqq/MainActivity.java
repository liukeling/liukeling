package com.example.copyqq;

import java.io.IOException;

import com.example.Tools.IdArray;
import com.example.lkl.socketlibrary.dbdao.dbdao;
import com.example.lkl.socketlibrary.tools.resource;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button login;
	Button regist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(resource.Myzhanghao != null && !resource.Myzhanghao.equals("0") && !resource.Myzhanghao.equals("")){
			Intent intent = new Intent(MainActivity.this,
					MainFragment.class);
			startActivity(intent);
			finish();
		}else {
			resource.init();
			IdArray.init(MainActivity.this);
			//用于判断是否之前记住过用户名和密码
			if (IdArray.getSize() >= 1) {

				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				this.finish();
			}

			try {
				//初始化配置
				dbdao.init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setContentView(R.layout.activity_main);
			login = (Button) findViewById(R.id.login);
			regist = (Button) findViewById(R.id.regist);
			login.setOnClickListener(this);
			regist.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int key = v.getId();
		int loginid = login.getId();
		int registid = regist.getId();
		if (key == loginid) {
			//登陆
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else if (key == registid) {
			//注册
			Intent intent = new Intent(MainActivity.this, RegistActivity.class);
			startActivity(intent);
		}
		this.finish();
	}

}
