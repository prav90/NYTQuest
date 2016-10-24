package com.codepath.nytquest.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpraveen on 10/23/16.
 */
public class SearchResult extends Article {

  @Override
  public String getImageURL() {
    if (articleImage != null) {
      return "http://www.nytimes.com/" + articleImage;
    }
    return articleImage;
  }

  public SearchResult(JSONObject jsonArticle) throws JSONException {
    articleName = jsonArticle.getJSONObject("headline").getString("main");
    JSONArray multimedia = jsonArticle.getJSONArray("multimedia");
    if (multimedia.length() > 0) {
      articleImage = multimedia.getJSONObject(0).getString("url");
    }
    articleURL = jsonArticle.getString("web_url");
  }

  public static List<Article> fromJSONArray(JSONArray jsonArticles) throws JSONException{
    ArrayList<Article> searchHits = new ArrayList<>();
    for (int i = 0; i < jsonArticles.length(); i++) {
      searchHits.add(new SearchResult(jsonArticles.getJSONObject(i)));
    }
    return searchHits;
  }
}
