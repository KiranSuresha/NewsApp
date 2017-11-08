package com.android.kiran.guardiannews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {

  private static final String LOG_TAG = NewsAdapter.class.getName();

  private Context mContext;
  private List<News> mNewsList;

  public NewsAdapter(Context context, List<News> newsItems) {
    super(context, 0, newsItems);
    mContext = context;
    mNewsList = newsItems;
  }

  public static class NewsViewHolder {

    TextView textViewTitle;
    TextView textViewSection;
    TextView textViewDate;
    TextView textViewAuthor;

    public NewsViewHolder(View itemView) {
      textViewTitle = itemView.findViewById(R.id.text_news_title);
      textViewSection = itemView.findViewById(R.id.text_news_section);
      textViewDate = itemView.findViewById(R.id.text_news_date);
      textViewAuthor = itemView.findViewById(R.id.text_news_author);
    }
  }

  @NonNull @Override public View getView(int position, View convertView, ViewGroup parent) {

    String title = "";
    String section = "";
    String newsDate = "";
    String author = "";
    NewsViewHolder holder;

    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_card, parent, false);
      holder = new NewsViewHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (NewsViewHolder) convertView.getTag();
    }

    if (position < getCount()) {

      News currentNews = getItem(position);
      if ((currentNews.getNewsTitle() != null) && (currentNews.getNewsTitle().length() > 0)) {
        title = currentNews.getNewsTitle();
      }
      if ((currentNews.getNewsSection() != null) && (currentNews.getNewsSection().length() > 0)) {
        section = currentNews.getNewsSection();
      }
      if ((currentNews.getNewsAuthor() != null) && (currentNews.getNewsAuthor().length() > 0)) {
        author = currentNews.getNewsAuthor();
      }
      if ((currentNews.getNewsPublishedDate() != null) && (currentNews.getNewsPublishedDate()
          .length() > 0)) {
        String date = currentNews.getNewsPublishedDate();
        newsDate = formatDate(date);
      }
    }
    holder.textViewTitle.setText(title);
    holder.textViewSection.setText(section);
    holder.textViewAuthor.setText(author);
    holder.textViewDate.setText(newsDate);

    return convertView;
  }

  private String formatDate(String date) {

    String dateFormatted = "";
    String dateNew = date.substring(0, 10);

    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
    try {
      Date dt = inputFormat.parse(dateNew);
      dateFormatted = newFormat.format(dt);
    } catch (ParseException pe) {
      Log.e(LOG_TAG, mContext.getString(R.string.exception_date_format), pe);
    }

    return dateFormatted;
  }
}
