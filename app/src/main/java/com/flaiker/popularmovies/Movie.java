/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Model for a movie and its properties.
 */
public class Movie {
    private static final String IMG_URL_BASE = "https://image.tmdb.org/t/p/";
    private static final String IMG_URL_SMALL = "w342/";
    private static final String IMG_URL_BIG = "w780/";
    private final long id;
    private final Context context;
    private final String title;
    private final String imageUrl;
    private final String bigImageUrl;
    private final Date releaseDate;
    private final float votesAverage;
    private final String synopsis;

    public Movie(long id, Context context, String title, String imageUrl, String bigImageUrl,
                 Date releaseDate, float votesAverage, String synopsis) {
        this.id = id;
        this.context = context;
        this.title = title;
        this.imageUrl = imageUrl;
        this.bigImageUrl = bigImageUrl;
        this.releaseDate = releaseDate;
        this.votesAverage = votesAverage;
        this.synopsis = synopsis;
    }

    public long getId() {
        return id;
    }

    public Context getContext() {
        return context;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public float getVotesAverage() {
        return votesAverage;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public static Movie fromCursor(Cursor cursor) {
        try {
            return new Movie(
                    cursor.getLong(0),
                    Context.fromString(cursor.getString(1)),
                    cursor.getString(2),
                    IMG_URL_BASE + IMG_URL_SMALL + cursor.getString(3),
                    IMG_URL_BASE + IMG_URL_BIG + cursor.getString(3),
                    new SimpleDateFormat("yyyy-MM-DD", Locale.US).parse(cursor.getString(6)),
                    cursor.getFloat(4),
                    cursor.getString(5)
            );
        } catch (ParseException e) {
            return null;
        }
    }

    public enum Context {
        FAVORITE,
        TOP_RATED,
        POPULAR;

        public static Context fromString(String context) {
            return Context.valueOf(context.toUpperCase());
        }
    }
}
