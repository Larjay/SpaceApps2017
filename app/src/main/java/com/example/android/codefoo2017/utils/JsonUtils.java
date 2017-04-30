package com.example.android.codefoo2017.utils;

import com.example.android.codefoo2017.pullapi.Article;
import com.example.android.codefoo2017.pullapi.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class JsonUtils {

    /**
     * Use as request type for articles
     */
    public static final int ARTICLES = 0;
    /**
     * Use as request type for videos
     */
    public static final int VIDEOS = 1;

    public static final int ERROR_TYPE = -1;

    /* Private Constants */
    private static final int PULL_SIZE = 10;
    private static final int MAX_PULL_SIZE = 20;
    private static final int START_INDEX = 30;
    private static final int NULL_INT = 0;
    private static final String NULL_STR = "null";
    private static final String ENCODE_TYPE = "UTF-8";
    private static final String ERROR = "Could not get request";

    /* IGN API Json strings */
    private static final String IGN_DATA = "data";
    private static final String IGN_METADATA = "metadata";
    private static final String IGN_NAME = "name";
    private static final String IGN_DESCRIPTION = "description";
    private static final String IGN_PUBLISH_DATE = "publishDate";
    private static final String IGN_LONG_TITLE = "longTitle";
    private static final String IGN_DURATION = "duration";
    private static final String IGN_URL = "url";
    private static final String IGN_SLUG = "slug";
    private static final String IGN_NETWORKS = "networks";
    private static final String IGN_STATE = "state";
    private static final String IGN_HEADLINE = "headline";
    private static final String IGN_SUB_HEADLINE = "subHeadline";
    private static final String IGN_ARTICLE_TYPE = "articleType";

    private static final String IGN_THUMBNAILS = "thumbnails";

    public static void main(String[] args) throws JSONException {

        Article[] articles;
        Video[] videos;

        articles = pullArticles(START_INDEX, PULL_SIZE);

        if (articles != null) {

            for (int index = 0; index < PULL_SIZE; index++) {
                articles[index].printJava();
            }
        }

        videos = pullVideos(START_INDEX, PULL_SIZE);

        if (videos != null) {

            for (int index = 0; index < PULL_SIZE; index++) {
                videos[index].printJava();
            }
        }

    }

    /**
     * Pull array of Videos from IGN API
     * @see Video Class
     * @param startIndex the index to start pagination from
     * @param count the amount of items to pull. Max of 20
     * @return array of video objects
     */
    public static Video[] pullVideos(int startIndex, int count) throws JSONException {

        Video[] pulledItems = new Video[count];

        for (int index = 0; index < count; index++) {
            pulledItems[index] = new Video();
        }

        String result = getRequest(VIDEOS, startIndex, count);

        if (result.isEmpty()) {
            return null;
        }

        JSONObject pullItem = new JSONObject(result);
        JSONArray pullArray = pullItem.getJSONArray(IGN_DATA);

        // Parses an 'array' of json objects from request
        for (int index = 0; index < count; index++) {

            JSONObject obj = pullArray.getJSONObject(index);
            JSONObject metaData = obj.getJSONObject(IGN_METADATA);

            JSONArray thumbnails = obj.getJSONArray(IGN_THUMBNAILS);
            JSONObject thumbnailUrlLarge = thumbnails.getJSONObject(2);

            pulledItems[index].setThumbnail(thumbnailUrlLarge.getString(IGN_URL));

            try {
                pulledItems[index].setName(metaData.getString(IGN_NAME));
            } catch (JSONException je) {
                pulledItems[index].setName(NULL_STR);
            }
            try {
                pulledItems[index].setDescription(metaData.getString(IGN_DESCRIPTION));
            } catch (JSONException je) {
                pulledItems[index].setDescription(NULL_STR);
            }
            try {
                pulledItems[index].setPublishDateLong(metaData.getString(IGN_PUBLISH_DATE));
            } catch (JSONException je) {
                pulledItems[index].setPublishDate(NULL_STR);
            }
            try {
                pulledItems[index].setLongTitle(metaData.getString(IGN_LONG_TITLE));
            } catch (JSONException je) {
                pulledItems[index].setLongTitle(NULL_STR);
            }
            try {
                pulledItems[index].setDuration(metaData.getInt(IGN_DURATION));
            } catch (JSONException je) {
                pulledItems[index].setDuration(NULL_INT);
            }
            try {
                pulledItems[index].setUrl(metaData.getString(IGN_URL));
            } catch (JSONException je) {
                pulledItems[index].setUrl(NULL_STR);
            }
            try {
                pulledItems[index].setSlug(metaData.getString(IGN_SLUG));
            } catch (JSONException je) {
                pulledItems[index].setSlug(NULL_STR);
            }
            try {
                pulledItems[index].setNetworks(metaData.getString(IGN_NETWORKS));
            } catch (JSONException je) {
                pulledItems[index].setNetworks(NULL_STR);
            }
            try {
                pulledItems[index].setState(metaData.getString(IGN_STATE));
            } catch (JSONException je) {
                pulledItems[index].setState(NULL_STR);
            }

        }

        return pulledItems;
    }

    /**
     * Pull an array of articles from IGN API
     * @param startIndex the index to start pagination from
     * @param count the number of items to pull
     * @return array of articles
     */
    public static Article[] pullArticles(int startIndex, int count) throws JSONException {

        Article[] pulledItems = new Article[count];

        for (int index = 0; index < count; index++) {
            pulledItems[index] = new Article();
        }

        String result = getRequest(ARTICLES, startIndex, count);

        if (result.isEmpty()) {
            return null;
        }

        JSONObject pullItem = new JSONObject(result);
        JSONArray pullArray = pullItem.getJSONArray(IGN_DATA);

        for (int index = 0; index < count; index++) {

            JSONObject obj = pullArray.getJSONObject(index);
            JSONArray thumbnails = obj.getJSONArray(IGN_THUMBNAILS);

            JSONObject thumbnailUrlLarge = thumbnails.getJSONObject(2);

            pulledItems[index].setThumbnail(thumbnailUrlLarge.getString(IGN_URL));

            JSONObject metaData = obj.getJSONObject(IGN_METADATA);

            try {
                pulledItems[index].setHeadline(metaData.getString(IGN_HEADLINE));
            } catch (JSONException je) {
                pulledItems[index].setHeadline(NULL_STR);
            }
            try {
                pulledItems[index].setNetworks(metaData.getString(IGN_NETWORKS));
            } catch (JSONException je) {
                pulledItems[index].setNetworks(NULL_STR);
            }
            try {
                pulledItems[index].setState(metaData.getString(IGN_STATE));
            } catch (JSONException je) {
                pulledItems[index].setState(NULL_STR);
            }
            try {
                pulledItems[index].setSlug(metaData.getString(IGN_SLUG));
            } catch (JSONException je) {
                pulledItems[index].setSlug(NULL_STR);
            }
            try {
                pulledItems[index].setSubHeadline(metaData.getString(IGN_SUB_HEADLINE));
            } catch (JSONException je) {
                pulledItems[index].setSubHeadline(NULL_STR);
            }
            try {
                pulledItems[index].setPublishDateLong(metaData.getString(IGN_PUBLISH_DATE));
            } catch (JSONException je) {
                pulledItems[index].setPublishDate(NULL_STR);
            }
            try {
                pulledItems[index].setArticleType(metaData.getString(IGN_ARTICLE_TYPE));
            } catch (JSONException je) {
                pulledItems[index].setArticleType(NULL_STR);
            }

            pulledItems[index].setUrl();
        }
        return pulledItems;
    }

    /**
     * Gets a request for the IGN API
     * uses the ign-apis.herokuapp.com request
     * @param reqType the type of request (articles or videos)
     * @param startIndex index to start pagination from
     * @param count count of items to pull (max of 20)
     * @return string of the response in json format
     */
    public static String getRequest(int reqType, int startIndex, int count) {

        String strBase = "http://ign-apis.herokuapp.com/";
        String strType;
        String strStartIndex = "startIndex=";
        String strAnd = "\u0026";
        String strCount = "count=";
        String strUrl;
        URL url;


        if (count > MAX_PULL_SIZE) {
            count = MAX_PULL_SIZE;
        }

        if (reqType == ARTICLES) {
            strType = "articles?";
        } else if (reqType == VIDEOS) {
            strType = "videos?";
        } else {
            return ERROR;
        }

        strStartIndex += startIndex;
        strCount += count;
        strUrl = strBase + strType + strStartIndex + strAnd + strCount;

        try {
            url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);		/* in milliseconds */
            conn.setConnectTimeout(15000);	/* in milliseconds */
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                return "";
            }
            InputStream is = conn.getInputStream();

            return readResponse(is);

        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String readResponse(InputStream stream) throws IOException {

        StringBuilder buf = new StringBuilder();
        InputStreamReader reader;
        reader = new InputStreamReader(stream, ENCODE_TYPE);

        int i;
        char c;

        while ((i = reader.read()) != -1) {
            c = (char) i;
            buf.append(c);
        }

        return buf.toString();
    }

}
