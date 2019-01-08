package com.mawaqaa.alrai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.activity.NewsDetailsActivity;
import com.mawaqaa.alrai.other.NewsClass;
import com.mawaqaa.alrai.other.SessionClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 5/24/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {


    ArrayList<NewsClass> newsClassArrayList;
    private Context context;

    public NewsAdapter(Context context, ArrayList<NewsClass> newsClassArrayList) {

        this.context = context;
        this.newsClassArrayList = newsClassArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_news_grid_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.news_text.setText(this.newsClassArrayList.get(position).getTitle());
        Picasso.with(this.context).load(this.newsClassArrayList.get(position).getImageUrl()).into(holder.news_image);

        holder.news_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionClass.URLlinkCat = newsClassArrayList.get(position).getURL();
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.news_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionClass.URLlinkCat = newsClassArrayList.get(position).getURL();
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return newsClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView news_text;
        ImageView news_image;

        public MyViewHolder(View view) {
            super(view);
            news_text = (TextView) view.findViewById(R.id.news_text);
            news_image = (ImageView) view.findViewById(R.id.news_image);

        }
    }


}