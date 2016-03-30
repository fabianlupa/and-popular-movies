/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies.dao;

import com.flaiker.popularmovies.models.Movie;

import java.util.List;

public interface MovieDao {
    Movie getMovieById(int movieId);
    List<Movie> getPopularMovies();
}
