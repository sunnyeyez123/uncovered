package com.jasminelawrence.uncovered;

/**
 * Created by Jasmine on 1/12/18.
 */

public class Article {

    private String mTitle, mSection, mAuthor, mPublished, mUrl;


    public Article(String title, String section, String published, String author, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
        mAuthor = author;
        mPublished = published;

    }

    public Article(String title, String section, String published, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
        mPublished = published;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublished() {
        return mPublished;
    }

    public String getUrl() {
        return mUrl;
    }
}
