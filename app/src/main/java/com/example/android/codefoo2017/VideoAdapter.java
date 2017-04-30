package com.example.android.codefoo2017;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Video Adapter for displaying Video data in RecyclerView
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    private final Context mContext;
    private Cursor mCursor;
    private int mParentSize;

    // Keep track of the number of VideoAdapters
    public static int sInstances;
    private int mInstance;

    final private VideoAdapterOnClickHandler mClickHandler;

    public interface VideoAdapterOnClickHandler {
        void onClick(String url);
    }

    public VideoAdapter(Context context, VideoAdapterOnClickHandler clickHandler,
                        int parentSize) {
        mContext = context;
        mClickHandler = clickHandler;
        mParentSize = parentSize;
        sInstances++;
        // Set the current instance number - but keep the numbers within a range
        mInstance = sInstances % (mParentSize / 2) + 1;
    }

    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_video, parent, false);

        view.setFocusable(true);

        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapterViewHolder holder, int position) {

        // So the videos aren't the same for each adapter,
        // the position of the cursor is changed - the cursor
        // has more than enough data for one adapter.
        switch (mInstance) {
            case 1:
                position += mParentSize;
                break;
        }

        mCursor.moveToPosition(position);

        String title = mCursor.getString(MainActivity.INDEX_VIDEO_TITLE);
        String thumbnailUrlAsString = mCursor.getString(MainActivity.INDEX_VIDEO_THUMBNAIL);
        final String urlAsString = mCursor.getString(MainActivity.INDEX_VIDEO_URL);

        // Get and download the thumbnail
        Glide.with(mContext)
                .load(thumbnailUrlAsString)
                .crossFade()
                .into(holder.thumbnailImageView);

        holder.titleTextView.setText(title);

        // Set the onclick for the entire view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onClick(urlAsString);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        // Displays appropriate size
        // NOTE: this isn't accurate for larger pull sizes at the moment
        return mCursor.getCount() - mParentSize;
    }

    /**
     * Set the cursor to the new cursor
     * @param newCursor cursor with appropriate video data
     */
    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class VideoAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView titleTextView;
        final ImageView thumbnailImageView;

        public VideoAdapterViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.videoTitleTextView);
            thumbnailImageView = (ImageView) itemView.findViewById(R.id.videoThumbnailImageView);
        }
    }
}
