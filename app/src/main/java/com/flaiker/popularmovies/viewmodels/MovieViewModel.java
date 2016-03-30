/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies.viewmodels;

import android.databinding.BaseObservable;
import android.view.View;

import com.flaiker.popularmovies.managers.MovieManager;
import com.flaiker.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * ViewModel to manage a single instance of {@link Movie}.
 */
public class MovieViewModel extends BaseObservable {
    private final MovieManager movieManager;

    private Movie mMovie;
    private List<Listener> mListeners;

    @Inject
    public MovieViewModel(MovieManager movieManager) {
        this.movieManager = movieManager;
        this.mListeners = new ArrayList<>();
    }

    public String setMovieId(String movieId() {
        mMovie = movieManager.getMovieById(movieId);
    }

    public String getId() {
        return mMovie.getId();
    }

    public String getName() {
        return mMovie.getTitle();
    }

    public String getImageUrl() {
        return mMovie.getImageUrl();
    }

    /**
     * Add a listener to receive callbacks from the managed movie.
     *
     * @param listener The listener to add
     */
    public void addListener(Listener listener) {
        if (mListeners.contains(listener))
            throw new IllegalArgumentException("Listener already registered.");

        mListeners.add(listener);
    }

    public final View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (Listener listener : mListeners) listener.onClick(MovieViewModel.this);
        }
    };

    /**
     * Interface for registering on callbacks of this ViewModel.
     * <p/>
     * Use {@link MovieViewModel#addListener(Listener)} to attach.
     */
    public interface Listener {
        /**
         * {@link MovieViewModel#onClick} has been fired.
         *
         * @param movie The instance the event was fired on
         */
        void onClick(MovieViewModel movie);
    }
}
