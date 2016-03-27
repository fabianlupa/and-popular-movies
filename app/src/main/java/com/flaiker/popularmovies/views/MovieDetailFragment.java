package com.flaiker.popularmovies.views;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flaiker.popularmovies.R;
import com.flaiker.popularmovies.databinding.MovieDetailBinding;
import com.flaiker.popularmovies.models.Movie;
import com.flaiker.popularmovies.viewmodels.MovieViewModel;

public class MovieDetailFragment extends Fragment {
    public static final String ARG_MOVIE_ID = "movie";
    private MovieViewModel mMovieViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE_ID)) {
            String arg = getArguments().getString(ARG_MOVIE_ID);
            mMovieViewModel = new MovieViewModel(new Movie(arg, "NO IDEA"));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mMovieViewModel.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MovieDetailBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.movie_detail, container, false);
        binding.setVm(mMovieViewModel);

        return binding.getRoot();
    }
}
