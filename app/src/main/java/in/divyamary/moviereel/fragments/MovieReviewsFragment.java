package in.divyamary.moviereel.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.divyamary.moviereel.DividerItemDecoration;
import in.divyamary.moviereel.R;
import in.divyamary.moviereel.adapter.MovieReviewAdapter;
import in.divyamary.moviereel.data.MovieContract;
import in.divyamary.moviereel.model.MovieDetails;
import in.divyamary.moviereel.model.Review;


public class MovieReviewsFragment extends Fragment {

    @Bind(R.id.recycler_view_review)
    RecyclerView recyclerView;
    @Bind(R.id.text_empty)
    TextView emptyView;
    private MovieReviewAdapter mAdapter;
    private List<Review> mReviewsList;


    public MovieReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        ButterKnife.bind(this, rootView);
        setRecyclerView();
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getBoolean(MovieDetailsFragment.ARGUMENT_IS_MOVIE_DB)) {
                int movieId = arguments.getInt(MovieDetailsFragment.EXTRA_MOVIE_ID);
                Cursor movieReviewCursor = getContext().getContentResolver().query(
                        MovieContract.MovieReviewsEntry.buildMovieReviewsUri(movieId),
                        null,
                        MovieContract.MovieReviewsEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{String.valueOf(movieId)},
                        null);
                if (movieReviewCursor.getCount() != 0) {
                    List<Review> reviewsList = new ArrayList<>();
                    while (movieReviewCursor.moveToNext()) {
                        Review review = new Review();
                        review.setAuthor(movieReviewCursor.getString(movieReviewCursor
                                .getColumnIndex(MovieContract.MovieReviewsEntry.COLUMN_REVIEW_AUTHOR)));
                        review.setContent(movieReviewCursor.getString(movieReviewCursor
                                .getColumnIndex(MovieContract.MovieReviewsEntry.COLUMN_REVIEW_CONTENT)));
                        review.setUrl(movieReviewCursor.getString(movieReviewCursor
                                .getColumnIndex(MovieContract.MovieReviewsEntry.COLUMN_REVIEW_URL)));
                        reviewsList.add(review);
                    }
                    mAdapter.addData(reviewsList);
                    mAdapter.notifyDataSetChanged();
                    movieReviewCursor.close();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            } else {
                MovieDetails movieDetails = arguments.getParcelable(MovieDetailsFragment.ARGUMENT_MOVIE_DETAILS);
                if (movieDetails.getReviews() != null) {
                    mReviewsList = movieDetails.getReviews().getResults();
                    if (mReviewsList.size() > 0) {
                        mAdapter.addData(movieDetails.getReviews().getResults());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        }
        return rootView;
    }

    private void setRecyclerView() {
        mReviewsList = new ArrayList<>();
        mAdapter = new MovieReviewAdapter(getContext(), mReviewsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(mAdapter);
    }


}
