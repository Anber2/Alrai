package com.mawaqaa.alrai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.activity.NewsPaperActivity;
import com.mawaqaa.alrai.other.newsPaperClass;

import java.util.ArrayList;

/**
 * Created by HP on 8/29/2017.
 */

public class NewsPaperAdapter extends RecyclerView.Adapter<NewsPaperAdapter.MyViewHolder> {


    Context context;
    String subCatID = "";
    ArrayList<newsPaperClass> newsPaperClassArrayList;
    newsPaperClass newsPaperClass;

    public NewsPaperAdapter(Context context, ArrayList<newsPaperClass> newsPaperClassArrayList) {

        this.context = context;
        this.newsPaperClassArrayList = newsPaperClassArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_cat_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.TextView_SubCat.setText(this.newsPaperClassArrayList.get(position).getNewsText());


        holder.TextView_SubCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position;


                NewsPaperActivity.webView.loadUrl(newsPaperClassArrayList.get(pos).getNewsURL());
               /* Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
*/

            }
        });

    }

    @Override
    public int getItemCount() {
        return newsPaperClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TextView_SubCat;

        public MyViewHolder(View view) {
            super(view);
            TextView_SubCat = (TextView) view.findViewById(R.id.TextView_SubCat);
        }
    }


}