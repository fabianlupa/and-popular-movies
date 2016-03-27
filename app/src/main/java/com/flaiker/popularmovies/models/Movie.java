/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies.models;

/**
 * Model for a movie and its properties.
 */
public class Movie {
    private final String id;
    private final String name;

    /**
     * @param id   ID of the movie
     * @param name Name of the movie
     */
    public Movie(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
