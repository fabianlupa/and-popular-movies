/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * AsyncTask to fetch details (reviews and trailers) for a specific movie.
 */
public class FetchMovieDetailsTask
        extends AsyncTask<String, Void, FetchMovieDetailsTask.ResultBundle> {

    private static final String VIDEO_OBJECT_KEY = "videos";
    private static final String VIDEO_NAME_KEY = "name";
    private static final String VIDEO_KEY_KEY = "key";
    private static final String REQUEST_RESULT_ARR_KEY = "results";
    private static final String REVIEW_OBJECT_KEY = "reviews";
    private static final String REVIEW_AUTHOR_KEY = "author";
    private static final String REVIEW_CONTENT_KEY = "content";

    private final Callback callback;

    public FetchMovieDetailsTask(@NonNull Callback callback) {
        this.callback = callback;
    }

    @Override
    protected ResultBundle doInBackground(String... params) {
        if (params.length != 1)
            throw new IllegalArgumentException("Movie id is a required parameter.");

        String movieId = params[0];

        // Build query URI
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId)
                .appendQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                .appendQueryParameter("append_to_response", "reviews,videos")
                .build();

        try {
            // Get api result
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();

            // Parse JSON
            JSONObject jObject = new JSONObject(stringBuilder.toString());

            // Videos
            JSONObject videos = jObject.getJSONObject(VIDEO_OBJECT_KEY);
            JSONArray videosArray = videos.getJSONArray(REQUEST_RESULT_ARR_KEY);
            List<ResultBundle.Video> videoList = new ArrayList<>();
            for (int i = 0; i < videosArray.length(); i++) {
                JSONObject jsonVideo = videosArray.getJSONObject(i);
                videoList.add(new ResultBundle.Video(jsonVideo.getString(VIDEO_NAME_KEY),
                        jsonVideo.getString(VIDEO_KEY_KEY)));
            }

            // Reviews
            JSONObject reviews = jObject.getJSONObject(REVIEW_OBJECT_KEY);
            JSONArray reviewsArray = reviews.getJSONArray(REQUEST_RESULT_ARR_KEY);
            List<ResultBundle.Review> reviewList = new ArrayList<>();
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject jsonReview = reviewsArray.getJSONObject(i);
                reviewList.add(new ResultBundle.Review(jsonReview.getString(REVIEW_CONTENT_KEY),
                        jsonReview.getString(REVIEW_AUTHOR_KEY)));
            }

            return new ResultBundle(videoList, reviewList);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ResultBundle resultBundle) {
        callback.onFinish(resultBundle);
    }

    public interface Callback {
        void onFinish(ResultBundle result);
    }

    public static class ResultBundle {
        public final List<Video> mVideos;
        public final List<Review> mReviews;

        public ResultBundle(List<Video> videos, List<Review> reviews) {
            this.mVideos = videos;
            this.mReviews = reviews;
        }

        public static class Video {
            public final String mName;
            public final String mKey;

            public Video(String name, String key) {
                this.mName = name;
                this.mKey = key;
            }
        }

        public static class Review {
            public final String mAuthor;
            public final String mContent;

            public Review(String content, String author) {
                this.mContent = content;
                this.mAuthor = author;
            }
        }
    }
}
