package com.codepath.nytquest.net;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.ArrayList;

/**
 * Created by rpraveen on 10/23/16.
 */
public class ArticleSearchClient extends TopStoriesClient {

  private String mQueryString;

  protected String getBaseURL() {
    return "https://api.nytimes.com/svc/search/v2/articlesearch.json";
  }

  public void setQueryString(String query) {
    mQueryString = query;
    mQueryParams.put("q", query);
  }

  public void setPage(int page) {
    mQueryParams.put("page", page);
  }

  public void setSortBy(boolean sortByNewest) {
    mQueryParams.put("sort", sortByNewest? "newest" : "oldest");
  }

  public void setNewsDesk(ArrayList<String> newsDeskValues) {
    String paramValue = "news_desk:(";
    for(String news_desk: newsDeskValues) {
      paramValue += "\"" + news_desk + "\"";
    }
    paramValue += ")";
    mQueryParams.put("fq", paramValue);
  }

  public void setBeginDate(String beginDate) {
    mQueryParams.put("begin_date", beginDate.replaceAll("-", ""));
  }

  public void setEndDate(String endDate) {
    mQueryParams.put("end_date", endDate.replaceAll("-", ""));
  }

  public void getResults(JsonHttpResponseHandler handler) {
    try {
      mClient.get(getBaseURL(), mQueryParams, handler);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
