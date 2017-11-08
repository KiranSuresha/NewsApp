package com.android.kiran.guardiannews;

/**
 * A {@link News} object that contains details related to a single
 * news item to be displayed in the list
 */

public class News {

  private String mNewsTitle;
  private String mNewsSection;
  private String mNewsPublishedDate;
  private String mNewsAuthor;
  private String mNewsUrl;

  /**
   * Default Constructor to construct a {@link News} object
   */
  public News(String newsTitle, String newsSection, String newsPublishedDate, String newsAuthor,
      String newsUrl) {

    mNewsTitle = newsTitle;
    mNewsSection = newsSection;
    mNewsPublishedDate = newsPublishedDate;
    mNewsAuthor = newsAuthor;
    mNewsUrl = newsUrl;
  }

  public String getNewsTitle() {
    return mNewsTitle;
  }

  public String getNewsSection() {
    return mNewsSection;
  }

  public String getNewsPublishedDate() {
    return mNewsPublishedDate;
  }

  public String getNewsAuthor() {
    return mNewsAuthor;
  }

  public String getNewsUrl() {
    return mNewsUrl;
  }
}
