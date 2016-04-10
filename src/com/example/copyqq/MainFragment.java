package com.example.copyqq;

import com.example.fragments.frindListfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainFragment extends FragmentActivity implements
		OnCheckedChangeListener {

	Context ap_c = null;

	int checkfragment = 1;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		ap_c = MainFragment.this;
		View view = View.inflate(ap_c, R.layout.mainfragment_layout, null);

		RadioGroup rg = (RadioGroup) view.findViewById(R.id.radioG);

		rg.setOnCheckedChangeListener(this);
		setContentView(view);

		rg.check(R.id.radiob1);

	}

	public void setFragment() {
		Fragment fra = null;
		switch (checkfragment) {
		case 1:
			fra = new frindListfragment();
			break;
		case 2:

			break;
		case 3:

			break;
		}
		if (fra != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.frame, fra).commit();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.radiob1:
			checkfragment = 1;
			setFragment();
			break;
		case R.id.radiob2:
			checkfragment = 2;
			break;
		case R.id.radiob3:
			checkfragment = 3;
			break;
		}
	}
}
