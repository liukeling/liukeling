package com.example.copyqq;

import java.util.ArrayList;

import com.example.fragments.Chat_fragment_buttom_lyj;
import com.example.fragments.Chat_fragment_buttom_other;
import com.example.lkl.socketlibrary.BaseChat;
import com.example.lkl.socketlibrary.BaseFrindInfo_More;
import com.example.lkl.socketlibrary.tools.resource;

import comm.qq_message;
import comm.user;

import android.annotation.SuppressLint;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Chat extends FragmentActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,BaseChat {
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
    SwipeRefreshLayout fresh;
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
    private RadioButton rb_lyj;
    private RadioButton rb_lxj;
    private RadioButton rb_photo;
    private RadioButton rb_zxj;
    private RadioButton rb_money;
    private RadioButton rb_bq;
    private RadioButton rb_more;
    //记录当前的fragment
    private Fragment cur_buttom_fragment;
    private FragmentManager fragmentManager;
    //用于接收消息的handler
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if (what == 4) {
                //接收到新的消息
                adapter_data.addAll((ArrayList<qq_message>) msg.obj);
                myAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter_data.size());
            } else if (what == 1231) {
                //刷新消息时获取到的历史记录
                fresh.setRefreshing(false);
                adapter_data.addAll(0, (ArrayList<qq_message>) msg.obj);
                myAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);
            }
        }

        ;
    };

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //得到fragmentmanager
        fragmentManager = getSupportFragmentManager();
        //得到屏幕的宽和高
        pinmu[0] = getWindowManager().getDefaultDisplay().getWidth();
        pinmu[1] = getWindowManager().getDefaultDisplay().getHeight();
        //设置布局文件
        setContentView(R.layout.activity_chat);
        //获取到对方对象
        Intent intent = getIntent();
        Object obj = intent.getSerializableExtra("frind");
        //判断对方对象是否是user
        if (obj instanceof user) {
            frind = (user) obj;
        } else {
            Toast.makeText(this, "我不知道Ta是谁", Toast.LENGTH_LONG).show();
            this.finish();
        }
        //得到各种控件
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        fresh = (SwipeRefreshLayout) findViewById(R.id.fresh);
        sendButton = (ImageButton) findViewById(R.id.sendMessage);
        message = (EditText) findViewById(R.id.message);
        title = (TextView) findViewById(R.id.title);
        frame_bottom = (FrameLayout) findViewById(R.id.frame_bottom);
        back = (TextView) findViewById(R.id.back);
        user_info = (ImageView) findViewById(R.id.user_info);

        rb_lyj = (RadioButton) findViewById(R.id.lyj);
        rb_bq = (RadioButton) findViewById(R.id.bq);
        rb_lxj = (RadioButton) findViewById(R.id.lxj);
        rb_money = (RadioButton) findViewById(R.id.money);
        rb_more = (RadioButton) findViewById(R.id.more);
        rb_photo = (RadioButton) findViewById(R.id.photo);
        rb_zxj = (RadioButton) findViewById(R.id.zxj);
