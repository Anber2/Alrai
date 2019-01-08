package com.mawaqaa.alrai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mawaqaa.alrai.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;
    Animation anim;
    LinearLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        anim = AnimationUtils.loadAnimation(this, R.anim.splashanimation);

        splashLayout = (LinearLayout) findViewById(R.id.splash_layout);
        splashLayout.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
