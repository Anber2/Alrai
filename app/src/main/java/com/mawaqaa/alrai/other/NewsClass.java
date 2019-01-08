package com.mawaqaa.alrai.other;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 5/24/2017.
 */

public class NewsClass implements Parcelable {
    public static final Creator<NewsClass> CREATOR = new Creator<NewsClass>() {
        @Override
        public NewsClass createFromParcel(Parcel in) {
            return new NewsClass(in);
        }

        @Override
        public NewsClass[] newArray(int size) {
            return new NewsClass[size];
        }
    };
    private String id;
    private String imageUrl;
    private String title;
    private String totalComments;
    private String totalShares;
    private String totalViews;
    private String URL;

    public NewsClass() {

    }

    public NewsClass(String id, String imageUrl, String title, String totalComments,
                     String totalShares, String totalViews, String URL) {
        this.setId(id);
        this.setImageUrl(imageUrl);
        this.setTitle(title);
        this.setTotalComments(totalComments);
        this.setTotalShares(totalShares);
        this.setTotalViews(totalViews);
        this.setURL(URL);
    }

    protected NewsClass(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        title = in.readString();
        totalComments = in.readString();
        totalShares = in.readString();
        totalViews = in.readString();
        URL = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

    public String getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(String totalShares) {
        this.totalShares = totalShares;
    }

    public String getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(String totalViews) {
        this.totalViews = totalViews;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(totalComments);
        dest.writeString(totalShares);
        dest.writeString(totalViews);
        dest.writeString(URL);
    }
}
