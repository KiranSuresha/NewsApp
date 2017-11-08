package com.android.kiran.guardiannews;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

  private static final String LOG_TAG = NewsLoader.class.getSimpleName();

  private String mUrl;

  public NewsLoader(Context context, String url) {
    super(context);
    mUrl = url;
  }

  @Override protected void onStartLoading() {
    super.onStartLoading();
    forceLoad();
  }

  @Override public List<News> loadInBackground() {
    if (mUrl == null) {
      return null;
    }

    // Perform network request, parse the response, and extract a list of news items
    return QueryUtils.fetchNewsItems(mUrl, getContext());
  }
}
