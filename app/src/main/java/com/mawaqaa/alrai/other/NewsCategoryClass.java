package com.mawaqaa.alrai.other;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 5/22/2017.
 */

public class NewsCategoryClass implements Parcelable {

    private String id;
    private String name;
    private String parentId;

    public static final Creator<NewsCategoryClass> CREATOR = new Creator<NewsCategoryClass>() {
        @Override
        public NewsCategoryClass createFromParcel(Parcel in) {
            return new NewsCategoryClass(in);
        }

        @Override
        public NewsCategoryClass[] newArray(int size) {
            return new NewsCategoryClass[size];
        }
    };

    public NewsCategoryClass(String id, String name, String parentId) {
        this.setId(id);
        this.setName(name);
        this.setParentId(parentId);
    }

    protected NewsCategoryClass(Parcel in) {
        id = in.readString();
        name = in.readString();
        parentId = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(parentId);
    }
}
