package com.mawaqaa.alrai.other;

/**
 * Created by HP on 8/27/2017.
 */

public class SubCategoryDataClass {

    String SubCategoryID;
    String SubCategoryName;

    public SubCategoryDataClass(String subCategoryID, String subCategoryName) {
        SubCategoryID = subCategoryID;
        SubCategoryName = subCategoryName;
    }

    public void setSubCategoryID(String subCategoryID) {
        SubCategoryID = subCategoryID;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getSubCategoryID() {
        return SubCategoryID;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }
}