//		RelativeLayout bottom = (RelativeLayout) findViewById(R.id.bottom);
        xiaoxi_type = (RadioGroup) findViewById(R.id.xiaoxi_type);
        recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));

        fresh.setOnRefreshListener(this);

        myAdapter = new Chat_adapter();
        recyclerView.setAdapter(myAdapter);

        //设置各种监听
        message.setOnClickListener(this);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        setfragme_bottomGONE();
                        closeInputMethod();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }

                return false;
            }
        });
        user_info.setOnClickListener(this);
        xiaoxi_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //如果选择的button不为空
                if(checkedId != -1) {
                    //设置底部的fragment显示
                    setfragment_bottomVisible();
                    //关闭输入法
                    closeInputMethod();
                    //设置显示的fragment
                    setframe_bottomLayout(checkedId);
                }
                //设置图标的改变
                setIconChange(checkedId);
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
                // 当输入框文本发生改变时对发送消息按钮进行相关设置
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
    }

    //关闭输入法
    public void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(message.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                Intent intent = new Intent(Chat.this, Chatset_Activity.class);

                if (frind != null) {
                    intent.putExtra("user", frind);
                }
                startActivityForResult(intent, 1);
                break;
            //输入框点击监听
            case R.id.message:
                setfragme_bottomGONE();
                break;
        }
    }
    //用于设置frame_bottom的布局
    public void setframe_bottomLayout(int id){
        //如果当前fragment不为空则将当前fragment设置为隐藏
        if(cur_buttom_fragment != null){
            fragmentManager.beginTransaction().hide(cur_buttom_fragment).commit();
        }
        //如果要显示的fragment之前添加过那么直接显示该fragment否则添加该fragment并显示   标记为 frame_bottom+id
        Fragment fragment = fragmentManager.findFragmentByTag("frame_bottom" + id);
        //判断标记的fragment是否为空
        if(fragment != null){
            //不为空则将该fragment显示
            fragmentManager.beginTransaction().show(fragment).commit();
            cur_buttom_fragment = fragment;
        }else {
            //为空则根据id来创建fragment并显示
            switch (id) {
                case R.id.lyj:
                    Fragment lyj_fragment = new Chat_fragment_buttom_lyj();
                    fragmentManager.beginTransaction().add(R.id.frame_bottom,lyj_fragment,"frame_bottom" + id).show(lyj_fragment).commit();
                    cur_buttom_fragment = lyj_fragment;
                    break;
                default:
                    Fragment other_fragment = new Chat_fragment_buttom_other();
                    fragmentManager.beginTransaction().add(R.id.frame_bottom,other_fragment,"frame_bottom" + id).show(other_fragment).commit();
                    cur_buttom_fragment = other_fragment;
                    break;
            }
        }
    }
    //用于更改RadioButton图片
    private void setIconChange(int id) {
        rb_lyj.setBackgroundResource(R.mipmap.hui_mai_icon);
        rb_zxj.setBackgroundResource(R.mipmap.hui_phone_icoon);
        rb_photo.setBackgroundResource(R.mipmap.hui_image_icon);
        rb_more.setBackgroundResource(R.mipmap.hui_jia_icon);
        rb_bq.setBackgroundResource(R.mipmap.hui_xiao_icon);
        rb_money.setBackgroundResource(R.mipmap.hui_hongbao_icon);
        rb_lxj.setBackgroundResource(R.mipmap.hui_vadio_icon);
        switch (id){
            case R.id.lyj:
                rb_lyj.setBackgroundResource(R.mipmap.lan_mai_icon);
                break;
            case R.id.zxj:
                rb_zxj.setBackgroundResource(R.mipmap.lan_phone_icon);
                break;
            case R.id.photo:
                rb_photo.setBackgroundResource(R.mipmap.lan_image_icon);
                break;
            case R.id.more:
                rb_more.setBackgroundResource(R.mipmap.lan_jia_icon);
                break;
            case R.id.bq:
                rb_bq.setBackgroundResource(R.mipmap.lan_xiao_icon);
                break;
            case R.id.money:
                rb_money.setBackgroundResource(R.mipmap.hong_hongbao_icon);
                break;
            case R.id.lxj:
                rb_lxj.setBackgroundResource(R.mipmap.lan_vadio_icon);
                break;
        }
    }
    //接收返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                //TODO
                case 5:
                    onBackPressed();
                    break;
            }
        }
    }
    //设置底部fragment消失
    private void setfragme_bottomGONE() {
        xiaoxi_type.check(-1);
        frame_bottom.setVisibility(View.GONE);
    }
    //设置底部的fragment显示
    private void setfragment_bottomVisible() {
        frame_bottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        //按返回键时判断底部fragment是否为显示，是则隐藏否则返回前一个activity
        if (frame_bottom.getVisibility() == View.VISIBLE) {
            setfragme_bottomGONE();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        //聊天框关闭时将该聊天框从集合中移除
        resource.user_chat.remove(frind);
        super.onStop();
    }

    @Override
    protected void onResume() {
        //进来时将对话框放进集合储存
        resource.user_chat.put(frind, this);
        super.onResume();
    }

    @Override
    public void onRefresh() {
        //下拉加载历史记录时判断对话框是否已有聊天记录有则将第一条记录的id传过去，否则传-1表示没有任何记录
        if (adapter_data.size() != 0) {
            resource.getRecord(frind.getZhanghao() + "", adapter_data.get(0).getId(), handler);
        } else {
            resource.getRecord(frind.getZhanghao() + "", -1, handler);
        }
    }

    //recyclerview适配器
    class Chat_adapter extends RecyclerView.Adapter<Chat_adapter.MyViewHolder> {
        //设置标记,如果是我发送的消息标记则为1，如果是对方的消息则为0；
        @Override
        public int getItemViewType(int position) {
            if (resource.Myzhanghao.equals(adapter_data.get(position).getSendUser_zhanghao())) {
                return 1;
            } else {
                return 0;
            }
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            //判断标记来设置相关的条目布局
            switch (viewType) {
                case 0:
                    //对方消息的条目布局
                    view = View.inflate(Chat.this, R.layout.item_qqmessage1, null);
                    break;
                case 1:
                    //我发送的消息的条目布局
                    view = View.inflate(Chat.this, R.layout.item_qqmessage2, null);
                    break;
            }
            if (view != null) {
                MyViewHolder myViewHolder = new MyViewHolder(view);
                return myViewHolder;
            } else {
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

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView message;

            public MyViewHolder(View itemView) {
                super(itemView);
                message = (TextView) itemView.findViewById(R.id.message);
            }
        }
    }

}
