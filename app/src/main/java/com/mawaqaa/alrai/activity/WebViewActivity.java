package com.mawaqaa.alrai.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.other.PrefManage;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by HP on 8/22/2017.
 */

public class WebViewActivity extends AppCompatActivity {


    WebView webView;
    SlidingUpPanelLayout mLayout;
    Button alraiMenuBTN, newsSettingBTN, savedNewsBTN;
    GridView gridView;
    ProgressDialog progressBar;
    String userId;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity_layout);
        final ProgressDialog pd = ProgressDialog.show(this, "", "Please wait...", true);
        initView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        titleText.setText(R.string.title_activity_alnasher);


        webView = (WebView) findViewById(R.id.webview_alnasher);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);


        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd.show();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();


            }


        });

        webView.loadUrl(PrefManage.getAlNasherUri(this));


    }

    private void initView() {




        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setTouchEnabled(false);
        mLayout.setTouchEnabled(false);

        gridView = (GridView) findViewById(R.id.news_grid);

        alraiMenuBTN = (Button) findViewById(R.id.alrai_menu_BTN);
        newsSettingBTN = (Button) findViewById(R.id.news_setting_btn);
        savedNewsBTN = (Button) findViewById(R.id.saved_news_btn);


    }

}
