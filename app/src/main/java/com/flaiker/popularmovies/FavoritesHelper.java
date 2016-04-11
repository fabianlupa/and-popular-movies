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
    private static final List<FavoriteChangeListener> sListeners = new ArrayList<>();
    private final SharedPreferences mPreferences;

    public FavoritesHelper(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Add a favorite.
     *
     * @param movieId Movie's id
     * @throws IllegalArgumentException
     */
    public void addFavorite(long movieId) throws IllegalArgumentException {
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
     */
    public void removeFavorite(long movieId) throws IllegalArgumentException {
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
     */
    public List<Long> getFavorites() {
        List<Long> movieList = new ArrayList<>();

        try {
            String movieJson = mPreferences.getString(PREFERENCES_KEY, "[]");
            JSONArray movieArray = new JSONArray(movieJson);

            for (int i = 0; i < movieArray.length(); i++) {
                movieList.add(movieArray.getLong(i));
            }
        } catch (JSONException e) {
            // Return an empty list if a json exception occurred
            movieList.clear();
        }

        return movieList;
    }

    /**
     * Check if a movie is favored.
     *
     * @param movieId Movie's id
     * @return Movie is currently in the favorites
     * @throws JSONException
     */
    public boolean isMovieFavored(long movieId) throws JSONException {
        return getFavorites().contains(movieId);
    }

    /**
     * Add a change listener.
     *
     * @param listener The listener to add
     */
    public static void addListener(FavoriteChangeListener listener)
            throws IllegalArgumentException {
        if (sListeners.contains(listener))
            throw new IllegalArgumentException("Listener already attached.");

        sListeners.add(listener);
    }

    /**
     * Remove a change listener.
     *
     * @param listener The listener to remove
     */
    public static void removeListener(FavoriteChangeListener listener)
            throws IllegalArgumentException {
        if (sListeners.contains(listener))
            throw new IllegalArgumentException("Listener not attached.");

        sListeners.remove(listener);
    }

    private void persistList(List<Long> favorites) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREFERENCES_KEY, new JSONArray(favorites).toString());
        editor.apply();
        raiseUpdateEvent();
    }

    private void raiseUpdateEvent() {
        for (FavoriteChangeListener listener : sListeners) listener.onFavoriteChange();
    }

    /**
     * Event listener for subscribing to favorite changed events.
     */
    public interface FavoriteChangeListener {
        /**
         * List of favorites has been updated.
         */
        void onFavoriteChange();
    }
}
