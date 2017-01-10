package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Tools.IdArray;
import com.example.copyqq.R;
import com.example.lkl.socketlibrary.tools.resource;

/**
 * Created by MBENBEN on 2016/12/4.
 */
public class IDListViewAdapter extends BaseAdapter {
    private Context context;
    private boolean canEdit = true;
    public IDListViewAdapter(Context context) {
        this.context = context;
    }
    private MyListerner myListerner;
    public interface MyListerner {
        void setOnUserIdClick(TextView Idview);
        void setDelIdclick(View view);
    }
    public void setMyListerner(MyListerner myListerner){
        this.myListerner = myListerner;
    }
    public void setCanEdit(boolean canEdit){
        this.canEdit = canEdit;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return IdArray.getSize();
    }

    @Override
    public String getItem(int position) {
        return IdArray.getItem(1, position)[0];
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
        if(myListerner != null){
            final MyHolder finalMyHolder = myHolder;
            myHolder.touxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListerner.setOnUserIdClick(finalMyHolder.tv_id);
                }
            });
            myHolder.tv_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListerner.setOnUserIdClick(finalMyHolder.tv_id);
                }
            });
            myHolder.iv_quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListerner.setDelIdclick(v);
                }
            });
            myHolder.iv_online.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListerner.setOnUserIdClick(finalMyHolder.tv_id);
                }
            });
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
