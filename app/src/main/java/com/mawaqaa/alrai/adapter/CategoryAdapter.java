package com.mawaqaa.alrai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.other.NewsCategoryClass;

import java.util.ArrayList;

/**
 * Created by HP on 5/18/2017.
 */

public class CategoryAdapter extends BaseAdapter {
    ArrayList<NewsCategoryClass> newsCategoryClassArrayList;
    private Context context;

    public CategoryAdapter(Context context, ArrayList<NewsCategoryClass> newsCategoryClassArrayList) {
        this.context = context;
        this.newsCategoryClassArrayList = newsCategoryClassArrayList;
    }


    @Override
    public int getCount() {
        return newsCategoryClassArrayList.size();
    }

    @Override
    public NewsCategoryClass getItem(int position) {

        NewsCategoryClass newsCategoryClass = newsCategoryClassArrayList.get(position);
        return newsCategoryClass;
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
            view = inflater.inflate(R.layout.category_news_grid_item, null);

        }
        TextView categoryTXT = (TextView) view.findViewById(R.id.category_txt);
        NewsCategoryClass newsCategoryClass = newsCategoryClassArrayList.get(position);
        categoryTXT.setText(newsCategoryClass.getName());
        return view;
    }
}
