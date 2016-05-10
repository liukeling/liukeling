package com.example.copyqq;

import java.io.IOException;

import com.dbdao.dbdao;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button login;
	Button regist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences spf = this.getSharedPreferences("users",
				LoginActivity.MODE_PRIVATE);
		if(!"null".equals(spf.getString("username", "null"))){

			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			this.finish();
		}

		try {
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int key = v.getId();
		int loginid = login.getId();
		int registid = regist.getId();
		if (key == loginid) {
			this.finish();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else if (key == registid) {
			Intent intent = new Intent(MainActivity.this, RegistActivity.class);
			startActivity(intent);
		}
	}
	
//	public void toggleMenu(View view){
//		
//	}
	
}
