package com.example.copyqq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.fragments.frindListfragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import comm.user;

import com.example.Tools.resource;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 这是登陆后的主界面，一个FragmentActivity。
 */
public class MainFragment extends FragmentActivity implements
        OnCheckedChangeListener {
    //toolbar
    private Toolbar main_toolbar;
    //radiogroup
    private RadioGroup rg;

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
                if (frindlistadapter != null) {
                    frindlistadapter.notifyDataSetChanged();
                }
                if ("one".equals(msg.obj)) {
                    resource.jieshouxiaoxiThread(handler);
                }
            } else if (what == 70) {
                Toast.makeText(MainFragment.this,
                        "" + resource.getMe().toString(), Toast.LENGTH_LONG).show();
            } else if (what == 71) {

            }
        }

        ;
    };
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        initView();
        main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.showMenu();
            }
        });
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_2.setBackgroundResource(R.color.qinse);
                tv_1.setBackgroundResource(R.color.white);
                tv_2.setTextColor(Color.parseColor("#ffffff"));
                tv_1.setTextColor(Color.parseColor("#00B7FB"));
                tv_1.setClickable(false);
                tv_2.setClickable(true);
            }
        });
        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_2.setBackgroundResource(R.color.white);
                tv_1.setBackgroundResource(R.color.qinse);
                tv_2.setTextColor(Color.parseColor("#00B7FB"));
                tv_1.setTextColor(Color.parseColor("#ffffff"));
                tv_2.setClickable(false);
                tv_1.setClickable(true);
            }
        });
        rg.setOnCheckedChangeListener(this);
        // 一进来默认是第2个被选中
        rg.check(R.id.radiob2);

        // 获取好友列表数据
        resource.getfrindListdata(handler);

		/*
         * 侧滑功能的实现
		 */
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setBehindOffset(100);

        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 为侧滑菜单设置布局
        menu.setMenu(cehuamen);
		/*
		 * 从侧滑布局中获取控件
		 */
        menuListView = (ListView) cehuamen.findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.menu_item, R.id.tv, menudata);

        menuListView.setAdapter(adapter);
    }
    /*
     * 初始化布局
     */
    private void initView() {
        View view = View.inflate(MainFragment.this, R.layout.mainfragment_layout, null);
        cehuamen = View.inflate(this, R.layout.main_menu, null);
        rg = (RadioGroup) view.findViewById(R.id.radioG);
        main_toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        tv_1 = (TextView) view.findViewById(R.id.tv_1);
        tv_2 = (TextView) view.findViewById(R.id.tv_2);
        tv_3 = (TextView) view.findViewById(R.id.tv_3);
        setContentView(view);
    }

    // 头像点击调用
    public void checkOther(View view) {
        Toast.makeText(this, "hehe", Toast.LENGTH_SHORT).show();
    }

	/*
	 * 用于切换不同Fragment
	 */

    public void setFragment() {
        switch (checkfragment) {
            case 1:

                tv_1.setVisibility(View.VISIBLE);
                tv_2.setVisibility(View.VISIBLE);
                tv_3.setVisibility(View.INVISIBLE);

                break;
            case 2:
                tv_1.setVisibility(View.INVISIBLE);
                tv_2.setVisibility(View.INVISIBLE);
                tv_3.setVisibility(View.VISIBLE);
                tv_3.setText("联系人");
                updata();
                frindlistadapter = new SimpleExpandableListAdapter(this,
                        resource.gruops, R.layout.one_mulu,
                        new String[]{"group"}, new int[]{R.id.tv},
                        resource.childs, R.layout.two_mulu,
                        new String[]{"child"}, new int[]{R.id.tv});
                fra = new frindListfragment(handler, frindlistadapter);
                break;
            case 3:
                tv_1.setVisibility(View.INVISIBLE);
                tv_2.setVisibility(View.INVISIBLE);
                tv_3.setVisibility(View.VISIBLE);
                tv_3.setText("动态");
                break;
        }
        putfragment();
    }
    private void putfragment(){

        if (fra != null) {
            if(curr_fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(curr_fragment).commit();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, fra).commit();
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
        resource.gruops.clear();
        resource.childs.clear();
        ArrayList<Integer> al = new ArrayList<Integer>();
        ArrayList<String> al1 = new ArrayList<String>();
        for (HashMap<HashMap<Integer, String>, user> hm : frinds) {
            for (HashMap<Integer, String> key : hm.keySet()) {
                for (int i : key.keySet()) {
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

        for (String s : al1) {
            Map<String, String> title = new HashMap<String, String>();
            title.put("group", s);
            resource.gruops.add(title);
        }
        int length = al.size();
        for (int i = 0; i < length; i++) {
            List<Map<String, String>> er = new ArrayList<Map<String, String>>();

            for (HashMap<HashMap<Integer, String>, user> hm : frinds) {
                boolean b = false;
                HashMap<String, String> c_er = new HashMap<String, String>();
                user u = null;
                for (HashMap<Integer, String> key : hm.keySet()) {
                    String value = "";
                    u = hm.get(key);
                    value = u.getName() + " " + u.getZhuangtai();
                    if ("是".equals(u.getHaveMassage())) {
                        value = value + "   有消息";
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
                    er.add(c_er);
                }
            }
            resource.childs.add(er);
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        resource.outLine();
        super.onDestroy();
    }

}
