package com.ljt.fastlivery.activity;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ljt.fastlivery.R;
import com.ljt.fastlivery.utils.binding.Bind;
import com.ljt.fastlivery.utils.binding.ViewBinder;

public class LoginActivity extends AppCompatActivity {

   @Bind(R.id.iv_login_bg)
   private ImageView bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewBinder.bind(this);
        initBgData();
    }

    private void initBgData() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                bg.startAnimation(animation);
            }
        },200);
    }
}
