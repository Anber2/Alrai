package com.mawaqaa.alrai.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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
import com.mawaqaa.alrai.adapter.CategoryAdapter;
import com.mawaqaa.alrai.adapter.NewsAdapter;
import com.mawaqaa.alrai.adapter.SubCategoryAdapter;
import com.mawaqaa.alrai.other.NewsCategoryClass;
import com.mawaqaa.alrai.other.NewsClass;
import com.mawaqaa.alrai.other.PrefManage;
import com.mawaqaa.alrai.other.SQLHelper;
import com.mawaqaa.alrai.other.SubCategoryDataClass;
import com.mawaqaa.alrai.other.urlClass;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class NewsActivity extends AppCompatActivity {
    private static final String TAG = "NewsActivity";


    public static TextView importantNewsTXT, watchNowTXT;
    // public static ListView newsList;
    public static SlidingUpPanelLayout mLayout;
    static LinearLayoutManager layoutManager, layoutManager2;
    Button alraiMenuBTN, newsSettingBTN, savedNewsBTN;
    GridView gridView;
    CategoryAdapter categoryAdapter;
    ProgressDialog progressBarNew;

    NewsAdapter newsAdapter;
    SQLHelper dbhelper;
    String userId;
    Cursor newsCurs, categoryCursor, subCategoryCurs;
    RecyclerView recycler_view, recycler_view_top_news_list;
    ArrayList<SubCategoryDataClass> subCategoryDataClassArrayList, subCategoryDataClassArrayList2;
    SubCategoryAdapter subCategoryAdapter, subCategoryAdapter2;
    SubCategoryDataClass subCategoryDataClass, subCategoryDataClass2;
    ArrayList<NewsCategoryClass> newsCategoryClassArrayList_;
    NewsClass newsClass_;
    ArrayList<NewsClass> newsArrayList;

    boolean flag_loading = false;

    int start = 0, end = 10;
    boolean isScrooled = false;
    private int preLast;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        titleText.setText(R.string.title_activity_news);

        initView();
        getCategory();
        getSubCategory();
        getNews(start, end);

        userId = new PrefManage(NewsActivity.this).getUserDetails();

        alraiMenuBTN.setMovementMethod(LinkMovementMethod.getInstance());
        alraiMenuBTN.setOnClickListener(new OnClickListener() {
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

        newsSettingBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userId.equals("") || userId.isEmpty()) {
                    Intent intent = new Intent(NewsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(NewsActivity.this, SettingActivity.class);
                    startActivity(intent);
                }


            }
        });

        savedNewsBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, SavedNewsActivity.class);
                startActivity(intent);
            }
        });

