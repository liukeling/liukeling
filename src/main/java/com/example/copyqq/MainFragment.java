package com.example.copyqq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.fragments.DynamicFragment;
import com.example.fragments.FrindListmain_fragment;
import com.example.fragments.SysInfolist_fragmnet;
import com.example.fragments.Phonelist_fragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import comm.SysInfo;
import comm.user;

import com.example.Tools.resource;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

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
    private int checkfragment = 2;
    //要转化的Fragment
    Fragment fra = null;

    // 侧滑布局
    private View cehuamen;
    private TextView usertitle;
    // 侧滑布局中ListView
    private ListView menuListView;
    // 侧滑布局中ListView的数据
    private String[] menudata = {"one", "two", "three", "fore", "five", "six"};
    public MyAdapter frindlistadapter = null;
    FragmentManager fragmentManager;

    // 用于接受信息的handler
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            if (what == 3) {
                frinds.clear();
                ArrayList<HashMap<HashMap<Integer, String>, user>> list = resource
                        .frinds;
                frinds.addAll(list);
                updata();
                //判断是不是第一次接受到消息，
                if ("one".equals(msg.obj)) {
                    //判断是不是消息界面
                    boolean isxiaoxijiemian = false;
                    if (fra != null) {
                        //如果fra不为空则判断是不是消息界面
                        isxiaoxijiemian = fra instanceof SysInfolist_fragmnet;
                    }

                    if (isxiaoxijiemian) {
                        //如果是消息界面则刷新消息
                        SysInfolist_fragmnet frindlist_fragment = (SysInfolist_fragmnet) fra;
                        frindlist_fragment.reflushSysInfo();
                    }
                    //如果有未读的系统消息则震动
                    for (SysInfo sinfo : resource.Sysinfos) {
                        if (!sinfo.isRead()) {
                            //震动
                            Vibrator vibrator = (Vibrator) MainFragment.this.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            Toast.makeText(MainFragment.this, "有未读的系统消息", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    //开始接受消息
                    resource.jieshouxiaoxiThread(handler);
                }
            }
//            else if (what == 71) {
//                //获取到好友的信息
//                user frind_info = (user) msg.obj;
//            }
            else if (what == 9) {
                ArrayList<SysInfo> al = (ArrayList<SysInfo>) msg.obj;
                resource.Sysinfos.clear();
                resource.Sysinfos.addAll(al);
                boolean isxiaoxijiemian = false;
                if (fra != null) {
                    isxiaoxijiemian = fra instanceof SysInfolist_fragmnet;
                }
                if (isxiaoxijiemian) {
                    SysInfolist_fragmnet frindlist_fragment = (SysInfolist_fragmnet) fra;
                    frindlist_fragment.reflushSysInfo();
                }
                for (SysInfo sinfo : al) {
                    if (!sinfo.isRead()) {
                        //震动
                        Vibrator vibrator = (Vibrator) MainFragment.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);
                        Toast.makeText(MainFragment.this, "有未读的系统消息", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            } else if (what == 10) {
                if (!msg.obj.equals("")) {
                    Toast.makeText(MainFragment.this, "" + msg.obj, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainFragment.this, "好友请求回复成功 ", Toast.LENGTH_SHORT).show();
                }
            } else if (what == 1232 && curr_fragment instanceof FrindListmain_fragment) {
                ((FrindListmain_fragment) curr_fragment).freshed();
            }
        }
    };
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        fragmentManager = getSupportFragmentManager();
        initView();
        //点击toolbar的图标，开始侧滑
        main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.showMenu();
            }
        });
        tv_1.setClickable(false);
        //初始化声音
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM , 5);
        soundPool.load(getApplicationContext(), R.raw.qq_message, 1);

        //设置监听
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        add_mainfram.setOnClickListener(this);

        add_mainfram_tv.setOnClickListener(this);

        rg.setOnCheckedChangeListener(this);
        // 获取好友列表数据
        resource.getfrindListdata(handler);
        /*
         * 侧滑功能的实现
		 */
        //设置侧滑菜单的位置在左侧
        menu.setMode(SlidingMenu.LEFT);
        //设置侧滑菜单的宽度
        menu.setBehindOffset(100);

        //以内容的形式显示
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 为侧滑菜单设置布局
        menu.setMenu(cehuamen);
        /*
         * 从侧滑布局中获取控件
		 */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.menu_item, R.id.tv, menudata);
        //侧滑菜单的listview
        menuListView.setAdapter(adapter);
        //侧滑菜单的打开监听
        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                //设置侧滑菜单的数据
                if (resource.getMe() != null) {
                    usertitle.setText(resource.getMe().getName() + "\n" + resource.getMe().getZhanghao());
                } else {
                    usertitle.setText("未知");
                }
            }
        });
        //好友列表的适配器
        frindlistadapter = new MyAdapter();
        updata();
        // 一进来默认是第2个被选中
        rg.check(R.id.radiob2);
