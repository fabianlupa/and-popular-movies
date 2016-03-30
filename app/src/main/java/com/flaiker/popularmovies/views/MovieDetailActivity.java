/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies.views;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.flaiker.popularmovies.R;
import com.flaiker.popularmovies.databinding.ActivityMovieDetailBinding;
import com.flaiker.popularmovies.models.Movie;
import com.flaiker.popularmovies.viewmodels.MovieViewModel;

import java.util.Date;

/**
 * Activity for showing detailed information on a {@link com.flaiker.popularmovies.models.Movie}.
 * <p/>
 * This uses a fragment ({@link MovieDetailFragment}) to show the content which is also used in
 * {@link MoviesListActivity} if a master-detail type of view is to be shown on a tablet.
 */
public class MovieDetailActivity extends AppCompatActivity
        implements MovieDetailFragment.DependencyInjector {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        binding.setMovie(new MovieViewModel(new Movie("1", "111", "https://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", new Date(), 1, "LOREMLOREM")));
        binding.executePendingBindings();

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        setTitle(binding.getMovie().getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.invalidateOptionsMenu();
        }


        invalidateOptionsMenu();

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            //arguments.putString(MovieDetailFragment.ARG_MOVIE_ID,
            //        getIntent().getExtras().getString(MovieDetailFragment.ARG_MOVIE_ID));
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MoviesListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public MovieViewModel getMovieViewModel() {
        return
    }
}
