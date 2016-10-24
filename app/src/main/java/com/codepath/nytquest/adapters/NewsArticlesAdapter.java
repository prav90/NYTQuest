package com.codepath.nytquest.adapters;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytquest.R;
import com.codepath.nytquest.models.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rpraveen on 10/21/16.
 */
public class NewsArticlesAdapter extends RecyclerView.Adapter<NewsArticlesAdapter.ViewHolder> {

  private List<Article> mArticles;
  private Context mContext;;
  private Bitmap mShareBitMap;

  private static final int ARTICLE_WITH_IMAGE = 0;
  private static final int TEXT_ONLY_ARTICLE = 1;

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.tvArticleName) TextView tvArticleName;
    @Nullable @BindView(R.id.tvArticleImage) ImageView ivArticleImage;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }
    // Handles the row being being clicked
    @Override
    public void onClick(View view) {
      int position = getAdapterPosition(); // gets item position
      // Check if an item was deleted, but the user clicked it before the UI removed it
      if (position != RecyclerView.NO_POSITION) {
        Article result = mArticles.get(position);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, result.getArticleURL());
        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setActionButton(mShareBitMap, "Share Link", pendingIntent, true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl((Activity) mContext, Uri.parse(result.getArticleURL()));
      }
    }
  }

  public NewsArticlesAdapter(Context context, List<Article> articles) {
    mArticles = articles;
    mContext = context;
    mShareBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_share);
  }

  @Override
  public int getItemViewType(int position) {
    if (mArticles.get(position).getImageURL() == null) {
      return TEXT_ONLY_ARTICLE;
    }
    return ARTICLE_WITH_IMAGE;
  }


  @Override
  public NewsArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View articleView;
    if (viewType == ARTICLE_WITH_IMAGE) {
      articleView = inflater.inflate(R.layout.item_article, parent, false);
    } else {
      articleView = inflater.inflate(R.layout.item_text_article, parent, false);
    }


    return new ViewHolder(articleView);
  }

  @Override
  public void onBindViewHolder(NewsArticlesAdapter.ViewHolder viewHolder, int position) {
    Article article = mArticles.get(position);
    viewHolder.tvArticleName.setText(article.toString());
    if (viewHolder.getItemViewType() == ARTICLE_WITH_IMAGE) {
      Glide.with(mContext)
        .load(article.getImageURL())
        .into(viewHolder.ivArticleImage);
    }
  }

  @Override
  public int getItemCount() {
    return mArticles.size();
  }
}
