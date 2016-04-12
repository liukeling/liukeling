package com.example.copyqq;

import java.util.ArrayList;

import comm.user;
import comm.example.tools.resource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("HandlerLeak") public class Myfrinds extends Activity {

	ListView frindList;
	Myadapter frindlistadapter = new Myadapter();

	private ArrayList<user> frinds = new ArrayList<user>();
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		resource.jieshouxiaoxi = true;
		setContentView(R.layout.activity_frinds);
		frindList = (ListView) findViewById(R.id.frinds);
		frindList.setAdapter(frindlistadapter);
		frindList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				user frind = frinds.get(position);
				Intent intent = new Intent(Myfrinds.this, Chat.class);
				intent.putExtra("frind", frind);
				startActivity(intent);
			}
		});
		resource.getfrindListdata(handler);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "destroy", 1).show();
		resource.outLine();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	class Myadapter extends BaseAdapter {

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

			LayoutInflater inflater = LayoutInflater.from(Myfrinds.this);
			View v = inflater.inflate(R.layout.item_frindlist, null);

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
}
