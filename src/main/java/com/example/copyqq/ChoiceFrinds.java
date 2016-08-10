package com.example.copyqq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.MyViews.MyExpandableListView;
import com.example.Tools.resource;

import java.util.HashMap;

import comm.user;

public class ChoiceFrinds extends AppCompatActivity implements View.OnClickListener {

    private TextView cancel, ok;
    private EditText search;
    private MyExpandableListView frinds;
    private Myadapter myadapter;
    private user intentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intentUser = (user) getIntent().getSerializableExtra("user");

        setContentView(R.layout.activity_choice_frinds);
        cancel = (TextView) findViewById(R.id.cancel);
        ok = (TextView) findViewById(R.id.ok);
        search = (EditText) findViewById(R.id.search);
        frinds = (MyExpandableListView) findViewById(R.id.frinds);

        frinds.setGroupIndicator(null);
        myadapter = new Myadapter();
        frinds.setAdapter(myadapter);

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                //TODO
                break;
            case R.id.cancel:
                onBackPressed();
                break;
        }
    }

    class Myadapter extends BaseExpandableListAdapter{

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
            return resource.childs.get(groupPosition).get(childPosition);
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
            String group = resource.gruops.get(groupPosition).get("group");
            View view = View.inflate(ChoiceFrinds.this, R.layout.checkfrind_one, null);

            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText(group);

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
            hm.put(groupPosition, childPosition);
            user u = resource.childs.get(groupPosition).get(childPosition).get("child");
//
            View v = View.inflate(ChoiceFrinds.this, R.layout.checkfrind_two, null);
//            v.find
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkbox);

            if(intentUser != null && u.equals(intentUser)){
                checkBox.setChecked(true);
                checkBox.setClickable(false);
            }

            return v;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
