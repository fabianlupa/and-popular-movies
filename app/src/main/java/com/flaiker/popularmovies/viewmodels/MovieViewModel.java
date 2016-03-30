package com.flaiker.popularmovies.viewmodels;

import android.databinding.BaseObservable;
import android.view.View;

import com.flaiker.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends BaseObservable {
    private final Movie movie;
    private List<Listener> listeners;

    public MovieViewModel(Movie movie) {
        this.listeners = new ArrayList<>();
        this.movie = movie;
    }

    public String getId() {
        return movie.getId();
    }

    public String getName() {
        return movie.getName();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public final View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (Listener listener : listeners) listener.onClick(MovieViewModel.this);
        }
    };

    public interface Listener {
        void onClick(MovieViewModel movie);
    }
}
