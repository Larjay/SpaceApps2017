package com.example.android.codefoo2017;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.codefoo2017.data.ItemPullContract;
import com.example.android.codefoo2017.data.ItemPullDBHelper;
import com.example.android.codefoo2017.pullapi.Article;
import com.example.android.codefoo2017.pullapi.Video;
import com.example.android.codefoo2017.utils.JsonUtils;
import com.example.android.codefoo2017.utils.Utils;

import org.json.JSONException;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ArticleAdapter.ArticleAdapterOnClickHandler {

    public static final int START_INDEX = 0;
    public static final int PULL_SIZE = 5;

    // Article columns to get for the Articles Cursor
    public static final String[] ARTICLE_PROJECTION = {
            ItemPullContract.ArticleEntry.COLUMN_TITLE,
            ItemPullContract.ArticleEntry.COLUMN_TIME_PASSED,
            ItemPullContract.ArticleEntry.COLUMN_THUMBNAIL,
            ItemPullContract.ArticleEntry.COLUMN_URL
    };

    // Indices for the Article columns above
    public static final int INDEX_ARTICLE_TITLE = 0;
    public static final int INDEX_ARTICLE_TIME_PASSED = 1;
    public static final int INDEX_ARTICLE_THUMBNAIL = 2;
    public static final int INDEX_ARTICLE_URL = 3;

    // Video columns to get for the Video Cursor
    public static final String[] VIDEO_PROJECTION = {
            ItemPullContract.VideoEntry.COLUMN_TITLE,
            ItemPullContract.VideoEntry.COLUMN_THUMBNAIL,
            ItemPullContract.VideoEntry.COLUMN_URL
    };

    // Indices for the Video columns above
    public static final int INDEX_VIDEO_TITLE = 0;
    public static final int INDEX_VIDEO_THUMBNAIL = 1;
    public static final int INDEX_VIDEO_URL = 2;

    // Get request loader for articles
    private static final int ID_GET_ARTICLE_LOADER = 321;
    // Get request loader for videos
    private static final int ID_GET_VIDEO_LOADER = 482;
    // Content Provider/db query articles loader
    private static final int ID_LIST_ARTICLE_LOADER = 703;
    // Content Provider/db query videos loader
    private static final int ID_LIST_VIDEO_LOADER = 815;

    private Article[] mArticles;
    private Video[] mVideos;

    private ArticleAdapter mArticleAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean firstLoad, prefFirstLoad;
        int prefPullSize;

        mRecyclerView = (RecyclerView) findViewById(R.id.articlesRV);

        mProgressBar = (ProgressBar) findViewById(R.id.loadingPB);

        LinearLayoutManager verticalLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(verticalLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mArticleAdapter = new ArticleAdapter(this, this);
        mRecyclerView.setAdapter(mArticleAdapter);

        firstLoad = getResources().getBoolean(R.bool.first_load);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the preferences to either get new results or query from local db/content provider
        prefFirstLoad = sharedPreferences.getBoolean(Utils.PREF_INITIAL_LOAD_KEY, firstLoad);
        prefPullSize = sharedPreferences.getInt(Utils.PREF_SIZE, PULL_SIZE);

        if (prefPullSize != PULL_SIZE || prefFirstLoad) {
            // Start the Get request loaders
            getSupportLoaderManager().initLoader(ID_GET_ARTICLE_LOADER, null, this);
            getSupportLoaderManager().initLoader(ID_GET_VIDEO_LOADER, null, this);
        } else {
            // Start the local query loaders
            getSupportLoaderManager().initLoader(ID_LIST_ARTICLE_LOADER, null, this);
            getSupportLoaderManager().initLoader(ID_LIST_VIDEO_LOADER, null, this);
        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {

                switch (id) {

                    case ID_GET_ARTICLE_LOADER:
                    case ID_GET_VIDEO_LOADER:

                        // Hide the recycler view and show the progress bar
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        mProgressBar.setVisibility(View.VISIBLE);

                        forceLoad();
                        break;

                    case ID_LIST_ARTICLE_LOADER:
                    case ID_LIST_VIDEO_LOADER:

                        // Hide the progress bar and show the recycler view
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);

                        forceLoad();
                        break;

                    default:
                        throw new RuntimeException("Unknown loader");
                }

                super.onStartLoading();
            }

            @Override
            public Cursor loadInBackground() {

                // Get the current time to properly display
                // how long it has been since published
                Date currentTime = new Date();

                ContentResolver contentResolver;
                String sortOrder;

                switch (id) {

                    case ID_GET_ARTICLE_LOADER:

                        try {
                            mArticles = JsonUtils.pullArticles(START_INDEX, PULL_SIZE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ContentValues contentValues[] = new ContentValues[PULL_SIZE];

                        // Iterate through each article in the Article array and
                        // populate into ContentValues array
                        // NOTE: could probably populate content values from above
                        // pullArticles method instead of having another data structure
                        // for the Articles themselves
                        for (int index = 0; index < PULL_SIZE; index++) {
                            if (mArticles != null) {
                                Article currentArticle = mArticles[index];

                                String timeUnit = Utils.getTimePassed(currentTime, currentArticle);

                                ContentValues articleValues = new ContentValues();

                                articleValues.put(ItemPullContract.ArticleEntry.COLUMN_DATE,
                                        currentArticle.getPublishDateLong());
                                articleValues.put(ItemPullContract.ArticleEntry.COLUMN_TITLE,
                                        currentArticle.getHeadline());
                                articleValues.put(ItemPullContract.ArticleEntry.COLUMN_THUMBNAIL,
                                        currentArticle.getThumbnail());
                                articleValues.put(ItemPullContract.ArticleEntry.COLUMN_URL,
                                        currentArticle.getUrl());
                                articleValues.put(ItemPullContract.ArticleEntry.COLUMN_TIME_PASSED,
                                        timeUnit);

                                contentValues[index] = articleValues;
                            }
                        }
                        contentResolver = getContentResolver();

                        // Insert into our local content provider/db
                        contentResolver.bulkInsert(
                                ItemPullContract.ArticleEntry.CONTENT_URI,
                                contentValues);

                        return null;

                    case ID_GET_VIDEO_LOADER:

                        // Since there is a video recyclerview for every
                        // 3 articles, there needs to be more videos to show
                        // so pull size is doubled for Videos
                        int videoPullSize = PULL_SIZE * 2;
                        try {
                            mVideos = JsonUtils.pullVideos(START_INDEX, videoPullSize);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ContentValues values[] = new ContentValues[videoPullSize];

                        // Iterate through each video in the Video array and
                        // populate into ContentValues array
                        // NOTE: could probably populate content values from above
                        // pullVideos method instead of having another data structure
                        // for the Videos themselves
                        for (int index = 0; index < videoPullSize; index++) {
                            if (mVideos != null) {
                                Video currentVideo = mVideos[index];

                                ContentValues videoValue = new ContentValues();

                                videoValue.put(ItemPullContract.VideoEntry.COLUMN_DATE,
                                        currentVideo.getPublishDateLong());
                                videoValue.put(ItemPullContract.VideoEntry.COLUMN_TITLE,
                                        currentVideo.getName());
                                videoValue.put(ItemPullContract.VideoEntry.COLUMN_THUMBNAIL,
                                        currentVideo.getThumbnail());
                                videoValue.put(ItemPullContract.VideoEntry.COLUMN_URL,
                                        currentVideo.getUrl());

                                values[index] = videoValue;
                            }
                        }
                        contentResolver = getContentResolver();

                        // Insert into our local content provider/db
                        contentResolver.bulkInsert(
                                ItemPullContract.VideoEntry.CONTENT_URI,
                                values);

                        return null;

                    case ID_LIST_ARTICLE_LOADER:

                        sortOrder = ItemPullContract.ArticleEntry.COLUMN_DATE + " ASC";

                        // Query from our content provider
                        return getContentResolver().query(
                                ItemPullContract.ArticleEntry.CONTENT_URI,
                                ARTICLE_PROJECTION,
                                null,
                                null,
                                sortOrder);

                    case ID_LIST_VIDEO_LOADER:

                        sortOrder = ItemPullContract.ArticleEntry.COLUMN_DATE + " ASC";

                        // Query from our content provider
                        return getContentResolver().query(
                                ItemPullContract.VideoEntry.CONTENT_URI,
                                VIDEO_PROJECTION,
                                null,
                                null,
                                sortOrder);

                    default:
                        throw new RuntimeException("Loader not implemented: " + id);
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch(loader.getId()) {

            case ID_GET_ARTICLE_LOADER:

                setSharedPrefLoaded();

                // Not the most efficient approach to loading the data as we then query
                // the db now instead of populating directly from network with get request
                getSupportLoaderManager().initLoader(ID_LIST_ARTICLE_LOADER, null, this);
                break;

            case ID_GET_VIDEO_LOADER:

                setSharedPrefLoaded();

                // Not the most efficient approach to loading the data as we then query
                // the db now instead of populating directly from network with get request
                getSupportLoaderManager().initLoader(ID_LIST_VIDEO_LOADER, null, this);
                break;

            case ID_LIST_ARTICLE_LOADER:

                // Pass in the cursor for Articles
                mArticleAdapter.swapCursor(data);

                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                break;

            case ID_LIST_VIDEO_LOADER:

                // ArticleAdapter handles the VideoAdapter
                // so we pass in the Video cursor through it
                mArticleAdapter.setVideoCursor(data);
                break;

            default:
                throw new RuntimeException("Unknown loader");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Empty
    }

    @Override
    public void onClick(String url) {

        Intent loadWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(loadWebIntent);
    }

    @SuppressLint("ApplySharedPref")
    private void setSharedPrefLoaded() {
        SharedPreferences sharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);

        // Commit instead of apply the shared preference changes
        sharedPreferences.edit()
                .putBoolean(Utils.PREF_INITIAL_LOAD_KEY, false)
                .putInt(Utils.PREF_SIZE, PULL_SIZE)
                .commit();
    }
}
