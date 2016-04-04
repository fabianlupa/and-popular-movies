/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies.contentprovider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

/**
 * Contract for internal movie database for use with android
 * {@link android.content.ContentProvider}.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.flaiker.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTES_AVERAGE = "votes_average";
        public static final String COLUMN_SYNOPSIS = "synopsis";

        /**
         * Build a movie uri for a specific movie by its id.
         *
         * @param id Movie's id
         * @return Uri for the movie
         */
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Retrieve a movie's id from its uri.
         *
         * @param uri The uri of the movie
         * @return The movie's id
         */
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
