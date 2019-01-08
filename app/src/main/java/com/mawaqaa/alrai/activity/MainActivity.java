package com.mawaqaa.alrai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mawaqaa.alrai.R;

public class MainActivity extends AppCompatActivity {

    LinearLayout mediaLayout;
    TextView textView_main_media, textView_main_drama, textView_main_rima, textView_main_alnasher, textView_main_newspaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaLayout = (LinearLayout) findViewById(R.id.media_layout);

        textView_main_media = (TextView) findViewById(R.id.textView_main_media);
        textView_main_drama = (TextView) findViewById(R.id.textView_main_drama);
        textView_main_rima = (TextView) findViewById(R.id.textView_main_rima);
        textView_main_alnasher = (TextView) findViewById(R.id.textView_main_alnasher);
        textView_main_newspaper = (TextView) findViewById(R.id.textView_main_newspaper);

        textView_main_newspaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsPaperActivity.class);
                startActivity(intent);
            }
        });

      //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        textView_main_alnasher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlNasherActivity.class);
                startActivity(intent);
            }
        });

        textView_main_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        textView_main_rima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RimaActivity.class);
                startActivity(intent);
            }
        });
    }
}
