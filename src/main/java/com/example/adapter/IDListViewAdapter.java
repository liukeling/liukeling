package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Tools.resource;
import com.example.copyqq.R;

import java.util.Iterator;

/**
 * Created by MBENBEN on 2016/12/4.
 */
public class IDListViewAdapter extends BaseAdapter {
    private Context context;
    private boolean canEdit = true;
    public IDListViewAdapter(Context context) {
        this.context = context;
    }
    public void setCanEdit(boolean canEdit){
        this.canEdit = canEdit;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return resource.idSet.size();
    }

    @Override
    public String getItem(int position) {
        Iterator<String> iterator = resource.idSet.iterator();
        int k = 0;
        while (iterator.hasNext()) {
            String itemData = iterator.next();
            if (k == position) {
                return itemData;
            }
            k++;
        }
        return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.popup_itemlayout, null);
            myHolder = new MyHolder(convertView);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        if (myHolder == null) {
            myHolder = new MyHolder(convertView);
        }
        String ID = getItem(position);
        myHolder.tv_id.setText(ID);
        if(canEdit){
            myHolder.iv_quxiao.setVisibility(View.VISIBLE);
            myHolder.iv_online.setVisibility(View.GONE);
        }else{
            myHolder.iv_quxiao.setVisibility(View.GONE);
            if(resource.Myzhanghao.equals(ID)){
                myHolder.iv_online.setVisibility(View.VISIBLE);
            }else{
                myHolder.iv_online.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private class MyHolder {
        public ImageView touxiao;
        public TextView tv_id;
        public ImageView iv_quxiao;
        public ImageView iv_online;

        public MyHolder(View convertView) {
            touxiao = (ImageView) convertView.findViewById(R.id.iv_touxiang);
            tv_id = (TextView) convertView.findViewById(R.id.tv_id);
            iv_quxiao = (ImageView) convertView.findViewById(R.id.iv_quxiao);
            iv_online = (ImageView) convertView.findViewById(R.id.iv_online);
        }
    }
}
