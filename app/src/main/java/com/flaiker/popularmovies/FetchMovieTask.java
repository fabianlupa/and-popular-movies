/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.flaiker.popularmovies.contentprovider.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class FetchMovieTask extends AsyncTask<String, Void, Void> {
    private static final String LOG = FetchMovieTask.class.getName();

    // Arguments
    public static final String SORT_ORDER_POPULAR = "POPULAR";
    public static final String SORT_ORDER_TOP_RATED = "TOP_RATED";

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

    private final Context mContext;

    public FetchMovieTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String sortOrder;
        if (params.length == 0 || (!params[0].equals(SORT_ORDER_POPULAR) &&
                !params[0].equals(SORT_ORDER_TOP_RATED))) {
            sortOrder = SORT_ORDER_POPULAR;
        } else {
            sortOrder = params[0];
        }

        // Build query URI
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(sortOrder.toLowerCase())
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
            Vector<ContentValues> vector = new Vector<>(results.length());
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonMovie = results.getJSONObject(i);
                /*Movie movie = new Movie(jsonMovie.getString(MOVIE_ID_KEY),
                        jsonMovie.getString(MOVIE_TITLE_KEY),
                        IMAGE_BASE_PATH + IMAGE_MEDIUM_RES + jsonMovie.getString(MOVIE_POSTER_KEY),
                        IMAGE_BASE_PATH + IMAGE_HIGH_RES + jsonMovie.getString(MOVIE_POSTER_KEY),
                        new SimpleDateFormat("yyyy-MM-DD", Locale.US)
                                .parse(jsonMovie.getString(MOVIE_RELEASE_DATE_KEY)),
                        Float.parseFloat(jsonMovie.getString(MOVIE_VOTE_AVERAGE_KEY)),
                        jsonMovie.getString(MOVIE_OVERVIEW_KEY));
                resultList.put(movie.getId(), movie);*/

                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_ID, jsonMovie.getString(MOVIE_ID_KEY));
                values.put(MovieContract.MovieEntry.COLUMN_CONTEXT, sortOrder);
                values.put(MovieContract.MovieEntry.COLUMN_TITLE, jsonMovie.getString(MOVIE_TITLE_KEY));
                values.put(MovieContract.MovieEntry.COLUMN_IMAGE, jsonMovie.getString(MOVIE_POSTER_KEY));
                values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, jsonMovie.getString(MOVIE_RELEASE_DATE_KEY));
                values.put(MovieContract.MovieEntry.COLUMN_VOTES_AVERAGE, jsonMovie.getString(MOVIE_VOTE_AVERAGE_KEY));
                values.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, jsonMovie.getString(MOVIE_OVERVIEW_KEY));

                vector.add(values);
            }

            ContentValues[] cvArray = new ContentValues[vector.size()];
            vector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        } catch (IOException | JSONException e) {
            Log.e(LOG, e.toString());
        }

        return null;
    }
}
