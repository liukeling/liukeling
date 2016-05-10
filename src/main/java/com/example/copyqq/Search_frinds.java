package com.example.copyqq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Search_frinds extends AppCompatActivity implements View.OnClickListener {

    private EditText string_select;
    private View main;
    private boolean back = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = View.inflate(this, R.layout.activity_search_frinds, null);
//        setContentView(R.layout.activity_search_frinds);
        setContentView(main);
        string_select = (EditText) main.findViewById(R.id.string_select);
        final Button btn_ok = (Button) main.findViewById(R.id.btn_ok);
        final ImageView ed_quxiao = (ImageView) main.findViewById(R.id.ed_quxiao);
        ed_quxiao.setVisibility(View.INVISIBLE);
        string_select.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String e_s = string_select.getText().toString();
                if (e_s != null && !"".equals(e_s)) {
                    btn_ok.setText("搜索");
                    ed_quxiao.setVisibility(View.VISIBLE);
                } else {
                    btn_ok.setText("取消");
                    ed_quxiao.setVisibility(View.INVISIBLE);
                }
            }
        });
        btn_ok.setOnClickListener(this);
        ed_quxiao.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if(back) {
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, AddNewFrind.y * 9);
            animation.setDuration(1000);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    back = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    startActivity(new Intent(Search_frinds.this, AddNewFrind.class));
                    Search_frinds.super.onBackPressed();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setFillAfter(true);
            main.startAnimation(animation);

        }else{

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ed_quxiao:
                string_select.setText("");
                break;
            case R.id.btn_ok:
                String s = string_select.getText().toString();
                if("".equals(s) || s == null){
                    onBackPressed();
                }else{

                }
                break;
        }
    }
}
