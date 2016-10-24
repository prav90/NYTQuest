package com.codepath.nytquest.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpraveen on 10/21/16.
 */
public class Article {
  protected String articleName;
  protected String articleImage;
  protected String articleURL;

  public String toString() {
    return articleName;
  }

  public String getImageURL() {
    return articleImage;
  }

  public String getArticleName() {
    return articleName;
  }

  public String getArticleURL() {
    return articleURL;
  }

  protected Article(){
  }

  public Article(JSONObject jsonArticle) throws JSONException {
    articleName = jsonArticle.getString("title");
    JSONArray multimedia = jsonArticle.getJSONArray("multimedia");
    if (multimedia.length() > 1) {
      articleImage = multimedia.getJSONObject(1).getString("url");
    }
    articleURL = jsonArticle.getString("url");
  }

  public static List<Article> fromJSONArray(JSONArray jsonArticles) throws JSONException{
    ArrayList<Article> articles = new ArrayList<>();
    for (int i = 0; i < jsonArticles.length(); i++) {
      articles.add(new Article(jsonArticles.getJSONObject(i)));
    }
    return articles;
  }
}

