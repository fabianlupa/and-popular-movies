/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies.managers;

import com.flaiker.popularmovies.loaders.AsyncMovieLoaderWrapper;
import com.flaiker.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MovieManager {
    private AsyncMovieLoaderWrapper movieLoader;
    private Map<String, Movie> availableMovies;

    @Inject
    public MovieManager(AsyncMovieLoaderWrapper movieLoader) {
        this.movieLoader = movieLoader;

        movieLoader.addListener(callback);
        availableMovies = new HashMap<>();
    }

    public Movie getMovieById(String movieId) {
        return availableMovies.get(movieId);
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(availableMovies.values());
    }

    public void saveMovie(Movie movie) {
        // TODO: V2
        throw new UnsupportedOperationException("Not implemented");

    }

    public void saveMovies() {
        // TODO: V2
        throw new UnsupportedOperationException("Not implemented");
    }

    public void reload() {
        movieLoader.reload();
    }

    private final AsyncMovieLoaderWrapper.Callback callback
            = new AsyncMovieLoaderWrapper.Callback() {
        @Override
        public void onChange(Map<String, Movie> movies) {
            availableMovies = movies;
        }
    };
}
