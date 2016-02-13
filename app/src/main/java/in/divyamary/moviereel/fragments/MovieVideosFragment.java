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
import in.divyamary.moviereel.adapter.MovieVideoAdapter;
import in.divyamary.moviereel.data.MovieContract;
import in.divyamary.moviereel.model.MovieDetails;
import in.divyamary.moviereel.model.Video;


public class MovieVideosFragment extends Fragment {

    @Bind(R.id.recycler_view_videos)
    RecyclerView recyclerView;
    @Bind(R.id.text_empty)
    TextView emptyView;
    private List<Video> mVideoList;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieVideoAdapter mAdapter;


    public MovieVideosFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_movie_videos, container, false);
        ButterKnife.bind(this, rootView);
        setRecyclerView();
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getBoolean(MovieDetailsFragment.ARGUMENT_IS_MOVIE_DB)) {
                int movieId = arguments.getInt(MovieDetailsFragment.EXTRA_MOVIE_ID);
                Cursor movieVideosCursor = getContext().getContentResolver().query(
                        MovieContract.MovieVideosEntry.buildMovieVideosUri(movieId),
                        null,
                        MovieContract.MovieVideosEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{String.valueOf(movieId)},
                        null);
                if (movieVideosCursor.getCount() != 0) {
                    List<Video> videoList = new ArrayList<>();
                    while (movieVideosCursor.moveToNext()) {
                        Video video = new Video();
                        video.setKey(movieVideosCursor.getString(movieVideosCursor
                                .getColumnIndex(MovieContract.MovieVideosEntry.COLUMN_VIDEO_KEY)));
                        video.setName(movieVideosCursor.getString(movieVideosCursor
                                .getColumnIndex(MovieContract.MovieVideosEntry.COLUMN_VIDEO_NAME)));
                        video.setSize(movieVideosCursor.getInt(movieVideosCursor
                                .getColumnIndex(MovieContract.MovieVideosEntry.COLUMN_VIDEO_SIZE)));
                        videoList.add(video);
                    }
                    mAdapter.addData(videoList);
                    mAdapter.notifyDataSetChanged();
                    movieVideosCursor.close();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            } else {
                MovieDetails movieDetails = arguments.getParcelable(MovieDetailsFragment.ARGUMENT_MOVIE_DETAILS);
                if (movieDetails.getVideos() != null) {
                    mVideoList = movieDetails.getVideos().getResults();
                    if (mVideoList.size() > 0) {
                        mAdapter.addData(mVideoList);
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
        mVideoList = new ArrayList<>();
        mAdapter = new MovieVideoAdapter(mVideoList);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(mAdapter);
    }


}
