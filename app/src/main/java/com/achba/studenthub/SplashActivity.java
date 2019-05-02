package com.achba.studenthub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    TextView tvLarge, tvSmall;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash);

        tvLarge=findViewById(R.id.font);
        Animation tvLargeAnimation= AnimationUtils.loadAnimation(this, R.anim.move_top_left);
        tvLarge.startAnimation(tvLargeAnimation);

        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setVisibility(View.VISIBLE);
        Animation btnAnimation=AnimationUtils.loadAnimation(this, R.anim.fade_in_move_bottom);

        btnAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                tvSmall=findViewById(R.id.tvSmall);
                Animation tvSmallAnimation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_right);
                tvSmall.startAnimation(tvSmallAnimation);
                tvSmall.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        btnLogin.startAnimation(btnAnimation);

        ImageView imageView=findViewById(R.id.iv_book);
        Animation ivAnimation= AnimationUtils.loadAnimation(this, R.anim.move_top);
        imageView.startAnimation(ivAnimation);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                RelativeLayout r = findViewById(R.id.textLayout);
                r.setVisibility(View.VISIBLE);
            }
        }, 1500);

        /*int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }, secondsDelayed * 5000);*/

        /*TextView secondTextView;
        secondTextView = findViewById(R.id.font);
        Shader textShader=new LinearGradient(0, 0, 0, 20,
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.BLUE},
                new float[]{0.5,1},
                Shader.TileMode.CLAMP);
        secondTextView.getPaint().setShader(textShader);*/

    }

    public void onClickLogin(View view) {
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
