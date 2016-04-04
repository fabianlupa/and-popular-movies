/*
 * Copyright (C) 2016 Fabian Lupa
 */

package com.flaiker.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flaiker.popularmovies.contentprovider.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Activity for showing movies loaded by {@link FetchMovieTask} in a grid.
 * <p/>
 * On tablets a detail fragment is loaded in the view, on phones {@link MovieDetailActivity} is
 * launched to provide details.
 */
public class MovieListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private boolean mTwoPane;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;
        mRecyclerView = (RecyclerView) recyclerView;
        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter());

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Start loading the movies
        getSupportLoaderManager().initLoader(0, null, this);
        //getSupportLoaderManager().restartLoader(0, null, this).forceLoad();

        FetchMovieTask task = new FetchMovieTask(this);
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // TODO: Add handling for sort order
            /*case R.id.menu_sort_popular:
                Bundle bundle = new Bundle();
                bundle.putInt(AsyncMovieLoader.ARG_SORT_ORDER, AsyncMovieLoader.SORT_ORDER_POPULAR);
                getSupportLoaderManager().restartLoader(0, bundle, this).forceLoad();

                return true;
            case R.id.menu_sort_top_rated:
                Bundle bundle2 = new Bundle();
                bundle2.putInt(AsyncMovieLoader.ARG_SORT_ORDER,
                        AsyncMovieLoader.SORT_ORDER_TOP_RATED);
                getSupportLoaderManager().restartLoader(0, bundle2, this).forceLoad();

                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((SimpleItemRecyclerViewAdapter) mRecyclerView.getAdapter()).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((SimpleItemRecyclerViewAdapter) mRecyclerView.getAdapter()).swapCursor(null);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        Cursor dataCursor;

        public SimpleItemRecyclerViewAdapter() {
        }

        public Cursor swapCursor(Cursor cursor) {
            if (dataCursor == cursor) return null;

            Cursor oldCursor = dataCursor;
            dataCursor = cursor;
            if (cursor != null) notifyDataSetChanged();

            return oldCursor;
        }

        public void changeCursor(Cursor cursor) {
            Cursor oldCursor = swapCursor(cursor);
            if (oldCursor != null) oldCursor.close();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Movie movie = getMovie(position);

            if (movie == null) return;

            Picasso.with(MovieListActivity.this).load(movie.getImageUrl())
                    .error(R.drawable.loading).into(holder.mImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(MovieDetailFragment.ARG_MOVIE_URI,
                                MovieContract.MovieEntry.buildMovieUri(movie.getId()));
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_MOVIE_URI,
                                MovieContract.MovieEntry.buildMovieUri(movie.getId()));

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return (dataCursor == null) ? 0 : dataCursor.getCount();
        }

        private Movie getMovie(int position) {
            dataCursor.moveToPosition(position);
            return Movie.fromCursor(dataCursor);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.item_image_view);
            }
        }
    }
}
