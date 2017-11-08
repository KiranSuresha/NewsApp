package com.android.kiran.guardiannews;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryUtils {

  private static final String LOG_TAG = QueryUtils.class.getName();
  private static final String API_KEY_RESPONSE = "response";
  private static final String API_KEY_RESULTS = "results";
  private static final String API_KEY_SECTION = "sectionName";
  private static final String API_KEY_PUBLISHED_DATE = "webPublicationDate";
  private static final String API_KEY_TITLE = "webTitle";
  private static final String API_KEY_WEBURL = "webUrl";
  private static final String API_KEY_TAGS = "tags";
  private static Context mContext;

  private QueryUtils() {
  }

  public static List<News> fetchNewsItems(String requestUrl, Context context) {

    mContext = context;

    URL url = createUrl(requestUrl);

    String jsonResponse = null;
    try {
      jsonResponse = makeHttpRequest(url);
    } catch (IOException e) {
      Log.e(LOG_TAG, mContext.getString(R.string.exception_http_request), e);
    }

    List<News> newsItems = extractFeatureFromJson(jsonResponse);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return newsItems;
  }

  private static URL createUrl(String stringUrl) {
    URL url = null;
    try {
      url = new URL(prepareUrl(stringUrl));
    } catch (MalformedURLException e) {
      Log.e(LOG_TAG, mContext.getString(R.string.exception_url_invalid), e);
    }
    return url;
  }

  private static String makeHttpRequest(URL url) throws IOException {
    String jsonResponse = "";

    if (url == null) {
      return jsonResponse;
    }

    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setReadTimeout(10000 /* milliseconds */);
      urlConnection.setConnectTimeout(15000 /* milliseconds */);
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      if (urlConnection.getResponseCode() == 200) {
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
      } else {
        Log.e(LOG_TAG,
            mContext.getString(R.string.exception_resp_code) + urlConnection.getResponseCode());
      }
    } catch (IOException e) {
      Log.e(LOG_TAG, mContext.getString(R.string.exception_json_results), e);
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (inputStream != null) {
        inputStream.close();
      }
    }
    return jsonResponse;
  }

  private static String readFromStream(InputStream inputStream) throws IOException {
    StringBuilder output = new StringBuilder();
    if (inputStream != null) {
      InputStreamReader inputStreamReader =
          new InputStreamReader(inputStream, Charset.forName("UTF-8"));
      BufferedReader reader = new BufferedReader(inputStreamReader);
      String line = reader.readLine();
      while (line != null) {
        output.append(line);
        line = reader.readLine();
      }
    }
    return output.toString();
  }

  private static List<News> extractFeatureFromJson(String newsJSON) {

    if (TextUtils.isEmpty(newsJSON)) {
      return null;
    }

    List<News> newsItems = new ArrayList<>();

    try {

      JSONObject baseJsonResponse;
      JSONObject jsonResults;
      JSONArray newsArray;
      JSONObject currentNewsItem;
      String newsTitle = "";
      String newsSection = "";
      String newsDate = "";
      String newsUrl = "";
      JSONArray tagsArray;
      JSONObject newsTag;
      String newsAuthor = "";

      baseJsonResponse = new JSONObject(newsJSON);
      jsonResults = baseJsonResponse.getJSONObject(API_KEY_RESPONSE);

      if (jsonResults.has(API_KEY_RESULTS)) {
        newsArray = jsonResults.getJSONArray(API_KEY_RESULTS);

        for (int i = 0; i < newsArray.length(); i++) {
          currentNewsItem = newsArray.getJSONObject(i);

          if (currentNewsItem.has(API_KEY_TITLE)) {
            newsTitle = currentNewsItem.getString(API_KEY_TITLE);
          }

          if (currentNewsItem.has(API_KEY_SECTION)) {
            newsSection = currentNewsItem.getString(API_KEY_SECTION);
          }

          if (currentNewsItem.has(API_KEY_PUBLISHED_DATE)) {
            newsDate = currentNewsItem.getString(API_KEY_PUBLISHED_DATE);
          }

          if (currentNewsItem.has(API_KEY_WEBURL)) {
            newsUrl = currentNewsItem.getString(API_KEY_WEBURL);
          }

          if (currentNewsItem.has(API_KEY_TAGS)) {
            tagsArray = currentNewsItem.getJSONArray(API_KEY_TAGS);
            // newsTags = tagsArray.getJSONObject(0);

            if (tagsArray.length() > 0) {
              for (int j = 0; j < 1; j++) {
                newsTag = tagsArray.getJSONObject(j);
                if (newsTag.has(API_KEY_TITLE)) {
                  newsAuthor = newsTag.getString(API_KEY_TITLE);
                }
              }
            }
          }

          News newsItem = new News(newsTitle, newsSection, newsDate, newsAuthor, newsUrl);

          newsItems.add(newsItem);
        }
      }
    } catch (JSONException e) {
      Log.e(LOG_TAG, mContext.getString(R.string.exception_json_results), e);
    }
    return newsItems;
  }

  private static String prepareUrl(String url) {
    Uri baseUri = Uri.parse(url);
    Uri.Builder uriBuilder = baseUri.buildUpon();

    uriBuilder.appendQueryParameter(mContext.getString(R.string.tags_label),
        mContext.getString(R.string.tags_value));
    uriBuilder.appendQueryParameter(mContext.getString(R.string.reference_label),
        mContext.getString(R.string.reference_value));
    uriBuilder.appendQueryParameter(mContext.getString(R.string.api_label),
        mContext.getString(R.string.api_value));

    Log.i(LOG_TAG, "Query URL => " + uriBuilder.toString());

    return uriBuilder.toString();
  }
}