package com.example.copyqq;

import java.util.ArrayList;

import com.example.MyViews.MyListView;
import com.example.Tools.resource;
import comm.qq_message;
import comm.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class Chat extends Activity {
	public user frind;
	
	private MyListView neirong;
	private ImageButton sendButton;
	private EditText message;
	private Myadapter myAdapter = new Myadapter();
	private ArrayList<qq_message> adapter_data = new ArrayList<qq_message>();
	private ScrollView sl;
	
	private int[] pinmu = new int[2];
	
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if(what == 4){
				adapter_data.addAll((ArrayList<qq_message>) msg.obj);
				myAdapter.notifyDataSetChanged();
				
			}
		};
	};
	
	@SuppressLint("ShowToast") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pinmu[0] = getWindowManager().getDefaultDisplay().getWidth();
		pinmu[1] = getWindowManager().getDefaultDisplay().getHeight();
		
		setContentView(R.layout.activity_chat);
		Intent intent = getIntent();
		Object obj = intent.getSerializableExtra("frind");
		
		if(obj instanceof user){
			frind = (user)obj;
		}else{
			Toast.makeText(this, "我不知道Ta是谁", Toast.LENGTH_LONG).show();
			this.finish();
		}
		sl = (ScrollView) findViewById(R.id.sl);
		resource.getQq_Message(frind.getZhanghao() + "");
		neirong = (MyListView) findViewById(R.id.neirong);
		sendButton = (ImageButton) findViewById(R.id.sendMessage);
		message = (EditText) findViewById(R.id.message);
		message.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(message.getText().toString() != null && !"".equals(message.getText().toString())){
					sendButton.setClickable(true);
					sendButton.setBackgroundResource(R.mipmap.sendbutton_chat);
				}else{
					sendButton.setClickable(false);
					sendButton.setBackgroundResource(R.mipmap.sendbutton_chat1);
				}
			}
		});
		sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String m_neirong = message.getText().toString();
				qq_message q_msg = new qq_message();
				q_msg.setMessage(m_neirong);
				q_msg.setSendUser_zhanghao(resource.Myzhanghao);
				q_msg.setReciveUser_zhanghao(""+frind.getZhanghao());
				resource.sendQq_Message(q_msg);
				message.setText("");
				adapter_data.add(q_msg);
				myAdapter.notifyDataSetChanged();
				sl.setTouchDelegate(new TouchDelegate(new Rect(), new View(Chat.this)));
			}
		});
		sendButton.setClickable(false);
		sendButton.setBackgroundResource(R.mipmap.sendbutton_chat1);
		neirong.setAdapter(myAdapter);
	}
	
	class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return adapter_data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return adapter_data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			qq_message q_msg = adapter_data.get(position);
			String s = q_msg.getSendUser_zhanghao();
			LayoutInflater inflater = LayoutInflater.from(Chat.this);
			View v;
			if(s.equals(resource.Myzhanghao)){
				v = inflater.inflate(R.layout.item_qqmessage2, null);
				TextView q_message = (TextView) v.findViewById(R.id.message);
				q_message.setText(q_msg.getMessage());
			}else{
				v = inflater.inflate(R.layout.item_qqmessage1, null);
				TextView q_message = (TextView) v.findViewById(R.id.message);
				q_message.setText(q_msg.getMessage());
			}

			sl.scrollTo(pinmu[0], pinmu[1]);
			return v;
		}
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		resource.user_chat.remove(frind);
		super.onStop();
	}
	@Override
	protected void onResume() {
		resource.user_chat.put(frind, this);
		super.onResume();
	}
	public user getFrind() {
		return frind;
	}
}
