package com.mawaqaa.alrai.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.other.PrefManage;
import com.mawaqaa.alrai.other.urlClass;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by HP on 8/13/2017.
 */

public class AlNasherActivity extends AppCompatActivity {

    public static TextView importantNewsTXT, watchNowTXT;
    WebView webView;
    SlidingUpPanelLayout mLayout;
    Button alraiMenuBTN, newsSettingBTN, savedNewsBTN;
    GridView gridView;
    ProgressDialog progressBar;
    String userId;
    LinearLayout layout_change, layout_subscr, layout_comp, layout_pose;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alnasher_activity_layout);
        //final ProgressDialog pd = ProgressDialog.show(this, "", "Please wait...", true);
        initView();

        userId = new PrefManage(AlNasherActivity.this).getUserDetails();

    }

    private void initView() {


        layout_change = (LinearLayout) findViewById(R.id.layout_change);
        layout_subscr = (LinearLayout) findViewById(R.id.layout_subscr);
        layout_comp = (LinearLayout) findViewById(R.id.layout_comp);
        layout_pose = (LinearLayout) findViewById(R.id.layout_pose);

        layout_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManage.setAlNasherUri(AlNasherActivity.this, urlClass.AlnasherUrl_changeAddress);
                Intent intent = new Intent(AlNasherActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        layout_subscr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManage.setAlNasherUri(AlNasherActivity.this, urlClass.AlnasherUrl_subscribeForRenew);
                Intent intent = new Intent(AlNasherActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        layout_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManage.setAlNasherUri(AlNasherActivity.this, urlClass.AlnasherUrl_complains);
                Intent intent = new Intent(AlNasherActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        layout_pose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManage.setAlNasherUri(AlNasherActivity.this, urlClass.AlnasherUrl_stopSubscription);
                Intent intent = new Intent(AlNasherActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });


        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setTouchEnabled(false);

        gridView = (GridView) findViewById(R.id.news_grid);

        alraiMenuBTN = (Button) findViewById(R.id.alrai_menu_BTN);
        newsSettingBTN = (Button) findViewById(R.id.news_setting_btn);
        savedNewsBTN = (Button) findViewById(R.id.saved_news_btn);


    }


}
