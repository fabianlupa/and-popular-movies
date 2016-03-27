package com.flaiker.popularmovies.views;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.flaiker.popularmovies.R;
import com.flaiker.popularmovies.databinding.ActivityMovieListBinding;
import com.flaiker.popularmovies.models.Movie;
import com.flaiker.popularmovies.viewmodels.MovieViewModel;
import com.flaiker.popularmovies.viewmodels.MoviesViewModel;

import java.util.ArrayList;
import java.util.List;

public class MoviesListActivity extends AppCompatActivity implements MoviesViewModel.InteractionListener {
    private boolean mTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieListBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_movie_list);

        List<MovieViewModel> movieList = new ArrayList<>();
        movieList.add(new MovieViewModel(new Movie("1", "Movie1")));
        movieList.add(new MovieViewModel(new Movie("2", "Movie2")));
        movieList.add(new MovieViewModel(new Movie("3", "Movie3")));
        MoviesViewModel vm = new MoviesViewModel(movieList);
        vm.addInteractionListener(this);
        binding.setVm(vm);
        binding.executePendingBindings();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPaneMode = true;
        }
    }

    @Override
    public void showDetail(String movieId) {
        if (mTwoPaneMode) {
            Bundle arguments = new Bundle();
            arguments.putString(MovieDetailFragment.ARG_MOVIE_ID, movieId);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailFragment.ARG_MOVIE_ID, movieId);
            startActivity(intent);
        }
    }
}
