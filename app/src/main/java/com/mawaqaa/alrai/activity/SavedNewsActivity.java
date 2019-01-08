package com.mawaqaa.alrai.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.adapter.NewsDetailAdapter;
import com.mawaqaa.alrai.other.NewsClass;
import com.mawaqaa.alrai.other.SessionClass;
import com.mawaqaa.alrai.other.urlClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SavedNewsActivity extends AppCompatActivity {

    ListView savedNewsListView;
    String deviceID;
    ProgressDialog progressBar;
    NewsDetailAdapter newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_news);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        titleText.setText(R.string.title_activity_saved_news);

        initView();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        getSavedNews(deviceID);

    }

    private void getSavedNews(final String deviceID) {

        progressBar = ProgressDialog.show(SavedNewsActivity.this, "", "Please Wait ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("DeviceId", deviceID);
                    jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                    makeVolleyRequest(urlClass.getReadLaterNewsList, jsonObject);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();
    }



    private void makeVolleyRequest(String url, JSONObject jsonObject) {


        RequestQueue queue = Volley.newRequestQueue(SavedNewsActivity.this);
        JsonArrayRequest jr = new JsonArrayRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                processDataNews(response);
                progressBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                SavedNewsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SavedNewsActivity.this, getApplicationContext().getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

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

    private void processDataNews(JSONArray response) {

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

            if(newsArrayList.isEmpty()||newsArrayList==null){

                Toast.makeText(SavedNewsActivity.this, getApplicationContext().getString(R.string.toast_no_saved_news), Toast.LENGTH_LONG).show();
                this.finish();

            }else{
                newsAdapter = new NewsDetailAdapter(SavedNewsActivity.this, newsArrayList);
                savedNewsListView.setAdapter(newsAdapter);

                savedNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        NewsClass newsClass = newsAdapter.getItem(position);
                        SessionClass.newsClassObj = newsClass;
                        Intent intent = new Intent(SavedNewsActivity.this, NewsDetailsActivity.class);
                        startActivity(intent);


//                    String selectedNewsId=newsClass.getId();
//                    getNewsDetails(selectedNewsId);
                    }
                });
            }



        } catch (Exception e) {
            e.toString();
        }

    }

    private void initView() {
        savedNewsListView= (ListView) findViewById(R.id.save_news_list);

    }

}
