package com.mawaqaa.alrai.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.adapter.RimaAdapter;
import com.mawaqaa.alrai.other.PrefManage;
import com.mawaqaa.alrai.other.newsPaperClass;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

/**
 * Created by HP on 8/29/2017.
 */

public class RimaActivity extends AppCompatActivity {

    public static TextView importantNewsTXT, watchNowTXT;
    static LinearLayoutManager layoutManager;
    public static WebView webView;
    SlidingUpPanelLayout mLayout;
    Button alraiMenuBTN, newsSettingBTN, savedNewsBTN;
    GridView gridView;
    ProgressDialog progressBar;
    String userId;
    newsPaperClass newsPaperClass_;
    ArrayList<newsPaperClass> newsPaperClassArrayList_;
    RecyclerView recycler_view_Rima;
    RimaAdapter newsPaperAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rima_layout_activity);
        final ProgressDialog pd = ProgressDialog.show(this, "", "Please wait...", true);
        initView();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);

        titleText.setText(R.string.title_activity_rima);
        webView = (WebView) findViewById(R.id.webview_Rima);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
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

        webView.loadUrl("http://mawaqaa_rima.alraimedia.com/ar/Home/Mobile_HomeAR/");

        userId = new PrefManage(this).getUserDetails();


        initView();

    }

    private void initView() {

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setEnabled(false);
        mLayout.setTouchEnabled(false);

        gridView = (GridView) findViewById(R.id.news_grid);
        importantNewsTXT = (TextView) findViewById(R.id.important_news_txt);
        watchNowTXT = (TextView) findViewById(R.id.watch_now_txt);
        alraiMenuBTN = (Button) findViewById(R.id.alrai_menu_BTN);
        newsSettingBTN = (Button) findViewById(R.id.news_setting_btn);
        savedNewsBTN = (Button) findViewById(R.id.saved_news_btn);

        recycler_view_Rima = (RecyclerView) findViewById(R.id.recycler_view_Rima);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        getRimaMenuList();



    }

    private void getRimaMenuList() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    newsPaperClassArrayList_ = new ArrayList<newsPaperClass>();


                    newsPaperClass_ = new newsPaperClass("عربي", "http://mawaqaa_rima.alraimedia.com/ar/Home/Mobile_HomeAR/");
                    newsPaperClassArrayList_.add(newsPaperClass_);

                    newsPaperClass_ = new newsPaperClass("EN", "http://mawaqaa_rima.alraimedia.com/Home/Mobile_HomeEN/");
                    newsPaperClassArrayList_.add(newsPaperClass_);

                    newsPaperAdapter = new RimaAdapter(getApplicationContext(), newsPaperClassArrayList_);

                    recycler_view_Rima.setAdapter(newsPaperAdapter);
                    recycler_view_Rima.setHasFixedSize(true);
                    recycler_view_Rima.setLayoutManager(layoutManager);


                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();
    }

}
