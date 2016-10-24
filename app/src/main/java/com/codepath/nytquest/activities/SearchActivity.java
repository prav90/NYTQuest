package com.codepath.nytquest.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.codepath.nytquest.R;
import com.codepath.nytquest.adapters.NewsArticlesAdapter;
import com.codepath.nytquest.decorators.SpacesItemDecoration;
import com.codepath.nytquest.fragments.FilterFragment;
import com.codepath.nytquest.interfaces.EndlessRecyclerViewScrollListener;
import com.codepath.nytquest.models.Article;
import com.codepath.nytquest.models.Filter;
import com.codepath.nytquest.models.SearchResult;
import com.codepath.nytquest.net.ArticleSearchClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 10/23/16.
 */
public class SearchActivity
  extends AppCompatActivity
  implements FilterFragment.OnSearchFilterListener {

  @BindView(R.id.layoutPopular) LinearLayout popularStrip;
  @BindView(R.id.rvArticles) RecyclerView rvArticles;
  @BindView(R.id.toolbar) Toolbar toolbar;

  private static final int MAX_PAGES = 10;

  private List<Article> mSearchResults;
  private NewsArticlesAdapter mSearchResultsAdapter;
  private StaggeredGridLayoutManager mTopStoriesLayoutManager;

  private Filter mFilter;

  @Override
  protected void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    popularStrip.setVisibility(View.INVISIBLE);
    mFilter = new Filter();
    mFilter.searchQuery = getIntent().getStringExtra("search_query");
    mSearchResults = new ArrayList<>();
    mSearchResultsAdapter = new NewsArticlesAdapter(this, mSearchResults);
    rvArticles.setAdapter(mSearchResultsAdapter);
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
        if (mFilter.searchQuery.trim().length() > 0 && page <= MAX_PAGES) {
          mFilter.page += 1;
          getSearchResults();
        }
      }
    });
  }

  private void clearSearchResults() {
    mSearchResults.clear();
    mSearchResultsAdapter.notifyDataSetChanged();
  }

  private void getSearchResults() {
    ArticleSearchClient searchClient = new ArticleSearchClient();
    searchClient.setQueryString(mFilter.searchQuery);
    searchClient.setPage(mFilter.page);
    searchClient.setSortBy(mFilter.sortByNewest);
    if (mFilter.newsDesk.size() > 0) {
      searchClient.setNewsDesk(mFilter.newsDesk);
    }
    if (mFilter.beginDate != null && mFilter.beginDate.length() > 0) {
      searchClient.setBeginDate(mFilter.beginDate);
    }
    if (mFilter.endDate != null &&  mFilter.endDate.length() > 0) {
      searchClient.setEndDate(mFilter.endDate);
    }
    searchClient.getResults(new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          if (response != null) {
            JSONObject results = response.getJSONObject("response");
            if (results != null) {
              JSONArray searchHits = results.getJSONArray("docs");
              int currentSize = mSearchResultsAdapter.getItemCount();
              mSearchResults.addAll(SearchResult.fromJSONArray(searchHits));
              mSearchResultsAdapter.notifyItemRangeInserted(currentSize, mSearchResults.size() - 1);
            }
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

  // Menu icons are inflated just as they were with actionbar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_search_activity, menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    searchView.setIconified(false);

    // Customize searchview text and hint colors
    int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
    EditText et = (EditText) searchView.findViewById(searchEditId);
    et.setText(mFilter.searchQuery);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        // perform query here
        if (query.trim().length() == 0) {
          return true;
        }
        clearSearchResults();
        mFilter.searchQuery = query;
        mFilter.page = 0;
        // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
        // see https://code.google.com/p/android/issues/detail?id=24599
        getSearchResults();
        searchView.clearFocus();

        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    // Expand the search view and request focus
    searchItem.expandActionView();
    getSearchResults();

    return true;
  }

  public void onFilterClick(MenuItem item) {
    FragmentManager fm = getSupportFragmentManager();
    FilterFragment filterDialog = FilterFragment.newInstance(mFilter);
    filterDialog.show(fm, "filter");
  }

  public void onFilter() {
    clearSearchResults();
    mFilter.page = 0;
    getSearchResults();
  }
}
