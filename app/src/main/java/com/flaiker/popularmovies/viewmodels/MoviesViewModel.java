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

public class MoviesViewModel extends BaseObservable {
    @Bindable
    public final ObservableList<MovieViewModel> movies;
    public final ItemView itemView = ItemView.of(BR.vm, R.layout.movie_list_content);

    private List<InteractionListener> mListeners;


    public MoviesViewModel(List<MovieViewModel> movies) {
        this.movies = new ObservableArrayList<>();
        this.movies.addAll(movies);

        mListeners = new ArrayList<>();

        for (MovieViewModel movie : this.movies)
            movie.addListener(new MovieViewModel.Listener() {
                @Override
                public void onClick(MovieViewModel movie) {
                    showDetail(movie);
                }
            });
    }

    public void showDetail(MovieViewModel movie) {
        for (InteractionListener listener : mListeners) listener.showDetail(movie.getId());
    }

    public void addInteractionListener(InteractionListener listener) {
        if (mListeners.contains(listener))
            throw new IllegalArgumentException("Listener already registered.");

        mListeners.add(listener);
    }

    public interface InteractionListener {
        void showDetail(String movieId);
    }
}
