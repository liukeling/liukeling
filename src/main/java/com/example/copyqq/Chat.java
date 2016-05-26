package com.example.copyqq;

import java.util.ArrayList;

import com.example.Tools.resource;

import comm.qq_message;
import comm.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class Chat extends Activity implements View.OnClickListener {
	//记录该聊天框是与谁的
	public user frind;
	//发送消息按钮
	private ImageButton sendButton;
	//消息编辑框
	private EditText message;
	//显示消息的适配器
	private Chat_adapter myAdapter;
	//QQ消息的数据
	private ArrayList<qq_message> adapter_data = new ArrayList<qq_message>();
	//消息显示的布局
	private RecyclerView recyclerView;
	//标题
	private TextView title;
	//返回的textView
	private TextView back;
	//储存屏幕的宽高
	private int[] pinmu = new int[2];
	//底部Fragment
	private FrameLayout frame_bottom;
	//用户信息头像
	private ImageView user_info;
	//底部按钮的集合
	private RadioGroup xiaoxi_type;
	//用于接收消息的handler
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if(what == 4){
				adapter_data.addAll((ArrayList<qq_message>) msg.obj);
				myAdapter.notifyDataSetChanged();
				recyclerView.smoothScrollToPosition(adapter_data.size());
			}
		};
	};
	
	@SuppressLint("ShowToast") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//得到屏幕的宽和高
		pinmu[0] = getWindowManager().getDefaultDisplay().getWidth();
		pinmu[1] = getWindowManager().getDefaultDisplay().getHeight();
		//设置布局文件
		setContentView(R.layout.activity_chat);
		//获取到对方对象
		Intent intent = getIntent();
		Object obj = intent.getSerializableExtra("frind");
		//判断对方对象是否是user
		if(obj instanceof user){
			frind = (user)obj;
		}else{
			Toast.makeText(this, "我不知道Ta是谁", Toast.LENGTH_LONG).show();
			this.finish();
		}
		//得到各种控件
//		Scroll_xiaoxi = (ScrollView) findViewById(R.id.sl);
//		neirong = (MyListView) findViewById(R.id.neirong);
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

		sendButton = (ImageButton) findViewById(R.id.sendMessage);
		message = (EditText) findViewById(R.id.message);
		title = (TextView) findViewById(R.id.title);
		frame_bottom = (FrameLayout) findViewById(R.id.frame_bottom);
		back = (TextView) findViewById(R.id.back);
		user_info = (ImageView) findViewById(R.id.user_info);
//		RelativeLayout bottom = (RelativeLayout) findViewById(R.id.bottom);
		xiaoxi_type = (RadioGroup) findViewById(R.id.xiaoxi_type);

		recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));

		myAdapter = new Chat_adapter();
		recyclerView.setAdapter(myAdapter);

		message.setOnClickListener(this);

		recyclerView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action){
					case MotionEvent.ACTION_DOWN:
						setfragme_bottomGONE();
						break;
					case MotionEvent.ACTION_MOVE:

						break;
				}

				return false;
			}
		});
		//设置各种监听
		user_info.setOnClickListener(this);
		xiaoxi_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				setfragment_bottomVisible();
			}
		});
		//获取未读信息
		resource.getQq_Message(frind.getZhanghao() + "");
		//设置标题
		title.setText(frind.getName());
		//设置监听
		back.setOnClickListener(this);
		setfragme_bottomGONE();
		//发送按钮监听
		sendButton.setOnClickListener(this);
		//输入框文字监听
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
				if (message.getText().toString() != null && !"".equals(message.getText().toString())) {
					sendButton.setClickable(true);
					sendButton.setBackgroundResource(R.mipmap.sendbutton_chat);
				} else {
					sendButton.setClickable(false);
					sendButton.setBackgroundResource(R.mipmap.sendbutton_chat1);
				}
			}
		});
		//一开始设置发送按钮为不可点击
		sendButton.setClickable(false);
		//设置背景
		sendButton.setBackgroundResource(R.mipmap.sendbutton_chat1);
//		neirong.setAdapter(myAdapter);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			//返回
			case R.id.back:
				onBackPressed();
				break;
			//发送按钮的点击事件
			case R.id.sendMessage:

				//将输入框中的内容封装成信息（qq_message）对象
				String m_neirong = message.getText().toString();
				qq_message q_msg = new qq_message();
				q_msg.setMessage(m_neirong);
				q_msg.setSendUser_zhanghao(resource.Myzhanghao);
				q_msg.setReciveUser_zhanghao("" + frind.getZhanghao());
				//发送消息
				resource.sendQq_Message(q_msg);
				//发送完信息后将输入框清空
				message.setText("");
				//将信息条目显示在List中
				adapter_data.add(q_msg);
				myAdapter.notifyDataSetChanged();
				//将下拉条滑至最低下
				recyclerView.smoothScrollToPosition(adapter_data.size());
				break;
			//用户信息头像点击事件
			case R.id.user_info:

				startActivity(new Intent(Chat.this, Chatset_Activity.class));

				break;
			//输入框点击监听
			case R.id.message:
				setfragme_bottomGONE();
				break;

		}
	}

	private void setfragme_bottomGONE(){
		frame_bottom.setVisibility(View.GONE);
	}
	private void setfragment_bottomVisible(){
		frame_bottom.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBackPressed() {
		if(frame_bottom.getVisibility() == View.VISIBLE){
			setfragme_bottomGONE();
		}else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onStop() {
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


	class Chat_adapter extends RecyclerView.Adapter<Chat_adapter.MyViewHolder> {

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = null;
			switch (viewType){
				case 0:
					view = View.inflate(Chat.this, R.layout.item_qqmessage1, null);
					break;
				case 1:
					view = View.inflate(Chat.this, R.layout.item_qqmessage2, null);
					break;
			}
			if(view != null) {
				MyViewHolder myViewHolder = new MyViewHolder(view);
				return myViewHolder;
			}else{
				return null;
			}
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, int position) {

			holder.message.setText(adapter_data.get(position).getMessage());
		}

		@Override
		public int getItemCount() {
			return adapter_data.size();
		}

		@Override
		public int getItemViewType(int position) {
			if(resource.Myzhanghao.equals(adapter_data.get(position).getSendUser_zhanghao())){
				return 1;
			}else{
				return 0;
			}
		}

		class MyViewHolder extends RecyclerView.ViewHolder{

			TextView message;

			public MyViewHolder(View itemView) {
				super(itemView);
				message = (TextView) itemView.findViewById(R.id.message);
			}
		}
	}

}
