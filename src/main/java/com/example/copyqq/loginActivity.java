package com.example.copyqq;

import comm.Response;

import com.example.MyViews.MyListView;
import com.example.Tools.IdArray;
import com.example.adapter.IDListViewAdapter;
import com.example.lkl.socketlibrary.tools.resource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements OnClickListener {

    private TextView regist;
    private TextView forgets;

    private Button login;

    private EditText username;
    private EditText userpswd;

    private String name = "";

    private CheckBox remenber;
//    private PopupWindow idWindow;
    private ImageView idsShow;
    private RelativeLayout listGroup;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("ShowToast")
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            //链接服务器失败码
            if (what == 122411) {
                Toast.makeText(LoginActivity.this, "链接失败", Toast.LENGTH_SHORT).show();
            } else if (what == 1) {
                //请求登录码
                Response response = (Response) msg.obj;
                //响应为空说明链接服务器失败
                if (response == null) {
                    Toast.makeText(LoginActivity.this, "链接服务器失败", Toast.LENGTH_SHORT).show();
                } else if (response.getResponse().contains("成功")) {
                    //登录成功
                    IdArray.remove(username.getText().toString());
                    IdArray.add(username.getText().toString(), userpswd.getText().toString(), remenber.isChecked());
                    resource.onLine(this);

                } else {
                    //登录失败
                    Toast.makeText(LoginActivity.this, "登陆失败,用户名或密码错误", Toast.LENGTH_SHORT)
                            .show();
                }
                //登录请求响应后设置登录按钮可点击,并设置背景
                login.setClickable(true);
                login.setBackgroundResource(R.drawable.login_buttonselector);
            } else if (what == 2) {
                //登录成功后上线响应码
                Response response = (Response) msg.obj;
                String onLine = response.getResponse();
                if (onLine.contains("成功")) {
                    //上线成功则跳转到主界面
                    resource.Myzhanghao = name;
                    Intent intent = new Intent(LoginActivity.this,
                            MainFragment.class);
                    startActivity(intent);
                    finish();
                } else {
                    //上线失败说明账号在其他地方已经登录。
                    Toast.makeText(LoginActivity.this, "登陆失败,该账号在其他地方登录", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        ;
    };

    @Override
    protected void onResume() {
        IdArray.flush();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        boolean initSocket = intent.getBooleanExtra("lianjiefuwu", true);
        if(initSocket) {
            resource.lianjie(handler);
        }
        View list_contaner = View.inflate(LoginActivity.this, R.layout.list_contaner, null);
        final MyListView IdsListView = (MyListView) list_contaner.findViewById(R.id.mylistView);
        regist = (TextView) findViewById(R.id.regist);
        forgets = (TextView) findViewById(R.id.forgets);
        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        userpswd = (EditText) findViewById(R.id.userpswd);
        remenber = (CheckBox) findViewById(R.id.remenber);
        idsShow = (ImageView) findViewById(R.id.IdsShow);
        regist.setOnClickListener(this);
        forgets.setOnClickListener(this);
        login.setOnClickListener(this);
        idsShow.setOnClickListener(this);
        listGroup = (RelativeLayout) findViewById(R.id.listGroup);
        listGroup.addView(list_contaner);
        listGroup.setVisibility(View.GONE);
        if (IdArray.getSize() > 0) {
            name = IdArray.getItem(1,0)[0];
        }else{
            idsShow.setVisibility(View.GONE);
        }
        IDListViewAdapter adapter = new IDListViewAdapter(this);
        adapter.setMyListerner(new IDListViewAdapter.MyListerner() {


            @Override
            public void setOnUserIdClick(TextView Idview) {
                String id = Idview.getText().toString();
                username.setText(id);
                userpswd.setText(IdArray.getItem(0, Integer.parseInt(id))[1]);
            }

            @Override
            public void setDelIdclick(View view) {
                Toast.makeText(LoginActivity.this, "del", Toast.LENGTH_SHORT).show();
            }
        });
        IdsListView.setAdapter(adapter);
        String pswd = "";
        if (name == null) {
            name = "";
        }else{
            if(!"".equals(name)) {
                pswd = IdArray.getItem(0, Integer.parseInt(name))[1];
            }
            username.setText(name);
            userpswd.setText(pswd);
        }
        if (!"".equals(name) && !"".equals(pswd) && name != null && pswd != null) {
            remenber.setChecked(true);
        } else {
            remenber.setChecked(false);
        }
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist:
                //注册
                Intent registintent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(registintent);
                break;
            case R.id.forgets:
                //忘记密码
                Intent forgetintent = new Intent(LoginActivity.this, Forgetspswd.class);
                startActivity(forgetintent);
                break;
            case R.id.login:
                //登陆
                login();
                break;
            case R.id.IdsShow:
                if(listGroup.getVisibility() == View.GONE) {
                    listGroup.setVisibility(View.VISIBLE);
                    idsShow.setImageResource(R.mipmap.to_up);
                }else{
                    idsShow.setImageResource(R.mipmap.to_down);
                    listGroup.setVisibility(View.GONE);
                }
                break;
        }

    }

    int back = 0;

    //双击退出
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (back == 1) {
            super.onBackPressed();
        } else {
            new Thread() {
                public void run() {
                    back = 1;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    back = 0;
                }
            }.start();
            Toast.makeText(LoginActivity.this, "再按一次退出", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @SuppressLint("ShowToast")
    //登录按钮的点击方法
    public void login() {
        if (resource.socket != null) {
            //用户名密码
            name = username.getText().toString();
            String pswd = userpswd.getText().toString();
            if (name != null && !"".equals(name) && pswd != null
                    && !"".equals(pswd)) {
                boolean b = name.matches("[0-9]{1,20}");
                if (b) {
                    boolean c = pswd.matches("\\w{1,20}");
                    if (c) {
                        resource.login(name, pswd, handler);
                        login.setClickable(false);
                        login.setBackgroundResource(R.drawable.login_button2);
                    } else {
                        Toast.makeText(this, "密码不能包含非法字符", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "用户名只能是数字，且长度最长为20", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            }
        } else {
            resource.socket = null;
            resource.lianjie(handler);
            Toast.makeText(this, "与服务器断开了，正在重连", Toast.LENGTH_SHORT).show();
        }
    }

}
