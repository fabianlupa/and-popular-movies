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
    private final String title;
    private final String imageUrl;
    private final String bigImageUrl;
    private final Date releaseDate;
    private final float votesAverage;
    private final String synopsis;

    public Movie(long id, String title, String imageUrl, String bigImageUrl, Date releaseDate,
                 float votesAverage, String synopsis) {
        this.id = id;
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
                    cursor.getString(1),
                    IMG_URL_BASE + IMG_URL_SMALL + cursor.getString(2),
                    IMG_URL_BASE + IMG_URL_BIG + cursor.getString(2),
                    new SimpleDateFormat("yyyy-MM-DD", Locale.US).parse(cursor.getString(5)),
                    cursor.getFloat(3),
                    cursor.getString(4)
            );
        } catch (ParseException e) {
            return null;
        }
    }
}
