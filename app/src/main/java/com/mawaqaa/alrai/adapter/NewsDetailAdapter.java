package com.mawaqaa.alrai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.other.NewsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 8/29/2017.
 */

public class NewsDetailAdapter extends BaseAdapter {

    ArrayList<NewsClass> newsArrayList;
    private Context context;

    public NewsDetailAdapter(Context context, ArrayList<NewsClass> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;
    }

    @Override
    public int getCount() {
        return newsArrayList.size();
    }

    @Override
    public NewsClass getItem(int position) {
        NewsClass newsClass = newsArrayList.get(position);
        return newsClass;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = inflater.inflate(R.layout.top_news_grid_item, null);

        }

        TextView newsText = (TextView) view.findViewById(R.id.news_text);
        ImageView newsImage = (ImageView) view.findViewById(R.id.news_image);

        NewsClass newsClass = newsArrayList.get(position);
        newsText.setText(newsClass.getTitle());
        Picasso.with(this.context).load(newsClass.getImageUrl()).into(newsImage);

        return view;
    }
}