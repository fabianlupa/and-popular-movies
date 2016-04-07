/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for managing movie favorites in shared preferences.
 */
public class FavoritesHelper {
    private static final String PREFERENCES_KEY = "favorite_movies";
    private final SharedPreferences mPreferences;

    public FavoritesHelper(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Add a favorite.
     *
     * @param movieId Movie's id
     * @throws IllegalArgumentException
     * @throws JSONException
     */
    public void addFavorite(long movieId) throws IllegalArgumentException, JSONException {
        List<Long> favorites = getFavorites();

        if (favorites.contains(movieId))
            throw new IllegalArgumentException("Movie is already favored.");

        favorites.add(movieId);
        persistList(favorites);
    }

    /**
     * Remove a favorite.
     *
     * @param movieId Movie's id
     * @throws IllegalArgumentException
     * @throws JSONException
     */
    public void removeFavorite(long movieId) throws IllegalArgumentException, JSONException {
        List<Long> favorites = getFavorites();

        if (!favorites.contains(movieId))
            throw new IllegalArgumentException("Movie cannot be unfavored because it is not " +
                    "favored.");

        favorites.remove(movieId);
        persistList(favorites);
    }

    /**
     * Get currently favored movies.
     *
     * @return List of favored movies
     * @throws JSONException
     */
    public List<Long> getFavorites() throws JSONException {
        List<Long> movieList = new ArrayList<>();

        String movieJson = mPreferences.getString(PREFERENCES_KEY, "[]");
        JSONArray movieArray = new JSONArray(movieJson);

        for (int i = 0; i < movieArray.length(); i++) {
            movieList.add(movieArray.getLong(i));
        }

        return movieList;
    }

    private void persistList(List<Long> favorites) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREFERENCES_KEY, new JSONArray(favorites).toString());
        editor.apply();
    }
}
