package com.codepath.nytquest.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by rpraveen on 10/22/16.
 */
public class TopStoriesClient {

  protected static String API_KEY = "009a6551197a4fadba0adebabbc255f0";

  protected AsyncHttpClient mClient;
  protected RequestParams mQueryParams;

  private String mSection;

  public TopStoriesClient() {
    mClient = new AsyncHttpClient();
    mQueryParams = new RequestParams();
    mQueryParams.put("api-key", API_KEY);
  }

  protected String getBaseURL() {
    return "https://api.nytimes.com/svc/topstories/v2/";
  }

  public void setSection(String section) {
    mSection = section;
    mQueryParams.put("section", section);
  }

  public void getTopStories(JsonHttpResponseHandler handler) {
    String queryURL = getBaseURL() + mSection + ".json";
    try {
      mClient.get(queryURL, mQueryParams, handler);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
