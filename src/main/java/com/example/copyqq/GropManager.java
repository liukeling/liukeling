package com.example.copyqq;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import comm.Modify_groupitem;
import comm.user;

public class GropManager extends AppCompatActivity {
    //完成按钮
    private TextView ok;
    //添加分组
    private TextView addgroup;
    //储存分组
    private ListView grops;
    //ListView的适配器
    private MyAdapter adapter;
    //适配器需要的数据
    private HashMap<Integer, String> group_id;
    private ArrayList<Integer> id_keys;
    //等待消息时弹的不可取消对话框
    private AlertDialog dialog;
    //用于接收修改后消息
    private Handler hanler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 11:
                    boolean ok = (boolean) msg.obj;
                    if(ok){
                        Toast.makeText(GropManager.this, "管理成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(GropManager.this, "管理失败", Toast.LENGTH_SHORT).show();
                    }
                    //更新数据
                    updata();
                    //通知适配器更新数据
                    adapter.notifyDataSetChanged();
                    if(dialog != null){
                        dialog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group_id = new HashMap<>();
        id_keys = new ArrayList<>();
        //更新数据
        updata();
        //设置布局文件
        setContentView(R.layout.activity_grop_manager);
        //找到控件
        ok = (TextView) findViewById(R.id.ok);
        addgroup = (TextView) findViewById(R.id.addgroup);
        grops = (ListView) findViewById(R.id.grops);
        adapter = new MyAdapter();
        grops.setAdapter(adapter);
        //按完成键返回
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //添加分组 监听
        addgroup.setOnClickListener(new View.OnClickListener() {
            AlertDialog dialog;
            @Override
            public void onClick(View v) {
                final View item_et = View.inflate(GropManager.this, R.layout.edit_gropitem, null);
                //弹出对话框，让用户输入分组名称并确定修改
                dialog = new AlertDialog.Builder(GropManager.this)
                        .setTitle("请输入分组名称")
                        .setView(item_et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //找到输入名称的输入框
                                EditText et = (EditText) item_et.findViewById(R.id.et);
                                //获取分组名
                                String groupName = et.getText().toString();
                                //实例化分组管理对象
                                Modify_groupitem modify_groupitem = new Modify_groupitem();
                                //设置管理类型
                                modify_groupitem.setType(1);
                                //设置分组名称
                                modify_groupitem.setItem_Name(groupName);
                                //开始修改
                                resource.groupManager(modify_groupitem, hanler);
                                //弹出一个等待对话框
                                dialog();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                //显示对话框
                dialog.show();
            }
        });

    }
    public void updata(){
        //更新数据前将数据清空
        group_id.clear();
        id_keys.clear();
        //得到分组id对应的分组名称
        for(HashMap<HashMap<Integer, String>, user> hm : resource.frinds){
            Set<HashMap<Integer, String>> set = hm.keySet();
            for(HashMap<Integer, String> hm1 : set){
                group_id.putAll(hm1);
            }
        }
        //将分组ID储存到集合中
        Collection collection = group_id.keySet();
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()){
            Integer s = (Integer) iterator.next();
            id_keys.add(s);
        }
    }
    public void dialog(){
        if(dialog == null) {
            dialog = new AlertDialog.Builder(GropManager.this)
                    .setTitle("正在修改中，请稍后。。。")
                    .setCancelable(false)
                    .create();
        }
        dialog.show();
    }

    private class MyAdapter extends BaseAdapter {

        //所有分组的个数
        @Override
        public int getCount() {
            return id_keys.size();
        }

        //根据某个条目得到分组的名称
        @Override
        public Object getItem(int position) {
            //先得到分组id然后根据id得到分组名
            return group_id.get(id_keys.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(GropManager.this, R.layout.groupmanager_item, null);
            }
            //得到分组id
            final int itemId = id_keys.get(position);
            //得到分组名称
            String itemName = (String) getItem(position);
            //找到控件
            //删除按钮
            final Button del = (Button) convertView.findViewById(R.id.del);
            //三横的图片，点击无效的，占位置用
            final ImageView san = (ImageView) convertView.findViewById(R.id.san);
            //显示分组名称的控件
            TextView tv_name = (TextView) convertView.findViewById(R.id.groupname);
            //显示或隐藏删除按钮的控件
            RelativeLayout del_dra = (RelativeLayout) convertView.findViewById(R.id.del_dra);
            //设置分组名称
            tv_name.setText(itemName + "");
            //刚开始删除按钮是隐藏的，站位的三横图片是显示的
            del.setVisibility(View.GONE);
            san.setVisibility(View.VISIBLE);
            //控制删除按钮和站位的三横的显示和隐藏
            del_dra.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (del.getVisibility() == View.GONE) {
                        del.setVisibility(View.VISIBLE);
                        san.setVisibility(View.GONE);
                    } else {
                        del.setVisibility(View.GONE);
                        san.setVisibility(View.VISIBLE);
                    }
                }
            });
            tv_name.setOnClickListener(new View.OnClickListener(){
                AlertDialog alertDialog;
                @Override
                public void onClick(View v) {
                    //如果删除按钮为显示就将其隐藏，显示三横的站位图
                    //如果删除按钮为隐藏，则弹出对话框，修改分组名称
                    if (del.getVisibility() == View.GONE) {
                        final View item_et = View.inflate(GropManager.this, R.layout.edit_gropitem, null);
                        final EditText et = (EditText) item_et.findViewById(R.id.et);
                        alertDialog = new AlertDialog.Builder(GropManager.this)
                                .setView(item_et)
                                .setTitle("请输入名称")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Modify_groupitem modify_groupitem = new Modify_groupitem();
                                        modify_groupitem.setType(0);
                                        modify_groupitem.setId(id_keys.get(position));
                                        modify_groupitem.setItem_Name(et.getText().toString());
                                        resource.groupManager(modify_groupitem, hanler);
                                    }
                                })
                                .create();
                        alertDialog.show();
                    } else {
                        del.setVisibility(View.GONE);
                        san.setVisibility(View.VISIBLE);
                    }
                }
            });

            //点击确定时弹出是否删除对话框
            del.setOnClickListener(new View.OnClickListener() {
                AlertDialog del_alert;
                @Override
                public void onClick(View v) {
                    del_alert = new AlertDialog.Builder(GropManager.this)
                            .setTitle("确定删除？")
                            //确定删除时访问将要删除的列表中的好友移动至那个分组中
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                //选择分组对话框
                                AlertDialog selectGroup;

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (!(getCount() <= 1)) {

                                        //选择对话框的布局
                                        View view = View.inflate(GropManager.this, R.layout.checkgroup, null);
                                        //找到控件
                                        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                                        //储存分组id和分组名称的集合
                                        final ArrayList<HashMap<Integer, String>> data = new ArrayList<>();
                                        //将集合中的分组名称按顺序储存到另一个集合中作为适配器的数据
                                        ArrayList<String> groupNames = new ArrayList<>();
                                        //用于储存选择的条目位置
                                        final int[] select_position = {0};
                                        //将集合中的分组名称按顺序储存到另一个集合的过程
                                        for (HashMap<HashMap<Integer, String>, user> hm : resource.frinds) {
                                            HashMap<Integer, String> h = hm.keySet().iterator().next();
                                            int id = h.keySet().iterator().next();
                                            if (id != itemId) {
                                                data.addAll(hm.keySet());
                                            }
                                        }
                                        for (HashMap<Integer, String> hm : data) {
                                            int id = hm.keySet().iterator().next();
                                            if (id != itemId) {
                                                groupNames.addAll(hm.values());
                                            }
                                        }
                                        //设置ArrayAdapter适配器
                                        spinner.setAdapter(new ArrayAdapter<String>(GropManager.this, R.layout.spinner_item, R.id.tv, groupNames));
                                        //设置条目选择监听
                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int cur, long id) {
                                                select_position[0] = cur;
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                        selectGroup = new AlertDialog.Builder(GropManager.this)
                                                .setTitle("请选择要转入的分组")
                                                .setView(view)
                                                        //点击取消就将对话框取消掉
                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        del_alert.dismiss();
                                                        selectGroup.dismiss();
                                                    }
                                                })
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        int cur = select_position[0];
                                                        HashMap<Integer, String> hm = data.get(cur);
                                                        int id = hm.keySet().iterator().next();
                                                        Modify_groupitem modify = new Modify_groupitem();
                                                        modify.setType(2);
                                                        modify.setId(itemId);
                                                        modify.setRemoveId(id);
                                                        resource.groupManager(modify, hanler);
                                                    }
                                                })
                                                .create();
                                        selectGroup.show();
                                    }else{
                                        Toast.makeText(GropManager.this, "不能没有分组！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    del_alert.dismiss();
                                }
                            })
                            .create();
                    del_alert.show();
                }
            });
            return convertView;
        }
    }
}
