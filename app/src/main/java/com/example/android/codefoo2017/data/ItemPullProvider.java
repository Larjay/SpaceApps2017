package com.example.android.codefoo2017.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class ItemPullProvider extends ContentProvider {

    private static final int CODE_ARTICLES = 200;
    private static final int CODE_VIDEOS = 300;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private ItemPullDBHelper mOpenHelper;

    /**
     * Builds the Uris for the Provider
     * Has two valid URIs. Articles and Videos
     * @return UriMatcher
     */
    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ItemPullContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ItemPullContract.PATH_ARTICLES, CODE_ARTICLES);
        matcher.addURI(authority, ItemPullContract.PATH_VIDEOS, CODE_VIDEOS);

        return matcher;
    }

    // Helper method to handle a database transaction for inserting multiple rows at once
    private int bulkTransaction(String tableName, @NonNull Uri uri, @NonNull ContentValues[] values,
                                SQLiteDatabase db, int rowsInserted) {

        db.beginTransaction();

        try {
            for (ContentValues value : values) {

                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d("PullProvider", "Rows inserted: " + rowsInserted);
        return rowsInserted;
    }

    @Override
    public boolean onCreate() {

        mOpenHelper = new ItemPullDBHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsInserted = 0;

        switch (sUriMatcher.match(uri)) {

            case CODE_ARTICLES:

                return bulkTransaction(ItemPullContract.ArticleEntry.TABLE_NAME,
                        uri,
                        values,
                        db,
                        rowsInserted);

            case CODE_VIDEOS:

                return bulkTransaction(ItemPullContract.VideoEntry.TABLE_NAME,
                        uri,
                        values,
                        db,
                        rowsInserted);

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_ARTICLES:

                // Query Articles to use with ArticleAdapter
                cursor = mOpenHelper.getReadableDatabase().query(
                        ItemPullContract.ArticleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_VIDEOS:

                // Query Videos to use with VideoAdapter
                cursor = mOpenHelper.getReadableDatabase().query(
                        ItemPullContract.VideoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Not implemented
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Inserting one row at a time is not implemented
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        int numRowsDeleted;

        if (selection == null) {
            selection = "1";
        }

        switch (sUriMatcher.match(uri)) {

            case CODE_ARTICLES:

                // Delete rows in articles
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        ItemPullContract.ArticleEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_VIDEOS:

                // Delete rows in videos
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        ItemPullContract.VideoEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}
