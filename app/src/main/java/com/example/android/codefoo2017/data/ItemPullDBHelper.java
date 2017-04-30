package com.example.android.codefoo2017.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.codefoo2017.data.ItemPullContract.ArticleEntry;
import com.example.android.codefoo2017.data.ItemPullContract.VideoEntry;

/**
 * Helper for creating the Article and Videos table
 */
public class ItemPullDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "itempulls.db";

    private static final int DATABASE_VERSION = 3;

    public ItemPullDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Publish date is used as the primary key for both tables
        // as there was not another natural primary from the requests

        final String SQL_CREATE_ARTICLE_TABLE =

                "CREATE TABLE " + ItemPullContract.ArticleEntry.TABLE_NAME + " (" +

                ArticleEntry.COLUMN_DATE        + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                ArticleEntry.COLUMN_TITLE       + " TEXT NOT NULL, "                        +
                ArticleEntry.COLUMN_THUMBNAIL   + " TEXT NOT NULL, "                        +
                ArticleEntry.COLUMN_URL         + " TEXT NOT NULL, "                        +
                ArticleEntry.COLUMN_TIME_PASSED + " TEXT NOT NULL "                         +

                ");";

        final String SQL_CREATE_VIDEO_TABLE =

                "CREATE TABLE " + ItemPullContract.VideoEntry.TABLE_NAME + " (" +

                VideoEntry.COLUMN_DATE          + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                VideoEntry.COLUMN_TITLE         + " TEXT NOT NULL, "                        +
                VideoEntry.COLUMN_THUMBNAIL     + " TEXT NOT NULL, "                        +
                VideoEntry.COLUMN_URL           + " TEXT NOT NULL "                         +

                ");";

        // Could combine the creates into one string
        // statement to call execSQL once if there
        // were more than a few method calls
        db.execSQL(SQL_CREATE_ARTICLE_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // If upgraded, this just drops the current tables and recreates them from scratch
        // Should retain the data if it were sensitive
        final String SQL_DROP_ARTICLE_TABLE = "DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME;
        final String SQL_DROP_VIDEO_TABLE = "DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME;

        db.execSQL(SQL_DROP_ARTICLE_TABLE);
        db.execSQL(SQL_DROP_VIDEO_TABLE);

        onCreate(db);
    }
}
