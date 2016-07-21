package com.example.copyqq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Tools.resource;

import java.util.HashMap;

public class GropManager extends AppCompatActivity {
    TextView ok;
    TextView addgroup;
    ListView grops;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grop_manager);
        ok = (TextView) findViewById(R.id.ok);
        addgroup = (TextView) findViewById(R.id.addgroup);
        grops = (ListView) findViewById(R.id.grops);
        adapter = new MyAdapter();
        grops.setAdapter(adapter);
    }
    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return resource.gruops.size();
        }

        @Override
        public Object getItem(int position) {
            return resource.gruops.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(GropManager.this, R.layout.groupmanager_item, null);
            }

            HashMap<String, String> hm = (HashMap<String, String>) resource.gruops.get(position);

            String itemName = hm.get("group");
            TextView tv_name = (TextView) convertView.findViewById(R.id.groupname);
            tv_name.setText(itemName+"");

            return convertView;
        }
    }
}
