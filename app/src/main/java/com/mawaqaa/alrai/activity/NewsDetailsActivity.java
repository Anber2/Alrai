package com.mawaqaa.alrai.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.adapter.NewsAdapter;
import com.mawaqaa.alrai.other.NewsClass;
import com.mawaqaa.alrai.other.SessionClass;
import com.mawaqaa.alrai.other.urlClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsDetailsActivity extends AppCompatActivity {

    WebView newsDetailsWebview;
    NewsClass newsClass;
    ProgressDialog progressBar;
    String deviceID;
    String newsSubCatID;

    NewsAdapter newsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        newsDetailsWebview = (WebView) findViewById(R.id.news_details_webview);
        final ProgressDialog pd = ProgressDialog.show(NewsDetailsActivity.this, "", "Please wait...", true);
      //  newsClass = SessionClass.newsClassObj;

//        newsSubCatID = SessionClass.newsSubCatID;
//        getNews(newsSubCatID);

        newsDetailsWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        newsDetailsWebview.getSettings().setLoadWithOverviewMode(true);
        newsDetailsWebview.getSettings().setDisplayZoomControls(false);
        newsDetailsWebview.getSettings().setBuiltInZoomControls(false);


        newsDetailsWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(NewsDetailsActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd.show();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();

                String webUrl = newsDetailsWebview.getUrl();

            }


        });
        newsDetailsWebview.loadUrl(SessionClass.URLlinkCat);
//        newsDetailsWebview.setWebViewClient(new MyWebViewClient());
//        WebSettings webSettings = newsDetailsWebview.getSettings();
//        webSettings.setJavaScriptEnabled(true);
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    private void getNews(final String newsSubCatID) {
        //progressBar = ProgressDialog.show(PopupActivity.this, "", "Please Wait ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("CategoryId", newsSubCatID);
                    jsonObject.putOpt("StartIndex", "0");
                    jsonObject.putOpt("EndIndex", "10");
                    jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                    makeVolleyRequestSubCat(urlClass.getNewsList, jsonObject);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();
    }

    public void makeVolleyRequestSubCat(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jr = new JsonArrayRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                processSubCatData(response);
                // progressBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                   //     Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

                    }
                });
                //progressBar.dismiss();

            }
        });
        jr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                2,
                2));
        queue.add(jr);
    }

    public void processSubCatData(JSONArray response) {

        ArrayList<NewsClass> newsArrayList = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {

                JSONObject jsonObject = response.getJSONObject(i);
                String newsId = jsonObject.getString("Id");
                String newsImageUrl = jsonObject.getString("ImageUrl");
                String newsTitle = jsonObject.getString("Title");
                String newsTotalComments = jsonObject.getString("TotalComments");
                String newsTotalShares = jsonObject.getString("TotalShares");
                String newsTotalViews = jsonObject.getString("TotalViews");
                String newsURL = jsonObject.getString("URL");
                NewsClass newsClass = new NewsClass(newsId, newsImageUrl, newsTitle, newsTotalComments, newsTotalShares, newsTotalViews, newsURL);
                newsArrayList.add(newsClass);
            }

            if (newsArrayList.isEmpty() || newsArrayList == null) {
                Toast.makeText(this, getApplicationContext().getString(R.string.toast_no_saved_news), Toast.LENGTH_LONG).show();
                this.finish();

            } else {


            }


        } catch (Exception e) {
            e.toString();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_news:
                saveNews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void saveNews() {

        progressBar = ProgressDialog.show(NewsDetailsActivity.this, "", "Please Wait ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("DeviceId", deviceID);
                    jsonObject.putOpt("NewsId", newsClass.getId());
                    jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                    makeVolleyRequest(urlClass.insertReadLater, jsonObject);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();

    }

    private void makeVolleyRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(NewsDetailsActivity.this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                processData(response);
                progressBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                NewsDetailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsDetailsActivity.this, getApplicationContext().getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

                    }
                });
                progressBar.dismiss();

            }
        });


        jr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                2,
                2));
        queue.add(jr);
    }

    private void processData(JSONObject response) {
        try {
            boolean status = response.getBoolean("Status");
            String Message = response.getString("Message");

            Toast.makeText(NewsDetailsActivity.this, getApplicationContext().getString(R.string.toast_save_news_sucss), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class MyWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
}