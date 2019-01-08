package com.mawaqaa.alrai.other;

/**
 * Created by HP on 8/29/2017.
 */

public class newsPaperClass
{
    private String newsText;
    private String newsURL;

    public newsPaperClass(String newsText, String newsURL) {
        this.setNewsText(newsText);
        this.setNewsURL(newsURL);
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public String getNewsURL() {
        return newsURL;
    }

    public void setNewsURL(String newsURL) {
        this.newsURL = newsURL;
    }
}
