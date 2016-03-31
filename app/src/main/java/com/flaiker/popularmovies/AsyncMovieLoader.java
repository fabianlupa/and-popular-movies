/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Loads movie data from the themoviedb.org webservice asynchronously.
 */
public class AsyncMovieLoader extends AsyncTaskLoader<Map<String, Movie>> {
    private static final String LOG = AsyncMovieLoader.class.getName();

    // Arguments
    public static final String ARG_SORT_ORDER = "sort_order";
    public static final int SORT_ORDER_POPULAR = 0;
    public static final int SORT_ORDER_TOP_RATED = 1;

    // JSON deserialization
    private static final String RESULT_ARRAY_KEY = "results";
    private static final String MOVIE_ID_KEY = "id";
    private static final String MOVIE_TITLE_KEY = "title";
    private static final String MOVIE_POSTER_KEY = "poster_path";
    private static final String MOVIE_RELEASE_DATE_KEY = "release_date";
    private static final String MOVIE_VOTE_AVERAGE_KEY = "vote_average";
    private static final String MOVIE_OVERVIEW_KEY = "overview";
    private static final String IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_MEDIUM_RES = "w342/";
    private static final String IMAGE_HIGH_RES = "w780/";

    private final SortOrder mSortOrder;

    public AsyncMovieLoader(Context context, SortOrder sortOrder) {
        super(context);
        mSortOrder = sortOrder;
    }

    @Override
    public Map<String, Movie> loadInBackground() {
        Map<String, Movie> resultList = new HashMap<>();

        // Build query URI
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(mSortOrder.toString().toLowerCase())
                .appendQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                .build();

        try {
            // Get api result
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();

            // Parse JSON
            JSONObject jObject = new JSONObject(stringBuilder.toString());
            JSONArray results = jObject.getJSONArray(RESULT_ARRAY_KEY);
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonMovie = results.getJSONObject(i);
                Movie movie = new Movie(jsonMovie.getString(MOVIE_ID_KEY),
                        jsonMovie.getString(MOVIE_TITLE_KEY),
                        IMAGE_BASE_PATH + IMAGE_MEDIUM_RES + jsonMovie.getString(MOVIE_POSTER_KEY),
                        IMAGE_BASE_PATH + IMAGE_HIGH_RES + jsonMovie.getString(MOVIE_POSTER_KEY),
                        new SimpleDateFormat("yyyy-MM-DD", Locale.US)
                                .parse(jsonMovie.getString(MOVIE_RELEASE_DATE_KEY)),
                        Float.parseFloat(jsonMovie.getString(MOVIE_VOTE_AVERAGE_KEY)),
                        jsonMovie.getString(MOVIE_OVERVIEW_KEY));
                resultList.put(movie.getId(), movie);
            }
        } catch (IOException | ParseException | JSONException e) {
            Log.e(LOG, e.toString());
        }

        return resultList;
    }

    public enum SortOrder {
        POPULAR,
        TOP_RATED;

        public static SortOrder fromInt(int i) {
            switch (i) {
                case SORT_ORDER_POPULAR:
                    return POPULAR;
                case SORT_ORDER_TOP_RATED:
                    return TOP_RATED;
                default:
                    return POPULAR;
            }
        }
    }
}
