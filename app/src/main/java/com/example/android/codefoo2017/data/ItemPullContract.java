package com.example.android.codefoo2017.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class ItemPullContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.codefoo2017";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ARTICLES = "articles";

    public static final String PATH_VIDEOS = "videos";

    public static final class ArticleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ARTICLES)
                .build();

        public static final String TABLE_NAME = "articles";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_THUMBNAIL = "thumbnail";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_URL = "articleUrl";

        public static final String COLUMN_TIME_PASSED = "timePassed";
    }

    public static final class VideoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VIDEOS)
                .build();

        public static final String TABLE_NAME = "videos";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_THUMBNAIL = "thumbnail";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_URL = "url";

    }

}
