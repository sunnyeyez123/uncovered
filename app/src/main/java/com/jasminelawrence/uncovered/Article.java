package com.jasminelawrence.uncovered;

/**
 * Created by Jasmine on 1/12/18.
 */

public class Article {

    private String mTitle, mSection, mAuthor, mPublished, mUrl;


    public Article(String title, String section, String url){
        title=mTitle;
        section= mSection;
        url=mUrl;


    }

    public Article(String title, String section,String author,String url){
        title=mTitle;
        section= mSection;
        url=mUrl;
        author = mAuthor;

    }

    public Article(String title, String section, String author, String published,String url){
        title=mTitle;
        section= mSection;
        url=mUrl;
        published=mPublished;
        author=mAuthor;
    }
}
