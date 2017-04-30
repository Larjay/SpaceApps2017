package com.example.android.codefoo2017;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Article adapter for displaying Article data in RecyclerView
 */
class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleAdapterViewHolder>
        implements VideoAdapter.VideoAdapterOnClickHandler {

    private final Context mContext;
    private Cursor mCursor;
    private Cursor mVideoCursor;

    final private ArticleAdapterOnClickHandler mClickHandler;

    public interface ArticleAdapterOnClickHandler {
        void onClick(String url);
    }

    public ArticleAdapter(@NonNull Context context, ArticleAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public ArticleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_article, parent, false);

        view.setFocusable(true);

        return new ArticleAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        // Get information from cursor for the appropriate position in the cursor
        String timePassed = mCursor.getString(MainActivity.INDEX_ARTICLE_TIME_PASSED);
        String title = mCursor.getString(MainActivity.INDEX_ARTICLE_TITLE);
        String thumbnailUrlAsString = mCursor.getString(MainActivity.INDEX_ARTICLE_THUMBNAIL);
        final String urlAsString = mCursor.getString(MainActivity.INDEX_ARTICLE_URL);

        // Get and download the thumbnail
        Glide.with(mContext)
                .load(thumbnailUrlAsString)
                .crossFade()
                .into(holder.thumbnailImageView);

        // Bind the data to the view widgets
        holder.timeSinceTextView.setText(timePassed);
        holder.titleTextView.setText(title);
        holder.thumbnailImageView.setContentDescription(title);

        // Set click listener for the entire view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onClick(urlAsString);
            }
        });

        // For every "third" article, we show a video recycler view
        if (position % 3 == 0) {

            holder.videoTitleTextView.setVisibility(View.VISIBLE);

            // Since each recyclerview needs it's own layout manager,
            // this is for the VideoRecyclerView
            LinearLayoutManager horizontalLayoutManager =
                    new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            RecyclerView videoRecyclerView = holder.videosRecyclerView;

            videoRecyclerView.setLayoutManager(horizontalLayoutManager);
            videoRecyclerView.setVisibility(View.VISIBLE);

            // Pass in the pull size as the third argument here
            // so the VideoAdapter can split up the data in its
            // cursor more appropriately
            VideoAdapter videoAdapter = new VideoAdapter(mContext, this, MainActivity.PULL_SIZE);
            // Sets the appropriate cursor
            videoAdapter.swapCursor(mVideoCursor);

            videoRecyclerView.setAdapter(videoAdapter);

        } else {
            // Keep these hidden for the other Articles
            holder.videoTitleTextView.setVisibility(View.GONE);
            holder.videosRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    class ArticleAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView titleTextView;
        final TextView timeSinceTextView;
        final ImageView thumbnailImageView;
        final TextView videoTitleTextView;
        final RecyclerView videosRecyclerView;

        public ArticleAdapterViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            timeSinceTextView = (TextView) itemView.findViewById(R.id.timeSinceTextView);
            thumbnailImageView = (ImageView) itemView.findViewById(R.id.thumbnailImageView);
            videoTitleTextView = (TextView) itemView.findViewById(R.id.videoTitleTextView);
            videosRecyclerView = (RecyclerView) itemView.findViewById(R.id.videosRV);
        }
    }

    @Override
    public void onClick(String url) {

        Intent loadWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(loadWebIntent);
    }

    /**
     * Set the article cursor
     * @param newCursor article data as a cursor
     */
    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * Set the video cursor
     * @param cursor video data as a cursor
     */
    void setVideoCursor(Cursor cursor) {
        mVideoCursor = cursor;
        notifyDataSetChanged();
    }

}