//        setFragment();
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
        Intent intent = new Intent(this, UserInfo.class);
        intent.putExtra("user", resource.getMe());
        startActivity(intent);
    }

	/*
     * 用于实例化要切换的Fragment
	 */

    //TODO
    public void setFragment() {

        Fragment fragment;
        switch (checkfragment) {
            case 1:
                tv_1.setVisibility(View.VISIBLE);
                tv_2.setVisibility(View.VISIBLE);
                tv_3.setVisibility(View.INVISIBLE);

                add_mainfram.setVisibility(View.VISIBLE);
                add_mainfram_tv.setVisibility(View.INVISIBLE);
                if (isxinxi) {
                    fragment = fragmentManager.findFragmentByTag("hehe" + checkfragment + isxinxi);
                    if (fragment == null) {
                        fra = SysInfolist_fragmnet.newInstance();
                    } else {
                        fra = fragment;
                    }
                } else {
                    fragment = fragmentManager.findFragmentByTag("hehe" + checkfragment + isxinxi);
                    if (fragment == null) {
                        fra = new Phonelist_fragment();
                    } else {
                        fra = fragment;
                    }
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
                fragment = fragmentManager.findFragmentByTag("hehe" + checkfragment);
                if (fragment == null) {
                    fra = new FrindListmain_fragment(handler);
                } else {
                    fra = fragment;
                }
                break;
            case 3:
                tv_1.setVisibility(View.INVISIBLE);
                tv_2.setVisibility(View.INVISIBLE);
                tv_3.setVisibility(View.VISIBLE);
                add_mainfram.setVisibility(View.INVISIBLE);
                add_mainfram_tv.setVisibility(View.VISIBLE);
                add_mainfram_tv.setText("更多");
                tv_3.setText("动态");
                fragment = fragmentManager.findFragmentByTag("hehe" + checkfragment);
                if (fragment == null) {
                    fra = new DynamicFragment();
                } else {
                    fra = fragment;
                }
                break;

        }
        putfragment();
    }

    /*
        用于切换Fragment
     */
    private void putfragment() {

        if (fra != null) {
            if (fra != curr_fragment) {

                String cur = "" + checkfragment;
                if (checkfragment == 1) {
                    cur = cur + isxinxi;
                }

                if (fragmentManager.findFragmentByTag("hehe" + cur) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.frame, fra, "hehe" + cur).show(fra).commit();
                } else {
                    List<Fragment> list = fragmentManager.getFragments();
                    for(Fragment f : list){
                        fragmentManager.beginTransaction().hide(f).commit();
                    }
                    fragmentManager.beginTransaction().show(fra).commit();
                }
            }
            curr_fragment = fra;
        }
    }
    /*
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
                //i为分组编号
                for (int i : key.keySet()) {
                    //用于判断是否有重复的分组编号
                    boolean b = false;
                    //遍历分组编号的集合，如果存在i==k，那么就有重复
                    for (int k : al) {
                        if (i == k) {
                            b = true;
                            break;
                        }
                    }
                    //如果编号不重复则添加到编号集合内，并且将分组的名称添加到al1集合内，否则break；
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
//            boolean userIsNull = false;

            List<Map<String, user>> er = new ArrayList<Map<String, user>>();
            for (HashMap<HashMap<Integer, String>, user> hm : frinds) {
                //记录是否添加至二级目录
                boolean addTotwo = false;
                HashMap<String, user> c_er = new HashMap<String, user>();
                for (HashMap<Integer, String> key : hm.keySet()) {
                    user u;
                    String value = "";
                    u = hm.get(key);
                    for (int k : key.keySet()) {
                        if (al.get(i) == k) {
                            if (!(u == null)) {
                                c_er.put("child", u);
                                addTotwo = true;
                            }
                        }
                    }

                }
                if (addTotwo) {
                    er.add(c_er);
                }
            }
            resource.childs.add(er);
        }

        if (frindlistadapter != null) {
            frindlistadapter.notifyDataSetChanged();
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
        Fragment fragment;
        switch (v.getId()) {
            case R.id.tv_1:

                tv_2.setBackgroundResource(R.color.qinse);
                tv_1.setBackgroundResource(R.color.white);
                tv_2.setTextColor(Color.parseColor("#ffffff"));
                tv_1.setTextColor(Color.parseColor("#00B7FB"));
                tv_1.setClickable(false);
                tv_2.setClickable(true);
                isxinxi = true;
                fragment = fragmentManager.findFragmentByTag("hehe1true");
                if (fragment == null) {
                    fra = SysInfolist_fragmnet.newInstance();
                } else {
                    fra = fragment;
                }
                putfragment();
                break;
            case R.id.tv_2:

                tv_2.setBackgroundResource(R.color.white);
                tv_1.setBackgroundResource(R.color.qinse);
                tv_2.setTextColor(Color.parseColor("#00B7FB"));
                tv_1.setTextColor(Color.parseColor("#ffffff"));
                tv_2.setClickable(false);
                tv_1.setClickable(true);
                fragment = fragmentManager.findFragmentByTag("hehe1false");
                isxinxi = false;
                if (fragment == null) {
                    fra = new Phonelist_fragment();
                } else {
                    fra = fragment;
                }
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
            case R.id.add_mainfram:
                break;
        }
    }


    public class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return resource.gruops.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return resource.childs.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return resource.gruops.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return resource.childs.get(groupPosition)
                    .get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = View.inflate(MainFragment.this.getApplicationContext(), R.layout.two_mulu, null);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText(((HashMap<String, String>) getGroup(groupPosition)).get("group"));
            Integer[] integers = {-1, groupPosition};
            view.setTag(integers);
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            View view = View.inflate(MainFragment.this.getApplicationContext(), R.layout.one_mulu, null);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            user u = ((HashMap<String, user>) getChild(groupPosition, childPosition)).get("child");
            String s = "";

            if (u != null) {
                s = u.getName() + " " + u.getZhuangtai();
                if ("是".equals(u.getHaveMassage())) {
                    //有未读消息
                    s = s + "   有消息";
                    playSound();
                }
            }
            tv.setText(s);
            Integer[] integers = {groupPosition, childPosition};
            view.setTag(integers);
            return view;
        }

        //允许子节点可以点击选中
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public void playSound() {
        soundPool.play(1, 1, 1, 1, 0, 1);
    }
}
