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
        main = View.inflate(this, R.layout.activity_add_new_frind, null);
        setContentView(main);
        select = (Button) main.findViewById(R.id.select_btn);
        select.setOnClickListener(this);
        TextView back = (TextView) main.findViewById(R.id.back);
        back.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_btn:
                y = select.getTop();
                TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,-y*9);
                translateAnimation.setDuration(500);
                translateAnimation.setFillAfter(true);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        select.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
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
