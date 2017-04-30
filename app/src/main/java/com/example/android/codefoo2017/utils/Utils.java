package com.example.android.codefoo2017.utils;

import com.example.android.codefoo2017.pullapi.Article;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Utils {

    public static final String PREF_INITIAL_LOAD_KEY = "pref_init_load_key";
    public static final String PREF_SIZE = "pref_pull_size";

    /**
     * Gets the time passed from the article's publish date
     * to the current date
     * @param currentTime today's date
     * @param article article to check how much time has passed
     * @return String of the time passed formatted as x days ago, x hours ago, x seconds ago
     */
    public static String getTimePassed(Date currentTime, Article article) {

        final int ONE_SECOND_MILLIS = 1000;
        final int SECONDS_PER_MINUTE = 60;
        final int MINUTES_PER_HOUR = 60;
        final int HOURS_PER_DAY = 24;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        Date pubDate;
        String timeUnit;
        long timePassed, outputTime;
        long hours = 0, seconds = 0, minutes = 0;

        try {
            pubDate = dateFormat.parse(article.getPublishDate());

            timePassed = currentTime.getTime() - pubDate.getTime();
            seconds = timePassed / ONE_SECOND_MILLIS % SECONDS_PER_MINUTE;
            minutes = timePassed / (MINUTES_PER_HOUR * ONE_SECOND_MILLIS);
            hours = minutes / MINUTES_PER_HOUR;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (hours >= HOURS_PER_DAY) {
            outputTime = hours / HOURS_PER_DAY;
            timeUnit = outputTime + " days ago";
        } else if (minutes >= MINUTES_PER_HOUR) {
            outputTime = minutes / MINUTES_PER_HOUR;
            timeUnit = outputTime + " hours ago";
        } else if (seconds >= SECONDS_PER_MINUTE) {
            outputTime = seconds / SECONDS_PER_MINUTE;
            timeUnit = outputTime + " minutes ago";
        } else {
            timeUnit = "not received";
        }
        return timeUnit;
    }
}
