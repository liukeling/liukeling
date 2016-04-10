package com.example.fragments;

import java.util.ArrayList;

import com.example.copyqq.Chat;
import com.example.copyqq.R;
import comm.user;
import comm.example.tools.resource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class frindListfragment extends Fragment {
	Context ap_c = null;
	ListView frindList;
	Myadapter frindlistadapter;

	private ArrayList<user> frinds;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.e("ceshi", "ceshi"+frinds.size());
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ap_c = getActivity();
		View view = View.inflate(ap_c, R.layout.activity_frinds, null);
		frinds = new ArrayList<user>();
		frindlistadapter = new Myadapter();
		resource.jieshouxiaoxi = true;
		frindList = (ListView) view.findViewById(R.id.frinds);
		frindList.setAdapter(frindlistadapter);
		frindList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				user frind = frinds.get(position);
				Intent intent = new Intent(ap_c, Chat.class);
				intent.putExtra("frind", frind);
				startActivity(intent);
			}
		});
		resource.getfrindListdata(handler);

		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		resource.outLine();
		super.onDestroy();
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
}
