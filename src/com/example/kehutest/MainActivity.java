package com.example.kehutest;

import com.example.khceshi.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements OnClickListener {

	Button b1;
	Button b2;
	Button b3;
	Button b4;
	Button b5;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        setOnclike();
		fragment01 f1 = new fragment01();
		click(b1, f1);
    }
    public void setOnclike(){
    	b1.setOnClickListener(this);
    	b2.setOnClickListener(this);
    	b3.setOnClickListener(this);
    	b4.setOnClickListener(this);
    	b5.setOnClickListener(this);
    }
    
    @SuppressLint("Recycle")
    public void click(Button button, Fragment f){

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		ft.replace(R.id.fl, f);
		ft.commit();
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			fragment01 f1 = new fragment01();
			click(b1, f1);
			break;
		case R.id.button2:

			fragment02 f2 = new fragment02();
			click(b1, f2);
			break;
		case R.id.button3:

			fragment03 f3 = new fragment03();
			click(b1, f3);
			break;
		case R.id.button4:

			fragment04 f4 = new fragment04();
			click(b1, f4);
			break;
		case R.id.button5:

			fragment05 f5 = new fragment05();
			click(b1, f5);
			break;
		}
	}
}
