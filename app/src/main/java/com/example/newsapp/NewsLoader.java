package com.example.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Mayank on 11/10/18 at 9:31 PM
 **/
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading is called");
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground is called");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.

        return NewsUtils.fetchNewsData(mUrl);
    }
}
