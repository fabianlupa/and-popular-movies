/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
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

public class AsyncMovieLoader extends AsyncTaskLoader<Map<String, Movie>> {
    private static final String LOG = AsyncMovieLoader.class.getName();

    public AsyncMovieLoader(Context context) {
        super(context);
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
                .appendPath("popular")
                .appendQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                .build();

        try {
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();

            Log.i(LOG, String.format("API result:\n%s", stringBuilder.toString()));

            // Parse JSON
            JSONObject jObject = new JSONObject(stringBuilder.toString());
            JSONArray results = jObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonMovie = results.getJSONObject(i);
                Movie movie = new Movie(jsonMovie.getString("id"),
                        jsonMovie.getString("title"),
                        "https://image.tmdb.org/t/p/w342/" + jsonMovie.getString("poster_path"),
                        "https://image.tmdb.org/t/p/w780/" + jsonMovie.getString("poster_path"),
                        new SimpleDateFormat("yyyy-MM-DD", Locale.US)
                                .parse(jsonMovie.getString("release_date")),
                        Float.parseFloat(jsonMovie.getString("vote_average")),
                        jsonMovie.getString("overview"));
                resultList.put(movie.getId(), movie);
            }
        } catch (IOException e) {
            Log.e(LOG, e.toString());
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
