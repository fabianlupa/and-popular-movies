package com.flaiker.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private Movie mMovie;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String key = getArguments().getString(ARG_ITEM_ID);

            if (MovieListActivity.sMovies.containsKey(key)) {
                mMovie = MovieListActivity.sMovies.get(key);

                Activity activity = this.getActivity();
                CollapsingToolbarLayout appBarLayout
                        = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mMovie.getTitle());
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mMovie.getReleaseDate());
        if (mMovie != null) {
            ((TextView) rootView.findViewById(R.id.movie_detail_release_date))
                    .setText(String.valueOf(calendar.get(Calendar.YEAR)));
            ((TextView) rootView.findViewById(R.id.movie_detail_synopsis))
                    .setText(mMovie.getSynopsis());
            ((TextView) rootView.findViewById(R.id.movie_detail_votes))
                    .setText(String.format("%.2f/10", mMovie.getVotesAverage()));

            // Tablet layout specific views

            TextView tabletTitleTextView = (TextView) rootView.findViewById(R.id.movie_detail_title);
            if (tabletTitleTextView != null) {
                tabletTitleTextView.setText(mMovie.getTitle());
            }

            ImageView imageView
                    = (ImageView) rootView.findViewById(R.id.movie_detail_poster_tablet);
            if (imageView != null) {
                Picasso.with(getActivity()).load(mMovie.getBigImageUrl()).error(R.drawable.loading)
                        .into(imageView);
            }
        }

        return rootView;
    }
}
