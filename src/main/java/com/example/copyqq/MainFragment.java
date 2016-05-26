package com.example.copyqq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.fragments.FrindListmain_fragment;
import com.example.fragments.Frindlist_fragmnet;
import com.example.fragments.Phonelist_fragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import comm.user;

import com.example.Tools.resource;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainFragment extends FragmentActivity implements
        OnCheckedChangeListener, View.OnClickListener {
    //是消息界面还是电话界面
    private boolean isxinxi = true;
    //toolbar
    private Toolbar main_toolbar;
    //radiogroup
    private RadioGroup rg;
    //toolbar菜单
    private ImageView add_mainfram;
    private TextView add_mainfram_tv;

    private View Main_view;
    //侧滑菜单
    private SlidingMenu menu;
    //toolbar上的title
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    //记录当前Fragment
    private Fragment curr_fragment;
    //获取到好友列表数据
    private ArrayList<HashMap<HashMap<Integer, String>, user>> frinds = new ArrayList<HashMap<HashMap<Integer, String>, user>>();
    // 当前的Fragment
    private int checkfragment = 1;
    //要转化的Fragment
    Fragment fra = null;

    // 侧滑布局
    private View cehuamen;
    private TextView usertitle;
    // 侧滑布局中ListView
    private ListView menuListView;
    // 侧滑布局中ListView的数据
    private String[] menudata = {"one", "two", "three", "fore", "five", "six"};
    // 好友列表的适配器
    private SimpleExpandableListAdapter frindlistadapter = null;
    // 用于接受信息的handler
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if (what == 3) {
                frinds.clear();
                ArrayList<HashMap<HashMap<Integer, String>, user>> list = resource
                        .getLinshiobj();
                frinds.addAll(list);
                updata();
                if ("one".equals(msg.obj)) {
                    resource.jieshouxiaoxiThread(handler);
                }
            } else if (what == 71) {
                //获取到好友的信息
                user frind_info = (user) msg.obj;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        initView();
        //点击toolbar的图标，开始侧滑
        main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.showMenu();
            }
        });
        tv_1.setClickable(false);
