/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import java.util.Date;

public class Movie {
    private final String id;
    private final String title;
    private final String imageUrl;
    private final String bigImageUrl;
    private final Date releaseDate;
    private final float votesAverage;
    private final String synopsis;

    public Movie(String id, String title, String imageUrl, String bigImageUrl, Date releaseDate, float votesAverage,
                 String synopsis) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.bigImageUrl = bigImageUrl;
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

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public String getSynopsis() {
        return synopsis;
    }
}
