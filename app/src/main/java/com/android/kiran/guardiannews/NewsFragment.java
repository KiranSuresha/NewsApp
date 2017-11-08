package com.android.kiran.guardiannews;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NewsFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {

  public static final String LOG_TAG = NewsFragment.class.getName();

  /** Guardian API Base URL */
  private static final String BASE_URL = "http://content.guardianapis.com/search?q=";
  private static final String SEARCH_URL = "https://content.guardianapis.com/search?section=";

  public static List<News> mListNews;
  private View mView;
  private NewsAdapter mAdapter;
  private TextView mEmptyStateTextView;
  private ListView mNewsListView;
  private SwipeRefreshLayout mSwipeRefreshLayout;
  private int mLoaderId;

  public static NewsFragment newInstance(String newsSection, int menuPosition) {
    NewsFragment newsFragment = new NewsFragment();
    Bundle args = new Bundle();
    args.putString("section", newsSection);
    args.putInt("position", menuPosition);
    newsFragment.setArguments(args);
    return newsFragment;
  }

  public String getSection() {
    return getArguments().getString("section", "");
  }

  public int getMenuPosition() {
    return getArguments().getInt("position", 0);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    mView = inflater.inflate(R.layout.news_list, container, false);

    mSwipeRefreshLayout = mView.findViewById(R.id.swipe_refresh_layout);
    mSwipeRefreshLayout.setOnRefreshListener(this);
    mSwipeRefreshLayout.setColorSchemeColors(
        ContextCompat.getColor(getContext(), R.color.colorAccent));

    mNewsListView = mView.findViewById(R.id.list_news);

    // Set empty view
    mEmptyStateTextView = mView.findViewById(R.id.text_empty_list);
    mNewsListView.setEmptyView(mEmptyStateTextView);

    mListNews = new ArrayList<News>();
    mAdapter = new NewsAdapter(getContext(), mListNews);
    mNewsListView.setAdapter(mAdapter);

    if (isConnected()) {
      LoaderManager loaderManager = getActivity().getSupportLoaderManager();
      mLoaderId = getMenuPosition();
      loaderManager.initLoader(mLoaderId, null, this);
    } else {
      View progressIndicator = mView.findViewById(R.id.progress_indicator);
      mEmptyStateTextView.setText(R.string.error_no_connection);
      progressIndicator.setVisibility(View.GONE);
    }

    mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        News currentNews = mAdapter.getItem(position);
        Uri newsUri = Uri.parse(currentNews.getNewsUrl());
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
        startActivity(websiteIntent);
      }
    });
    return mView;
  }

  @Override public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

    String section = getSection();
    String url = "";

    if (section.equals(getString(R.string.menu_home).toLowerCase())) {
      url = BASE_URL;
    } else {
      url = SEARCH_URL + section;
    }

    Uri baseUri = Uri.parse(url);
    Uri.Builder uriBuilder = baseUri.buildUpon();

    return new NewsLoader(getContext(), uriBuilder.toString());
  }

  @Override public void onLoadFinished(Loader<List<News>> loader, List<News> newsItems) {

    mSwipeRefreshLayout.setRefreshing(false);

    View progressIndicator = mView.findViewById(R.id.progress_indicator);
    progressIndicator.setVisibility(View.GONE);

    if (isConnected()) {
      if (newsItems == null || newsItems.size() == 0) {
        mEmptyStateTextView.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setText(getString(R.string.info_no_news));
      } else {
        mEmptyStateTextView.setVisibility(View.GONE);
      }
      mListNews.clear();
      if (newsItems != null && !newsItems.isEmpty()) {
        mListNews.addAll(newsItems);
        mAdapter.notifyDataSetChanged();
      }
    } else {
      mEmptyStateTextView.setText(R.string.error_no_connection);
    }
  }

  @Override public void onLoaderReset(Loader<List<News>> loader) {
    mAdapter.clear();
  }

  @Override public void onRefresh() {
    getActivity().getSupportLoaderManager().restartLoader(mLoaderId, null, this);
  }

  public boolean isConnected() {
    boolean hasNetwork;

    ConnectivityManager connMgr =
        (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);

    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    if (networkInfo != null && networkInfo.isConnected()) {
      hasNetwork = true;
    } else {
      hasNetwork = false;
    }
    return hasNetwork;
  }
}

