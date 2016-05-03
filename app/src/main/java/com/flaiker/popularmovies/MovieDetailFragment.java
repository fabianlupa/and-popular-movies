/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Fragment for a movie detail screen. Used by {@link MovieDetailActivity} on smartphones and
 * {@link MovieListActivity} on tablets.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener {

    /**
     * ID of the movie to provide details for
     */
    public static final String ARG_MOVIE_URI = "movie_uri";

    private Uri mUri;
    private FavoritesHelper mFavoritesHelper;

    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mSynopsis;
    private TextView mTitleTablet;
    private ImageView mImageTablet;
    private ToggleButton mFavoriteToggle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFavoritesHelper = new FavoritesHelper(getActivity());

        if (getArguments().containsKey(ARG_MOVIE_URI)) {
            mUri = getArguments().getParcelable(ARG_MOVIE_URI);
            /*// Load movie from static field in the main activity for now
            if (MovieListActivity.sMovies.containsKey(key)) {
                mMovie = MovieListActivity.sMovies.get(key);

                Activity activity = this.getActivity();
                CollapsingToolbarLayout appBarLayout
                        = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mMovie.getTitle());
                }
            }*/
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(ARG_MOVIE_URI);
        }
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        mReleaseDate = ((TextView) rootView.findViewById(R.id.movie_detail_release_date));
        mSynopsis = ((TextView) rootView.findViewById(R.id.movie_detail_synopsis));
        mRating = ((TextView) rootView.findViewById(R.id.movie_detail_votes));
        mTitleTablet = (TextView) rootView.findViewById(R.id.movie_detail_title);
        mImageTablet = (ImageView) rootView.findViewById(R.id.movie_detail_poster_tablet);

        mFavoriteToggle = (ToggleButton) rootView.findViewById(R.id.detail_favorite_toggle);
        if (mFavoriteToggle != null) mFavoriteToggle.setOnClickListener(this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            return new CursorLoader(getActivity(), mUri, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        Movie movie = Movie.fromCursor(data);

        if (movie != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(movie.getReleaseDate());
            mReleaseDate.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            mRating.setText(String.format(Locale.US, "%.2f/10", movie.getVotesAverage()));
            mSynopsis.setText(movie.getSynopsis());
            if (mImageTablet != null) {
                Picasso.with(getActivity()).load(movie.getImageUrl()).error(R.drawable.loading)
                        .into(mImageTablet);
            }
            if (mTitleTablet != null) mTitleTablet.setText(movie.getTitle());

            if (mFavoriteToggle != null) {
                mFavoriteToggle.setChecked(mFavoritesHelper.isMovieFavored(movie.getId()));
            }

            // Fetch the details of the movie
            FetchMovieDetailsTask task = new FetchMovieDetailsTask(
                    new FetchMovieDetailsTask.Callback() {
                @Override
                public void onFinish(FetchMovieDetailsTask.ResultBundle result) {
                    // TODO: fill trailer list
                    // TODO: fill review list
                }
            });
            task.execute(String.valueOf(movie.getId()));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_favorite_toggle:
                String msg = FavoritesHelper
                        .swapFavoriteStatusFromToggleButton((ToggleButton) v, mUri, mFavoritesHelper);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
