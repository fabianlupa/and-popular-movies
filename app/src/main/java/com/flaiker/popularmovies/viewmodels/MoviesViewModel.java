package com.flaiker.popularmovies.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.flaiker.popularmovies.BR;
import com.flaiker.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * ViewModel for managing a quantity of {@link com.flaiker.popularmovies.models.Movie Movies} /
 * {@link MovieViewModel MovieViewModels}.
 */
public class MoviesViewModel extends BaseObservable {
    @Bindable
    public final ObservableList<MovieViewModel> movies;
    public final ItemView itemView = ItemView.of(BR.vm, R.layout.movie_list_content);

    private List<InteractionListener> mListeners;

    /**
     * @param movies The MovieViewModels to manage
     */
    public MoviesViewModel(List<MovieViewModel> movies) {
        this.movies = new ObservableArrayList<>();
        this.movies.addAll(movies);

        mListeners = new ArrayList<>();

        // Register this listener as a handler for events in MovieViewModels
        for (MovieViewModel movie : this.movies) {
            movie.addListener(new MovieViewModel.Listener() {
                @Override
                public void onClick(MovieViewModel movie) {
                    showDetail(movie);
                }
            });
        }
    }

    /**
     * Invoke all registered listeners to show details for the given movie.
     *
     * @param movie The movie to show details for
     */
    public void showDetail(MovieViewModel movie) {
        for (InteractionListener listener : mListeners) listener.showDetail(movie.getId());
    }

    /**
     * Add a listener to provide view interaction on the ui-layer.
     *
     * @param listener The listener to add
     */
    public void addInteractionListener(InteractionListener listener) {
        if (mListeners.contains(listener))
            throw new IllegalArgumentException("Listener already registered.");

        mListeners.add(listener);
    }

    /**
     * Interface for registering on callbacks of this ViewModel.
     * <p/>
     * Use {@link MovieViewModel#addListener(MovieViewModel.Listener)} to attach.
     */
    public interface InteractionListener {
        /**
         * A detail view of the movie was requested.
         *
         * @param movieId The ID of the movie to provide a detail view for
         */
        void showDetail(String movieId);
    }
}
