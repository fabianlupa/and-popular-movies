package com.flaiker.popularmovies.viewmodels;

import android.databinding.BaseObservable;
import android.view.View;

import com.flaiker.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel to manage a single instance of {@link Movie}.
 */
public class MovieViewModel extends BaseObservable {
    private final Movie mMovie;
    private List<Listener> mListeners;

    /**
     * @param movie The movie to manage
     */
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
