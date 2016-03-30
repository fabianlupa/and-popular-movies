/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies.models;

import java.util.Date;

/**
 * Model for a movie and its properties.
 */
public class Movie {
    private final String id;
    private final String title;
    private final String imageUrl;
    private final Date releaseDate;
    private final float votesAverage;
    private final String synopsis;

    /**
     * @param id           ID of the movie
     * @param title        Name of the movie
     * @param imageUrl     URL of the movie image
     * @param releaseDate  Release date of the movie
     * @param votesAverage Average vote rating
     * @param synopsis     Plot synopsis
     */
    public Movie(String id, String title, String imageUrl, Date releaseDate, float votesAverage, String synopsis) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.votesAverage = votesAverage;
        this.synopsis = synopsis;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public float getVotesAverage() {
        return votesAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }
}
