package com.example.kehutest;

import com.example.khceshi.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragment04 extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = LayoutInflater.from(getActivity());
		View v = inflater.inflate(R.layout.fragment4, null);
		// TODO Auto-generated method stub
		return v;
	}
}
