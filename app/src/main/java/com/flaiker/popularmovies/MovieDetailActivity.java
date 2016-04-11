/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.flaiker.popularmovies.contentprovider.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

/**
 * Detail activity for a movie.
 * <p/>
 * Not used on tablet devices.
 */
public class MovieDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private ActionBar mActionBar;
    private ImageView mPosterView;
    private ToggleButton mFavoriteToggle;

    private FavoritesHelper mFavoritesHelper;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFavoritesHelper = new FavoritesHelper(this);

        mPosterView = (ImageView) findViewById(R.id.detail_image_view);

        mFavoriteToggle = (ToggleButton) findViewById(R.id.detail_favorite_toggle);

        mUri = getIntent().getParcelableExtra(MovieDetailFragment.ARG_MOVIE_URI);

        getSupportLoaderManager().initLoader(0, null, this);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.ARG_MOVIE_URI, mUri);
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
            navigateUpTo(new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            return new CursorLoader(this, mUri, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        Movie movie = Movie.fromCursor(data);

        if (movie != null) {
            mActionBar.setTitle(movie.getTitle());
            Picasso.with(this).load(movie.getBigImageUrl()).error(R.drawable.loading)
                    .into(mPosterView);

            try {
                mFavoriteToggle.setChecked(mFavoritesHelper.isMovieFavored(movie.getId()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void swapFavoriteStatus(View view) {
        long id = MovieContract.MovieEntry.getIdFromUri(mUri);

        try {
            if (!((ToggleButton) view).isChecked()) {
                mFavoritesHelper.removeFavorite(id);
                Toast.makeText(this, "Removed favorite", Toast.LENGTH_SHORT).show();
            } else {
                mFavoritesHelper.addFavorite(id);
                Toast.makeText(this, "Added favorite", Toast.LENGTH_SHORT).show();
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, String.format("Could not (un)favor: %s", e.getMessage()),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
