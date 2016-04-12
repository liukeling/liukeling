package com.example.copyqq;

import java.util.ArrayList;

import com.example.fragments.frindListfragment;
import comm.user;
import comm.example.tools.resource;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainFragment extends FragmentActivity implements
		OnCheckedChangeListener {

	Context ap_c = null;

	int checkfragment = 1;
	ArrayList<user> frinds = new ArrayList<user>();
	Myadapter frindlistadapter = new Myadapter();
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 3) {
				frinds.clear();
				ArrayList<user> list = resource.frind_list;
				frinds.addAll(list);
				frindlistadapter.notifyDataSetChanged();
				if ("one".equals(msg.obj)) {
					resource.jieshouxiaoxiThread(handler);
				}
			}
		};
	};

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

		resource.getfrindListdata(handler);

	}

	public void setFragment() {
		Fragment fra = null;
		switch (checkfragment) {
		case 1:
			fra = new frindListfragment(handler, frindlistadapter);
			
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
	private class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return frinds.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return frinds.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View v = View.inflate(ap_c, R.layout.item_frindlist, null);

			TextView tv = (TextView) v.findViewById(R.id.frindinfo);

			user frind = frinds.get(position);
			String name = frind.getName();
			String zhuangtai = frind.getZhuangtai();
			String xiaoxi = frind.getHaveMassage();

			if (xiaoxi.contains("·ñ")) {
				xiaoxi = "";
			} else {
				xiaoxi = "ÓÐÏûÏ¢";
			}

			tv.setText(name + "  " + zhuangtai + "  " + xiaoxi);

			return v;
		}

	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		resource.outLine();
		super.onDestroy();
	}
}
