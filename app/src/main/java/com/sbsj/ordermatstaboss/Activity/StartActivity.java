package com.sbsj.ordermatstaboss.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sbsj.ordermatstaboss.R;

public class StartActivity extends AppCompatActivity {
    private ImageView imgAndroid;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();
        startLoading();
    }

    private void initView() {
        imgAndroid = (ImageView) findViewById(R.id.start_logo);
        anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        imgAndroid.setAnimation(anim);

    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}