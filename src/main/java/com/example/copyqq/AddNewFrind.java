package com.example.copyqq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewFrind extends AppCompatActivity implements View.OnClickListener{
    public static float y;
    View main;
    Button select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //布局转换器，
        main = View.inflate(this, R.layout.activity_add_new_frind, null);
        //设置布局
        setContentView(main);
        //搜索按钮
        select = (Button) main.findViewById(R.id.select_btn);
        select.setOnClickListener(this);
        //返回
        TextView back = (TextView) main.findViewById(R.id.back);
        back.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_btn:
                //得到搜索按钮的高度
                y = select.getTop();
                //实例化上移动画
                TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,-y*9);
                //设置持续时间
                translateAnimation.setDuration(500);
                //设置最后停在所在位置为true
                translateAnimation.setFillAfter(true);
                //设置动画监听
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //当动画开始时设置按钮不可点击
                        select.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //当动画结束则跳转到搜索界面
                        startActivity(new Intent(AddNewFrind.this, Search_frinds.class));
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                main.startAnimation(translateAnimation);
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }
}
