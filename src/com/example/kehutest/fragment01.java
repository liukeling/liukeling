package com.example.kehutest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.MyViews.MyGridView;
import com.example.SQLite.AppContext;
import com.example.SQLite.sqliteUtils;
import com.example.khceshi.R;
import com.example.shujufangwen.getdata;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class fragment01 extends Fragment {

	MyGridView myGridView;
	List<data> myGridviewdata = new ArrayList<data>();
	MyAdapter gridViewadapter = new MyAdapter();
	EditText ip_et;
	Button getipbutton;
	List<Handler> handlerzis = new ArrayList<Handler>();
	boolean isflushdata = true;
	Handler getdatahandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String jon = (String) msg.obj;
				myGridviewdata.clear();
				try {
					JSONObject job = new JSONObject(jon);
					Iterator iterator = job.keys();
					for (int i = 0; i < job.length(); i++) {
						String key = (String) iterator.next();
						data data1 = new data();
						if ("airTemperature".equals(key)) {
							data1.setType("空气\n温度");
							int max = job.getInt("airTempMaxValue");
							int min = job.getInt("airTempMinValue");
							int curr = job.getInt("airTemperature");
							if (curr > min && curr < max) {
								data1.setJingao("正常");
							} else {
								data1.setJingao("警告");
							}
							data1.setData(curr + "");
						} else if ("airHumidity".equals(key)) {
							data1.setType("空气\n湿度");
							int max = job.getInt("airHumiMaxValue");
							int min = job.getInt("airHumiMinValue");
							int curr = job.getInt("airHumidity");
							if (curr > min && curr < max) {
								data1.setJingao("正常");
							} else {
								data1.setJingao("警告");
							}
							data1.setData(curr + "");
						} else if ("earthTemperature".equals(key)) {
							data1.setType("地温");
							int max = job.getInt("earthTempMaxValue");
							int min = job.getInt("earthTempMinValue");
							int curr = job.getInt("earthTemperature");
							if (curr > min && curr < max) {
								data1.setJingao("正常");
							} else {
								data1.setJingao("警告");
							}
							data1.setData(curr + "");
						} else if ("earthHumidity".equals(key)) {
							data1.setType("土壤\n湿度");
							int max = job.getInt("earthHumiMaxValue");
							int min = job.getInt("earthHumiMinValue");
							int curr = job.getInt("earthHumidity");
							if (curr > min && curr < max) {
								data1.setJingao("正常");
							} else {
								data1.setJingao("警告");
							}
							data1.setData(curr + "");
						} else if ("light".equals(key)) {
							data1.setType("光");
							int max = job.getInt("lightMaxValue");
							int min = job.getInt("lightMinValue");
							int curr = job.getInt("light");
							if (curr > min && curr < max) {
								data1.setJingao("正常");
							} else {
								data1.setJingao("警告");
							}
							data1.setData(curr + "");
						} else if ("co2".equals(key)) {
							data1.setType("co2");
							int max = job.getInt("co2MaxValue");
							int min = job.getInt("co2MinValue");
							int curr = job.getInt("co2");
							if (curr > min && curr < max) {
								data1.setJingao("正常");
							} else {
								data1.setJingao("警告");
							}
							data1.setData(curr + "");
						} else {
							data1 = null;
						}
						if (data1 != null) {
							myGridviewdata.add(data1);

							ContentValues values = new ContentValues();

							sqliteUtils.insert(values);
						}
						gridViewadapter.notifyDataSetChanged();
						isflushdata = true;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1:
				int code = (Integer) msg.obj;
				Toast.makeText(getActivity(), "获取数据失败：" + code, 1).show();

				for(Handler h : handlerzis){
					Message msg1 = new Message();
					msg.what = 0;
					h.sendMessage(msg1);
				}
				break;
			case 2:
				Toast.makeText(getActivity(), "获取数据失败：有异常抛出 ", 1).show();

				for(Handler h : handlerzis){
					Message msg1 = new Message();
					msg.what = 0;
					h.sendMessage(msg1);
				}
				break;
			}
		};
	};


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = LayoutInflater.from(getActivity());
		View v = inflater.inflate(R.layout.fragment1, null);

		getipbutton = (Button) v.findViewById(R.id.button);
		
		myGridView = (MyGridView) v.findViewById(R.id.mgv);
		myGridView.setAdapter(gridViewadapter);
		ip_et = (EditText) v.findViewById(R.id.ip);

		getipbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isflushdata = true;

				AppContext.setContext(getActivity());
				
				sqliteUtils.getValue("airTemperature", 0);
				final String ip = ip_et.getText().toString();
				
				for(Handler h : handlerzis){
					Message msg = new Message();
					msg.what = 0;
					h.sendMessage(msg);
				}
				
				new Thread() {
					
					private boolean xunhuan = false;
					
					Handler handlerzi = new Handler(){
						public void handleMessage(Message msg) {
							if(msg.what == 0)
								xunhuan = false;
						};
					};
					public void run() {
						handlerzis.add(handlerzi);
						
						xunhuan = isflushdata;
						
						while (xunhuan) {
							getdata.getweatherdatafrominter(getdatahandler, ip);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						handlerzis.remove(handlerzi);
					};
				}.start();

			
			}
		});
		
		// TODO Auto-generated method stub
		return v;
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		isflushdata = false;
		super.onDestroy();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return myGridviewdata.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return myGridviewdata.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			View v = inflater.inflate(R.layout.f1_gridview_item, null);

			data d = myGridviewdata.get(position);
			TextView tv1 = (TextView) v.findViewById(R.id.type);
			TextView tv2 = (TextView) v.findViewById(R.id.jingao);
			TextView tv3 = (TextView) v.findViewById(R.id.data);

			tv1.setText(d.getType());
			tv2.setText(d.getJingao());
			tv3.setText(d.getData());
			return v;
		}

	}
}
