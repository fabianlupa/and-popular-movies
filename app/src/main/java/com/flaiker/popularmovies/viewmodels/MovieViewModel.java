package com.flaiker.popularmovies.viewmodels;

import android.databinding.BaseObservable;
import android.view.View;

import com.flaiker.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends BaseObservable {
    private final Movie mMovie;
    private List<Listener> mListeners;

    public MovieViewModel(Movie movie) {
        this.mListeners = new ArrayList<>();
        this.mMovie = movie;
    }

    public String getId() {
        return mMovie.getId();
    }

    public String getName() {
        return mMovie.getName();
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public final View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (Listener listener : mListeners) listener.onClick(MovieViewModel.this);
        }
    };

    public interface Listener {
        void onClick(MovieViewModel movie);
    }
}