/*
        newsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
//                if (newsList.getLastVisiblePosition() == newsList.getAdapter().getCount() -1 &&
//                        newsList.getChildAt(newsList.getChildCount() - 1).getBottom() <= newsList.getHeight())
//                {
//                    start = end +  1;
//                    end = end *2;
//                    isScrooled=true;
//
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run()
//                        {
//                            getNews(start ,end);
//
//
//                        }
//                    }).start();
//
//
//
//
//
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

*/


    }


    private void initView() {

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setTouchEnabled(false);
        //newsList = (ListView) findViewById(R.id.top_news_list);
        gridView = (GridView) findViewById(R.id.news_grid);

        alraiMenuBTN = (Button) findViewById(R.id.alrai_menu_BTN);
        newsSettingBTN = (Button) findViewById(R.id.news_setting_btn);
        savedNewsBTN = (Button) findViewById(R.id.saved_news_btn);

        recycler_view_top_news_list = (RecyclerView) findViewById(R.id.recycler_view_top_news_list);
        layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);


        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        importantNewsTXT = (TextView) findViewById(R.id.important_news_txt);
        watchNowTXT = (TextView) findViewById(R.id.watch_now_txt);

        importantNewsTXT.setText(getApplicationContext().getResources().getString(R.string.important_news_txt));
        watchNowTXT.setText(getApplicationContext().getResources().getString(R.string.watch_now_txt));


        dbhelper = new SQLHelper(getApplicationContext());
        dbhelper.open();


        newsArrayList = new ArrayList<>();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recycler_view.setVisibility(View.VISIBLE);
                importantNewsTXT.setVisibility(View.GONE);
                watchNowTXT.setVisibility(View.GONE);

                getSubCategoryList(newsCategoryClassArrayList_.get(position).getId());

            }
        });

        recycler_view_top_news_list
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        totalItemCount = layoutManager2.getItemCount();
                        lastVisibleItem = layoutManager2
                                .findLastVisibleItemPosition();
                        if (!loading
                                && totalItemCount <= (lastVisibleItem + visibleThreshold)) {


                            start = end + 1;
                            end = end * 2;

                            getNews(start, end);
                            loading = true;


                        }
                    }
                });


    }

    private void getCategory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("CategoryId", "0");
                    jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                    getCatgRequest(urlClass.categoryURL, jsonObject);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();
    }

    private void getSubCategory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("CategoryId", "1");
                    jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                    getSubCatgRequest(urlClass.categoryURL, jsonObject);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();
    }

    private void getNews(final int start, final int end) {

        progressBarNew = ProgressDialog.show(NewsActivity.this, "", "Please Wait ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("StartIndex", start);
                    jsonObject.putOpt("EndIndex", end);
                    jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                    makeVolleyRequestNews(urlClass.getTopNewsList, jsonObject);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();

    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void getCatgRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(NewsActivity.this);
        JsonArrayRequest jr = new JsonArrayRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                processCategoryandSubCat(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                NewsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsActivity.this, getApplicationContext().getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

                    }
                });

            }
        });


        jr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                2,
                2));
        queue.add(jr);

    }

    private void getSubCatgRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(NewsActivity.this);
        JsonArrayRequest jr = new JsonArrayRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                processCategoryandSubCat(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                NewsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsActivity.this, getApplicationContext().getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

                    }
                });


            }
        });


        jr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 30,
                2,
                2));
        queue.add(jr);

    }


    private void processCategoryandSubCat(JSONArray response) {

        newsCategoryClassArrayList_ = new ArrayList<>();
        dbhelper.Delete("delete from news_sub_cat");

        try {

            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                String id = jsonObject.getString("Id");
                String name = jsonObject.getString("Name");
                String parentId = jsonObject.getString("ParentId");

                String query = "insert into news_sub_cat values('" + id + "','" + name + "','" + parentId + "')";
                String res = dbhelper.Insert(query);


            }

            categoryCursor = dbhelper.Select("select id,name,parentID from news_sub_cat where parentID='0'", null);
            if (categoryCursor.moveToFirst()) {
                do {
                    NewsCategoryClass newsCategoryClass = new NewsCategoryClass(categoryCursor.getString(0), categoryCursor.getString(1), categoryCursor.getString(2));
                    newsCategoryClassArrayList_.add(newsCategoryClass);

                }
                while (categoryCursor.moveToNext());

                categoryAdapter = new CategoryAdapter(NewsActivity.this, newsCategoryClassArrayList_);
                gridView.setAdapter(categoryAdapter);
            }


        } catch (Exception e) {

            e.toString();
        }
    }

    private void getSubCategoryList(String catID) {

        try {
            String query = "select id,name from news_sub_cat where parentid='" + catID + "'";
            newsCurs = dbhelper.Select(query, null);
            subCategoryDataClassArrayList = new ArrayList<SubCategoryDataClass>();

            if (newsCurs != null) {
                if (newsCurs.moveToFirst()) {
                    do {
                        String subID = newsCurs.getString(0).toString();
                        String subName = newsCurs.getString(1).toString();

                        subCategoryDataClass = new SubCategoryDataClass(subID, subName);

                        subCategoryDataClassArrayList.add(subCategoryDataClass);

                    }
                    while (newsCurs.moveToNext());
                }

                if (subCategoryDataClassArrayList.isEmpty() || subCategoryDataClassArrayList == null) {
                    Toast.makeText(this, getApplicationContext().getString(R.string.toast_no_sub_cat), Toast.LENGTH_SHORT).show();

                } else {
                    subCategoryAdapter = new SubCategoryAdapter(getApplicationContext(), subCategoryDataClassArrayList);


                    recycler_view.setAdapter(subCategoryAdapter);
                    recycler_view.setHasFixedSize(true);
                    recycler_view.setLayoutManager(layoutManager);
                }
            }


        } catch (Exception e) {
            e.getMessage();
        }

    }


    private void makeVolleyRequestNews(String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(NewsActivity.this);
        JsonArrayRequest jr = new JsonArrayRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                progressBarNew.dismiss();
                processDataNews(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                NewsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsActivity.this, getApplicationContext().getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();
                        progressBarNew.dismiss();
                    }
                });

            }
        });


        jr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 50,
                2,
                2));
        queue.add(jr);

    }

    private void processDataNews(JSONArray response) {


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
                newsClass_ = new NewsClass(newsId, newsImageUrl, newsTitle, newsTotalComments, newsTotalShares, newsTotalViews, newsURL);
                newsArrayList.add(newsClass_);
            }
            newsAdapter = new NewsAdapter(NewsActivity.this, newsArrayList);
            recycler_view_top_news_list.setAdapter(newsAdapter);
            recycler_view_top_news_list.setHasFixedSize(true);
            recycler_view_top_news_list.setLayoutManager(layoutManager2);
            recycler_view_top_news_list.getLayoutManager().scrollToPosition(start-1);
            if (loading = true) {
                loading = false;
            }

        } catch (Exception e) {
            progressBarNew.dismiss();
            e.toString();
        }
    }

}