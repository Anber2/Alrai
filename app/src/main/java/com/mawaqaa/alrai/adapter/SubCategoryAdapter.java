package com.mawaqaa.alrai.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mawaqaa.alrai.activity.NewsDetailsActivity;
import com.mawaqaa.alrai.other.NewsClass;
import com.mawaqaa.alrai.other.SessionClass;
import com.mawaqaa.alrai.other.SubCategoryDataClass;
import com.mawaqaa.alrai.other.urlClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by HP on 8/27/2017.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {


    //List<ContactsContract.Contacts.Data> horizontalList = Collections.emptyList();
    Context context;
    String subCatID = "";
    ArrayList<SubCategoryDataClass> subCatClassArray_;
    SubCategoryDataClass subCategoryDataClass;
    NewsClass newsClass;
    ArrayList<NewsClass> newsArrayList;

     int pos=0;
   // ProgressDialog progressBarNew;
    public SubCategoryAdapter(Context context, ArrayList<SubCategoryDataClass> subCatClassArray) {

        this.context = context;
        this.subCatClassArray_ = subCatClassArray;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_cat_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.TextView_SubCat.setText(this.subCatClassArray_.get(position).getSubCategoryName());


        holder.TextView_SubCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pos=position;
                subCatID = subCatClassArray_.get(position).getSubCategoryID();

                getNews(subCatID);



            }
        });
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TextView_SubCat;

        public MyViewHolder(View view) {
            super(view);
            TextView_SubCat = (TextView) view.findViewById(R.id.TextView_SubCat);
        }
    }

    private void getNews(final String newsSubCatID) {
        //progressBarNew = ProgressDialog.show(context, "", "Please Wait ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("CategoryId", newsSubCatID);
                    jsonObject.putOpt("StartIndex", "0");
                    jsonObject.putOpt("EndIndex", "15");
                    jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                    makeVolleyRequestSubCat(urlClass.getNewsList, jsonObject);
                } catch (Exception xx)
                {
                   // progressBarNew.dismiss();
                    xx.toString();
                }
            }
        }).start();
    }

    public void makeVolleyRequestSubCat(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jr = new JsonArrayRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                processSubCatData(response);
              //  progressBarNew.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

                    }
                });
              //  progressBarNew.dismiss();

            }
        });
        jr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                2,
                2));
        queue.add(jr);
    }


    private void processSubCatData(JSONArray response) {

      newsArrayList = new ArrayList<>();

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
                 newsClass = new NewsClass(newsId, newsImageUrl, newsTitle, newsTotalComments, newsTotalShares, newsTotalViews, newsURL);
                newsArrayList.add(newsClass);
            }

            if(newsArrayList.size()>0)
            {
                SessionClass.URLlinkCat = newsArrayList.get(pos).getURL();
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }




        } catch (Exception e) {
            e.toString();
        }

    }


    @Override
    public int getItemCount() {
        return subCatClassArray_.size();
    }

}