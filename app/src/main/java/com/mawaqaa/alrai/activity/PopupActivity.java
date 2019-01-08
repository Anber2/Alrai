package com.mawaqaa.alrai.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.mawaqaa.alrai.adapter.NewsAdapter;
import com.mawaqaa.alrai.other.NewsClass;
import com.mawaqaa.alrai.other.SQLHelper;
import com.mawaqaa.alrai.other.SessionClass;
import com.mawaqaa.alrai.other.urlClass;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by HP on 5/30/2017.
 */

public class PopupActivity extends Activity {

    ListView Popup_LV;
    ArrayList<String> results;
    TextView selectedCat;
    // ProgressDialog progressBar;
    Cursor newsCurs;
    SQLHelper dbhelper;
    String newsSubCatID;
    NewsAdapter newsAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_withoutsrch);
        dbhelper = new SQLHelper(this);
        dbhelper.open();
        selectedCat = (TextView) findViewById(R.id.popup_title);

        Popup_LV = (ListView) findViewById(R.id.popLV);

        Popup_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {

                if (SessionClass.popupFlag.equalsIgnoreCase("newsCategory")) {

                    if (newsCurs.moveToPosition(position)) {
                        SessionClass.newsSubCatID = newsCurs.getString(0);
                        newsSubCatID = SessionClass.newsSubCatID;
                        //NewsActivity.importantNewsTXT.setText(newsCurs.getString(1));
                        if (NewsActivity.mLayout != null &&
                                (NewsActivity.mLayout.getPanelState() ==
                                        SlidingUpPanelLayout.PanelState.EXPANDED ||
                                        NewsActivity.mLayout.getPanelState() ==
                                                SlidingUpPanelLayout.PanelState.ANCHORED))
                        {
                            NewsActivity.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                        getNews(newsSubCatID);
                        finish();
                    }


                }

            }

        });


        if (SessionClass.popupFlag.equalsIgnoreCase("newsCategory")) {


            fileNewsList();

            try {
                SessionClass.BackupDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                    makeVolleyRequest(urlClass.getNewsList, jsonObject);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();
    }

    private void makeVolleyRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(PopupActivity.this);
        JsonArrayRequest jr = new JsonArrayRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                processData(response);
                // progressBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                PopupActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PopupActivity.this, getApplicationContext().getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

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

    private void processData(JSONArray response) {

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
                Toast.makeText(PopupActivity.this, getApplicationContext().getString(R.string.toast_no_saved_news), Toast.LENGTH_LONG).show();
                this.finish();

            } else {
                newsAdapter = new NewsAdapter(PopupActivity.this, newsArrayList);
              //  NewsActivity.newsList.setAdapter(newsAdapter);

                /*NewsActivity.newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        NewsClass newsClass = newsAdapter.getItem(position);
                        SessionClass.newsClassObj = newsClass;
                        Intent intent = new Intent(PopupActivity.this, NewsDetailsActivity.class);
                        startActivity(intent);


//                    String selectedNewsId=newsClass.getId();
//                    getNewsDetails(selectedNewsId);
                    }
                });*/
            }


        } catch (Exception e) {
            e.toString();
        }

    }


    private void fileNewsList() {

        try {
            String query = "select id,name from news_sub_cat where parentid='" + SessionClass.newsCatID + "'";
            newsCurs = dbhelper.Select(query, null);
            results = new ArrayList<String>();
            if (newsCurs != null) {
                if (newsCurs.moveToFirst()) {
                    do {
                        String subID = newsCurs.getString(1).toString();

                        results.add(subID);

                    }
                    while (newsCurs.moveToNext());
                }

                if (results.isEmpty() || results == null) {
                    Toast.makeText(PopupActivity.this, getApplicationContext().getString(R.string.toast_no_sub_cat), Toast.LENGTH_SHORT).show();

                    this.finish();
                } else {
                    selectedCat.setText(SessionClass.selectedCat);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_checkboxes, results);
                    Popup_LV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    Popup_LV.setAdapter(adapter);
                }

            }


        } catch (Exception e) {
            e.getMessage();
        }
    }
}