//TODO
        //设置监听
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);

        add_mainfram_tv.setOnClickListener(this);

        rg.setOnCheckedChangeListener(this);
        // 一进来默认是第2个被选中
        rg.check(R.id.radiob2);

        // 获取好友列表数据
        resource.getfrindListdata(handler);

		/*
         * 侧滑功能的实现
		 */
        menu.setMode(SlidingMenu.LEFT);
        menu.setBehindOffset(100);

        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 为侧滑菜单设置布局
        menu.setMenu(cehuamen);
        /*
		 * 从侧滑布局中获取控件
		 */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.menu_item, R.id.tv, menudata);

        menuListView.setAdapter(adapter);

        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {

                if (resource.getMe() != null) {
                    usertitle.setText(resource.getMe().getName() + "\n" + resource.getMe().getZhanghao());
                } else {
                    usertitle.setText("未知");
                }
            }
        });

        updata();
    }

    /*
     * 初始化布局
     */
    private void initView() {
        Main_view = View.inflate(MainFragment.this, R.layout.mainfragment_layout, null);
        cehuamen = View.inflate(this, R.layout.main_menu, null);
        menu = new SlidingMenu(this);
        menuListView = (ListView) cehuamen.findViewById(R.id.listView);
        usertitle = (TextView) cehuamen.findViewById(R.id.usertitle);
        rg = (RadioGroup) Main_view.findViewById(R.id.radioG);
        main_toolbar = (Toolbar) Main_view.findViewById(R.id.main_toolbar);
        tv_1 = (TextView) Main_view.findViewById(R.id.tv_1);
        tv_2 = (TextView) Main_view.findViewById(R.id.tv_2);
        tv_3 = (TextView) Main_view.findViewById(R.id.tv_3);
        add_mainfram = (ImageView) Main_view.findViewById(R.id.add_mainfram);
        add_mainfram_tv = (TextView) Main_view.findViewById(R.id.add_mainfram_tv);
        setContentView(Main_view);
    }

    // 头像点击调用
    public void checkOther(View view) {
        //TODO
        startActivity(new Intent(this, UserInfo.class));
    }

	/*
	 * 用于实例化要切换的Fragment
	 */

    //TODO
    public void setFragment() {
        switch (checkfragment) {
            case 1:
                tv_1.setVisibility(View.VISIBLE);
                tv_2.setVisibility(View.VISIBLE);
                tv_3.setVisibility(View.INVISIBLE);

                add_mainfram.setVisibility(View.VISIBLE);
                add_mainfram_tv.setVisibility(View.INVISIBLE);
                if (isxinxi) {
                    fra = new Frindlist_fragmnet();
                } else {
                    fra = new Phonelist_fragment();
                }
                break;
            case 2:
                tv_1.setVisibility(View.INVISIBLE);
                tv_2.setVisibility(View.INVISIBLE);
                tv_3.setVisibility(View.VISIBLE);
                add_mainfram.setVisibility(View.INVISIBLE);
                add_mainfram_tv.setVisibility(View.VISIBLE);

                add_mainfram_tv.setText("添加");

                tv_3.setText("联系人");
                frindlistadapter = new SimpleExpandableListAdapter(this,
                        resource.gruops, R.layout.one_mulu,
                        new String[]{"group"}, new int[]{R.id.tv},
                        resource.childs, R.layout.two_mulu,
                        new String[]{"child"}, new int[]{R.id.tv});
                fra = new FrindListmain_fragment(handler, frindlistadapter);
                break;
            case 3:
                tv_1.setVisibility(View.INVISIBLE);
                tv_2.setVisibility(View.INVISIBLE);
                tv_3.setVisibility(View.VISIBLE);
                add_mainfram.setVisibility(View.INVISIBLE);
                add_mainfram_tv.setVisibility(View.VISIBLE);
                add_mainfram_tv.setText("更多");
                tv_3.setText("动态");
                break;
        }
        putfragment();
    }

    /*
        用于切换Fragment
     */
    private void putfragment() {

        if (fra != null) {
            if (curr_fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(curr_fragment).commit();
            }
            if (fra != curr_fragment) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fra).commit();
            }
            curr_fragment = fra;
        }
    }
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android
	 * .widget.RadioGroup, int)
	 *
	 * 底部导航栏的切换监听
	 */

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        switch (checkedId) {
            case R.id.radiob1:
                checkfragment = 1;
                break;
            case R.id.radiob2:
                checkfragment = 2;
                break;
            case R.id.radiob3:
                checkfragment = 3;
                break;
        }
        setFragment();
    }

    /*
     * 这个方法是用来更新好友列表数据的
     */
    private void updata() {

        /*
            更新好友分组
         */
        resource.gruops.clear();
        resource.childs.clear();
        //用于储存分组编号
        ArrayList<Integer> al = new ArrayList<Integer>();
        //用于储存分组名称
        ArrayList<String> al1 = new ArrayList<String>();
        for (HashMap<HashMap<Integer, String>, user> hm : frinds) {
            for (HashMap<Integer, String> key : hm.keySet()) {
                for (int i : key.keySet()) {
                    //用于判断是否有重复的分组编号
                    boolean b = false;
                    for (int k : al) {
                        if (i == k) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        break;
                    } else {
                        al.add(i);
                        al1.add(key.get(i));
                    }
                }
            }
        }
        //一级分组的数据设置
        for (String s : al1) {
            Map<String, String> title = new HashMap<String, String>();
            title.put("group", s);
            resource.gruops.add(title);
        }
        //二级分组的数据设置
        int length = al.size();
        for (int i = 0; i < length; i++) {
            boolean userIsNull = false;

            List<Map<String, String>> er = new ArrayList<Map<String, String>>();

            for (HashMap<HashMap<Integer, String>, user> hm : frinds) {
                boolean b = false;
                HashMap<String, String> c_er = new HashMap<String, String>();
                user u = null;
                for (HashMap<Integer, String> key : hm.keySet()) {
                    String value = "";
                    u = hm.get(key);
                    if (u != null) {
                        value = u.getName() + " " + u.getZhuangtai();
                        if ("是".equals(u.getHaveMassage())) {
                            value = value + "   有消息";
                        }
                    } else {
                        userIsNull = true;
                    }
                    for (int k : key.keySet()) {
                        if (al.get(i) == k) {
                            c_er.put("child", value);
                            b = true;
                        }
                    }

                }
                if (b) {
                    HashMap<Integer, Integer> jilu = new HashMap<Integer, Integer>();
                    jilu.put(i, er.size());
                    resource.frindList.put(jilu, u);
                    //如果用户为空就不添加到分组
                    if(u != null) {
                        er.add(c_er);
                    }
                }
            }
            resource.childs.add(er);
        }

        if (fra instanceof FrindListmain_fragment) {
            FrindListmain_fragment f = (FrindListmain_fragment) fra;
            if (f.frindlistadapter != null) {
                f.frindlistadapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        resource.outLine();
        super.onDestroy();
    }

    //TODO
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:

                tv_2.setBackgroundResource(R.color.qinse);
                tv_1.setBackgroundResource(R.color.white);
                tv_2.setTextColor(Color.parseColor("#ffffff"));
                tv_1.setTextColor(Color.parseColor("#00B7FB"));
                tv_1.setClickable(false);
                tv_2.setClickable(true);
                fra = new Frindlist_fragmnet();
                isxinxi = true;
                putfragment();
                break;
            case R.id.tv_2:

                tv_2.setBackgroundResource(R.color.white);
                tv_1.setBackgroundResource(R.color.qinse);
                tv_2.setTextColor(Color.parseColor("#00B7FB"));
                tv_1.setTextColor(Color.parseColor("#ffffff"));
                tv_2.setClickable(false);
                tv_1.setClickable(true);
                isxinxi = false;
                fra = new Phonelist_fragment();
                putfragment();
                break;
            case R.id.add_mainfram_tv:
                if (checkfragment == 2) {
                    Intent intent = new Intent(this, AddNewFrind.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.no_translate);
                } else {
                    //更多功能

                }
                break;
        }
    }
}
