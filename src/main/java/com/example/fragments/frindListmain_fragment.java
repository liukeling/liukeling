package com.example.fragments;

import java.util.HashMap;

import com.example.MyViews.MyExpandableListView;
import com.example.copyqq.Chat;
import com.example.copyqq.R;
import comm.user;
import com.example.Tools.resource;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class FrindListmain_fragment extends Fragment {
	MyExpandableListView frindList;
	Context ap_c = null;
	public SimpleExpandableListAdapter frindlistadapter;
	Handler handler;

	public FrindListmain_fragment(Handler handler,
								  SimpleExpandableListAdapter frindlistadapter) {
		this.handler = handler;
		this.frindlistadapter = frindlistadapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ap_c = getActivity();
		View view = View.inflate(ap_c, R.layout.activity_frinds, null);
		resource.jieshouxiaoxi = true;
		frindList = (MyExpandableListView) view.findViewById(R.id.frinds);
		frindList.setGroupIndicator(null);
		frindList.setAdapter(frindlistadapter);


		frindList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(ap_c, ""+position, Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		frindList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				HashMap<Integer, Integer> jilu = new HashMap<Integer, Integer>();
				jilu.put(groupPosition, childPosition);
				user u = resource.frindList.get(jilu);
				if (u != null) {
					Intent intent = new Intent(FrindListmain_fragment.this
							.getActivity(), Chat.class);
					intent.putExtra("frind", u);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(),
							"null:" + groupPosition + ":::" + childPosition, Toast.LENGTH_SHORT)
							.show();
				}
				return false;
			}
		});

		return view;
	}
}
