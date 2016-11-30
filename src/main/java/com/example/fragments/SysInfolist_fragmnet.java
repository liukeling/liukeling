package com.example.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.Tools.resource;
import com.example.copyqq.CheckGroupActivity;
import com.example.copyqq.R;
import com.example.copyqq.Sysinfo_Activity;

import java.util.ArrayList;
import java.util.HashMap;

import comm.SysInfo;

public class SysInfolist_fragmnet extends Fragment {

    private SysInfo linshiinfo;
    private ListView list_view;
    private MyAdapter adapter;

    private SysInfolist_fragmnet() {

    }

    public static SysInfolist_fragmnet newInstance() {
        SysInfolist_fragmnet sysInfolist_fragmnet = new SysInfolist_fragmnet();

        return sysInfolist_fragmnet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getContext(), R.layout.frindlistfragment_layout, null);
        list_view = (ListView) v.findViewById(R.id.list_view);
        adapter = new MyAdapter();
        list_view.setAdapter(adapter);
        return v;
    }

    public void reflushSysInfo() {
        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends BaseAdapter {

        ArrayList<HashMap<String, String>> spinnerData = new ArrayList<>();

        @Override
        public int getCount() {
            return resource.Sysinfos.size();
        }

        @Override
        public Object getItem(int position) {
            return resource.Sysinfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(SysInfolist_fragmnet.this.getContext(), R.layout.frindlist_item, null);
            }

            LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll_huifu);

            final SysInfo info = resource.Sysinfos.get(position);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Sysinfo_Activity.class);
                    intent.putExtra("sinfo", info);
                    getActivity().startActivity(intent);
                }
            });
            final int type = info.getType_huifu();
            if(info.isRead()){
                ll.setVisibility(View.GONE);
            }else{
                ll.setVisibility(View.VISIBLE);
            }
            if (type == 0) {
                HashMap<String, String> hm1 = new HashMap<>();
                hm1.put("key", "同意");
                HashMap<String, String> hm3 = new HashMap<>();
                hm3.put("key", "拒绝");
                spinnerData.clear();
                spinnerData.add(hm1);
                spinnerData.add(hm3);
            } else if (type == 1) {

                HashMap<String, String> hm1 = new HashMap<>();
                hm1.put("key", "确定");
                spinnerData.clear();
                spinnerData.add(hm1);
            }
            TextView tv_title2 = (TextView) convertView.findViewById(R.id.title2);
            tv_title2.setText(info.getNeirong());
            Spinner spinner = (Spinner) convertView.findViewById(R.id.type);
            Button btn_ok = (Button) convertView.findViewById(R.id.btn_ok);
            spinner.setAdapter(new SimpleAdapter(SysInfolist_fragmnet.this.getContext(), spinnerData, R.layout.textviewitem, new String[]{"key"}, new int[]{R.id.tv}));
            final int[] spinner_position = {-1};
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinner_position[0] = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinner_position[0] = -1;
                }
            });

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = spinner_position[0];
                    String s = "";
                    if (position != -1) {
                        s = spinnerData.get(position).get("key");
                    } else {
                        s = "未选择";
                    }
                    switch (type) {
                        case 0:
                            if ("同意".equals(s)) {
                                Intent intent = new Intent(SysInfolist_fragmnet.this.getContext(), CheckGroupActivity.class);
                                linshiinfo = info;
                                startActivityForResult(intent, 0);
                            } else if ("拒绝".equals(s)) {
                                resource.addFrindResponse(1, info, -1);
                            }
                            break;
                        case 1:
                            resource.setSysinfoRead(info);
                            break;
                    }
                }
            });
            return convertView;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == 0) {
            int i = data.getIntExtra("result", -1);
            if (i != -1) {
                resource.addFrindResponse(0, linshiinfo, i);
            }
        }

    }
}
