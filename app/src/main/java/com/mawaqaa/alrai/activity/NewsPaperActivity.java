package com.mawaqaa.alrai.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.adapter.NewsPaperAdapter;
import com.mawaqaa.alrai.other.PrefManage;
import com.mawaqaa.alrai.other.newsPaperClass;
import com.mawaqaa.alrai.other.urlClass;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;

import static com.mawaqaa.alrai.activity.NewsActivity.layoutManager;

/**
 * Created by HP on 8/14/2017.
 */

public class NewsPaperActivity extends AppCompatActivity {
    public static TextView importantNewsTXT, watchNowTXT;

    public static WebView webView;
    SlidingUpPanelLayout mLayout;
    Button alraiMenuBTN, newsSettingBTN, savedNewsBTN;
    GridView gridView;
    ProgressDialog progressBar;
    String userId;

    newsPaperClass newsPaperClass_;
    ArrayList<newsPaperClass> newsPaperClassArrayList_;

    RecyclerView recycler_view_newsPaper;

    NewsPaperAdapter newsPaperAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_paper_activity_layout);
        final ProgressDialog pd = ProgressDialog.show(this, "", "Please wait...", true);
        initView();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);

        titleText.setText(R.string.title_activity_newspaper);
        webView = (WebView) findViewById(R.id.webview_newsPaper);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(NewsPaperActivity.this, description, Toast.LENGTH_SHORT).show();
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

        webView.loadUrl(urlClass.NewsPaperUrl);

        userId = new PrefManage(NewsPaperActivity.this).getUserDetails();


        alraiMenuBTN.setMovementMethod(LinkMovementMethod.getInstance());
        alraiMenuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLayout.getPanelState() == PanelState.ANCHORED || mLayout.getPanelState() == PanelState.EXPANDED) {
                    mLayout.setPanelState(PanelState.COLLAPSED);
                } else {
                    mLayout.setAnchorPoint(0.8f);
                    mLayout.setPanelState(PanelState.EXPANDED);
                }

            }
        });


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

        recycler_view_newsPaper = (RecyclerView) findViewById(R.id.recycler_view_newsPaper);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        getNewsPaperMenuList();


    }

    private void getNewsPaperMenuList() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    newsPaperClassArrayList_ = new ArrayList<newsPaperClass>();

                    newsPaperClass_ = new newsPaperClass("Archive", urlClass.NewsPaperArchiveURL);
                    newsPaperClassArrayList_.add(newsPaperClass_);

                    newsPaperClass_ = new newsPaperClass("Dashboard", "http://newspaper.mawaqaademo.com/NewsPaper/AccountsMobile/LoginPageMobile/RegUserHomeMobile");
                    newsPaperClassArrayList_.add(newsPaperClass_);

                    newsPaperClass_ = new newsPaperClass("Logout", "http://newspaper.mawaqaademo.com/NewsPaper/MobileHome/LogoutMobile");
                    newsPaperClassArrayList_.add(newsPaperClass_);

                    newsPaperClass_ = new newsPaperClass("Login", "http://newspaper.mawaqaademo.com/NewsPaper/AccountsMobile/LoginPageMobile");
                    newsPaperClassArrayList_.add(newsPaperClass_);

                    newsPaperClass_ = new newsPaperClass("ChangePassword", "http://newspaper.mawaqaademo.com/NewsPaper/AccountsMobile/LoginPageMobile/ChangePasswordMobile");
                    newsPaperClassArrayList_.add(newsPaperClass_);

                    newsPaperClass_ = new newsPaperClass("ForgotPassword", "http://newspaper.mawaqaademo.com/NewsPaper/AccountsMobile/ForgotPasswordMobile");
                    newsPaperClassArrayList_.add(newsPaperClass_);

                    newsPaperClass_ = new newsPaperClass("ResetPassword", "http://newspaper.mawaqaademo.com/NewsPaper/AccountsMobile/ResetPasswordMobile");
                    newsPaperClassArrayList_.add(newsPaperClass_);
                    newsPaperClass_ = new newsPaperClass("SignUp", "http://newspaper.mawaqaademo.com/NewsPaper/AccountsMobile/SignupPageMobile");
                    newsPaperClassArrayList_.add(newsPaperClass_);
                    newsPaperClass_ = new newsPaperClass("Home", "http://newspaper.mawaqaademo.com/NewsPaper/MobileHome");
                    newsPaperClassArrayList_.add(newsPaperClass_);


                    newsPaperAdapter = new NewsPaperAdapter(getApplicationContext(), newsPaperClassArrayList_);

                    recycler_view_newsPaper.setAdapter(newsPaperAdapter);
                    recycler_view_newsPaper.setHasFixedSize(true);
                    recycler_view_newsPaper.setLayoutManager(layoutManager);


                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();
    }



}
