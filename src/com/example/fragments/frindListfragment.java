package com.example.fragments;

import java.util.ArrayList;

import com.example.copyqq.Chat;
import com.example.copyqq.R;
import comm.user;
import comm.example.tools.resource;

import android.annotation.SuppressLint;
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

@SuppressLint("ValidFragment")
public class frindListfragment extends Fragment {
	ListView frindList;
	Context ap_c = null;
	BaseAdapter frindlistadapter;
	Handler handler;

	public frindListfragment(Handler handler,
			BaseAdapter frindlistadapter) {
		this.handler = handler;
		this.frindlistadapter = frindlistadapter;
		user ceshi = new user(32);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ap_c = getActivity();
		View view = View.inflate(ap_c, R.layout.activity_frinds, null);
		resource.jieshouxiaoxi = true;
		frindList = (ListView) view.findViewById(R.id.frinds);
		frindList.setAdapter(frindlistadapter);
		frindList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				user frind = resource.frind_list.get(position);
				Intent intent = new Intent(ap_c, Chat.class);
				intent.putExtra("frind", frind);
				startActivity(intent);
			}
		});

		return view;
	}
}
