/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.flaiker.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class AsyncMovieLoaderWrapper {
    private final Context context;
    private final LoaderManager loaderManager;
    private List<Callback> listeners;

    @Inject
    public AsyncMovieLoaderWrapper(Context context, LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;

        listeners = new ArrayList<>();
    }

    public void addListener(Callback listener) {
        if (listeners.contains(listener)) throw new IllegalArgumentException("Already registered");

        listeners.add(listener);
    }

    public void reload() {
        loaderManager.restartLoader(0, null, loaderCallbacks);
    }

    private final LoaderManager.LoaderCallbacks<Map<String, Movie>> loaderCallbacks
            = new LoaderManager.LoaderCallbacks<Map<String, Movie>>() {

        @Override
        public Loader<Map<String, Movie>> onCreateLoader(int id, Bundle args) {
            return new AsyncMovieLoader(context);
        }

        @Override
        public void onLoadFinished(Loader<Map<String, Movie>> loader, Map<String, Movie> data) {
            for (Callback callback : listeners) callback.onChange(data);
        }

        @Override
        public void onLoaderReset(Loader<Map<String, Movie>> loader) {
        }
    };

    public interface Callback {
        void onChange(Map<String, Movie> movies);
    }
}
