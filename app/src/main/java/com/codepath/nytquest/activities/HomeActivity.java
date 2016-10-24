package com.codepath.nytquest.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.nytquest.R;
import com.codepath.nytquest.adapters.NewsArticlesAdapter;
import com.codepath.nytquest.applications.NYTApplication;
import com.codepath.nytquest.broadcastReceivers.ConnectivityReceiver;
import com.codepath.nytquest.decorators.SpacesItemDecoration;
import com.codepath.nytquest.interfaces.EndlessRecyclerViewScrollListener;
import com.codepath.nytquest.models.Article;
import com.codepath.nytquest.net.TopStoriesClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

  @BindView(R.id.rvArticles) RecyclerView rvArticles;
  @BindView(R.id.toolbar) Toolbar toolbar;

  private ArrayList<Article> mTopStories;
  private NewsArticlesAdapter mTopStoriesAdapter;
  private StaggeredGridLayoutManager mTopStoriesLayoutManager;

  private static final String[] TOP_STORY_SECTIONS = new String[] {
    "home", "world", "politics", "national", "sports", "business", "technology", "science",
    "health", "movies", "food", "travel", "automobiles"
  };

  private int mStartPage = 0;

  public void onSearchClick(MenuItem item) {
    Intent i = new Intent(this, SearchActivity.class);
    startActivity(i);
  }

  @Override
  public void onResume() {
    super.onResume();
    NYTApplication.getInstance().setConnectivityListener(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    mTopStories = new ArrayList<>();
    mTopStoriesAdapter = new NewsArticlesAdapter(this, mTopStories);
    rvArticles.setAdapter(mTopStoriesAdapter);
    mTopStoriesLayoutManager = new StaggeredGridLayoutManager(
      2,
      StaggeredGridLayoutManager.VERTICAL
    );
    mTopStoriesLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
    rvArticles.addItemDecoration(new SpacesItemDecoration(16));
    rvArticles.setLayoutManager(mTopStoriesLayoutManager);
    rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(mTopStoriesLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        if (page < TOP_STORY_SECTIONS.length) {
          getTopStories();
        }
      }
    });
    getTopStories();
  }

  // Menu icons are inflated just as they were with actionbar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_home_activity, menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        Intent i = new Intent(HomeActivity.this, SearchActivity.class);
        i.putExtra("search_query", query);
        startActivity(i);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    return true;
  }

  private void getTopStories() {
    TopStoriesClient topStoriesClient = new TopStoriesClient();
    topStoriesClient.setSection(TOP_STORY_SECTIONS[mStartPage++]);
    topStoriesClient.getTopStories(new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          if (response != null) {
            JSONArray results = response.getJSONArray("results");
            int currentSize = mTopStoriesAdapter.getItemCount();
            mTopStories.addAll(Article.fromJSONArray(results));
            mTopStoriesAdapter.notifyItemRangeInserted(currentSize, mTopStories.size() - 1);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
      }
    });
  }

  // Showing the status in Snackbar
  private void showSnack(boolean isConnected) {
    if (!isConnected) {
      String message = "Sorry! Not connected to internet";
      int color = Color.RED;
      Snackbar snackbar = Snackbar
      .make(rvArticles, message, Snackbar.LENGTH_LONG);

      View sbView = snackbar.getView();
      TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
      textView.setTextColor(color);
      snackbar.show();
    }
  }

  @Override
  public void onNetworkConnectionChanged(boolean isConnected) {
    showSnack(isConnected);
  }
}
