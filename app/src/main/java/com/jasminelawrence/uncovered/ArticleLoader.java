package com.jasminelawrence.uncovered;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Jasmine on 1/12/18.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<Article> loadInBackground() {

        // Don't perform the request if there are no URLs, or the first URL is null.
        if ( mUrl == null) {
            return null;
        }

        List<Article> result = QueryUtils.fetchArticleData(mUrl);
        return result;        }
}
