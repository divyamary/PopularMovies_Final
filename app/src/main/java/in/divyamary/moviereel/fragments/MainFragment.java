package in.divyamary.moviereel.fragments;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.divyamary.moviereel.MoviesClient;
import in.divyamary.moviereel.R;
import in.divyamary.moviereel.adapter.MovieFavoritesAdapter;
import in.divyamary.moviereel.adapter.MovieRecyclerAdapter;
import in.divyamary.moviereel.api.ErrorBundle;
import in.divyamary.moviereel.bus.MovieBus;
import in.divyamary.moviereel.data.MovieContract;
import in.divyamary.moviereel.event.DiscoverMoviesResultEvent;
import in.divyamary.moviereel.event.ErrorResultEvent;
import in.divyamary.moviereel.helper.Utils;
import in.divyamary.moviereel.model.Movie;

/**
 * MovieGridFragment displays the movie posters on app start.
 */
public class MainFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String BUNDLE_MOVIES_LIST = "BUNDLE_MOVIES_LIST";
    private static final String BUNDLE_IS_LOADING = "BUNDLE_IS_LOADING";
    private static final String BUNDLE_CURRENT_PAGE = "BUNDLE_CURRENT_PAGE";
    private static final String BUNDLE_SCROLL_POSITION = "BUNDLE_SCROLL_POSITION";
    private static final String BUNDLE_VIEW_REFRESH = "BUNDLE_VIEW_REFRESH";
    private static final String BUNDLE_SORT_TYPE = "BUNDLE_SORT_TYPE";
    private static final String[] MOVIE_GRID_PROJECTION = new String[]{
            MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieDetailsEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieDetailsEntry.COLUMN_MOVIE_RELEASE_DATE,
    };
    private static final int LOADER_ID = 0;
    private final int mVisibleThreshold = 4;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.spinner_sort)
    Spinner spinner;
    @Bind(R.id.text_empty)
    TextView emptyView;
    private MovieRecyclerAdapter mAdapter;
    private MovieFavoritesAdapter mFavoritesAdapter;
    private GridLayoutManager mLayoutManager;
    private MoviesClient mMoviesClient;
    private List<Movie> mMoviesList;
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;
    private int mCurrentPage = 1;
    private boolean mIsRefresh = false;
    private boolean mIsLoading = true;
    private boolean mIsViewRestored;
    private int mScrollPosition;
    private Cursor mCursorData;
    private boolean mIsBusRegistered;
    private String mSortType;


    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        mMoviesList = new ArrayList<>();
        mMoviesClient = new MoviesClient(getContext());
        mAdapter = new MovieRecyclerAdapter(mMoviesList, new MovieRecyclerAdapter.MovieAdapterOnClickHandler() {
            @Override
            public void onClick(int movieId) {
                ((Callback) getActivity()).onItemSelected(movieId);
            }
        });
        mFavoritesAdapter = new MovieFavoritesAdapter(new MovieFavoritesAdapter.MovieAdapterOnClickHandler() {
            @Override
            public void onClick(int movieId) {
                ((Callback) getActivity()).onItemSelected(movieId);
            }
        });
        setSpinner();
        setRecyclerView();
        if (savedInstanceState != null) {
            ArrayList<Movie> savedMovieList = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES_LIST);
            mIsLoading = savedInstanceState.getBoolean(BUNDLE_IS_LOADING);
            mCurrentPage = savedInstanceState.getInt(BUNDLE_CURRENT_PAGE);
            mIsRefresh = savedInstanceState.getBoolean(BUNDLE_VIEW_REFRESH);
            mScrollPosition = savedInstanceState.getInt(BUNDLE_SCROLL_POSITION);
            mLayoutManager.scrollToPosition(mScrollPosition);
            mSortType = savedInstanceState.getString(BUNDLE_SORT_TYPE);
            if (mSortType.equals(getString(R.string.pref_rating)) ||
                    mSortType.equals(getString(R.string.pref_popularity))) {
                recyclerView.setAdapter(mAdapter);
                mAdapter.addData(savedMovieList, mIsRefresh);
                mAdapter.notifyDataSetChanged();
            } else if (mSortType.equals(getString(R.string.pref_favorites))) {
                recyclerView.setAdapter(mFavoritesAdapter);
                mFavoritesAdapter.swapCursor(mCursorData);
            }
            mIsViewRestored = true;
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<Movie> savedMovieList = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES_LIST);
            mIsLoading = savedInstanceState.getBoolean(BUNDLE_IS_LOADING);
            mCurrentPage = savedInstanceState.getInt(BUNDLE_CURRENT_PAGE);
            mIsRefresh = savedInstanceState.getBoolean(BUNDLE_VIEW_REFRESH);
            mScrollPosition = savedInstanceState.getInt(BUNDLE_SCROLL_POSITION);
            mSortType = savedInstanceState.getString(BUNDLE_SORT_TYPE);
            mLayoutManager.scrollToPosition(mScrollPosition);
            if (mSortType.equals(getString(R.string.pref_rating)) ||
                    mSortType.equals(getString(R.string.pref_popularity))) {
                recyclerView.setAdapter(mAdapter);
                mAdapter.addData(savedMovieList, mIsRefresh);
                mAdapter.notifyDataSetChanged();
            } else if (mSortType.equals(getString(R.string.pref_favorites))) {
                recyclerView.setAdapter(mFavoritesAdapter);
                mFavoritesAdapter.swapCursor(mCursorData);
            }
            mIsViewRestored = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsViewRestored) {
            String count;
            String sortParam;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            mSortType = sharedPreferences.getString(getString(R.string.pref_sort_type),
                    getString(R.string.pref_sort_default_value));
            if (Utils.isInternetConnected(getContext())) {
                if (mSortType.equals(getString(R.string.pref_popularity))) {
                    sortParam = getString(R.string.sort_popularity);
                    count = getString(R.string.default_vote_count);
                    recyclerView.setAdapter(mAdapter);
                    mMoviesClient.discoverMovies(mCurrentPage, mIsRefresh, sortParam, count);
                } else if (mSortType.equals(getString(R.string.pref_rating))) {
                    sortParam = getString(R.string.sort_rating);
                    count = getString(R.string.vote_count);
                    recyclerView.setAdapter(mAdapter);
                    mMoviesClient.discoverMovies(mCurrentPage, mIsRefresh, sortParam, count);
                }
            } else {
                Snackbar snackbar = Snackbar.make(recyclerView, getString(R.string.no_internet),
                        Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.grey_900));
                snackbar.show();
            }
            if (mSortType.equals(getString(R.string.pref_favorites))) {
                recyclerView.setAdapter(mFavoritesAdapter);
                mFavoritesAdapter.swapCursor(mCursorData);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mSortType.equals(getString(R.string.pref_popularity)) || mSortType.equals(getString(R.string.pref_rating))) {
            outState.putParcelableArrayList(BUNDLE_MOVIES_LIST, (ArrayList<? extends Parcelable>) mAdapter.getData());
            outState.putBoolean(BUNDLE_IS_LOADING, mIsLoading);
            outState.putInt(BUNDLE_CURRENT_PAGE, mCurrentPage);
            outState.putBoolean(BUNDLE_VIEW_REFRESH, mIsRefresh);
        }
        outState.putInt(BUNDLE_SCROLL_POSITION, mLayoutManager.findFirstVisibleItemPosition());
        outState.putString(BUNDLE_SORT_TYPE, mSortType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsViewRestored = true;
    }

    @Override
    public void onDestroy() {
        unregisterBus();
        super.onDestroy();
    }

    private void registerBus() {
        if (!mIsBusRegistered) {
            MovieBus.getInstance().register(this);
            mIsBusRegistered = true;
        }
    }

    private void unregisterBus() {
        if (mIsBusRegistered) {
            MovieBus.getInstance().unregister(this);
            mIsBusRegistered = false;
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        //On layout change, change span of GridLayoutManager
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onGlobalLayout() {
                            recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            mLayoutManager.setSpanCount(getActivity().getResources().getInteger(R.integer.num_columns));
                            mLayoutManager.requestLayout();
                        }
                    });
        }
        mLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.num_columns));
        recyclerView.setLayoutManager(mLayoutManager);
        // Add scroll listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                mVisibleItemCount = mLayoutManager.getChildCount();
                mTotalItemCount = mLayoutManager.getItemCount();
                if (dy > 0) {
                    if (!mIsLoading) {
                        if (mFirstVisibleItem + mVisibleItemCount >= mTotalItemCount - mVisibleThreshold) {
                            mMoviesClient = new MoviesClient(getContext());
                            String count = "";
                            String sortParam = "";
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            mSortType = sharedPreferences.getString(getString(R.string.pref_sort_type),
                                    getString(R.string.pref_sort_default_value));
                            if (mSortType.equals(getString(R.string.pref_popularity))) {
                                sortParam = getString(R.string.sort_popularity);
                                count = getString(R.string.default_vote_count);
                            } else if (mSortType.equals(getString(R.string.pref_rating))) {
                                sortParam = getString(R.string.sort_rating);
                                count = getString(R.string.vote_count);
                            }
                            mMoviesClient.discoverMovies(mCurrentPage, mIsRefresh, sortParam, count);
                            mIsLoading = true;
                        }
                    }
                }
            }
        });
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_array, R.layout.custom_spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String spinnerSelection = sharedPreferences.getString(getString(R.string.pref_sort_type), getString(R.string.pref_popularity));
        if (spinnerSelection.equals(getString(R.string.pref_popularity))) {
            //Set animate to false to prevent onItemSelected callback on spinner intialization.
            spinner.setSelection(0, false);
        } else if (spinnerSelection.equals(getString(R.string.pref_rating))) {
            spinner.setSelection(1, false);
        } else {
            spinner.setSelection(2, false);
        }
        spinner.setOnItemSelectedListener(this);
    }

    @Subscribe
    public void onDiscoverMoviesResult(DiscoverMoviesResultEvent event) {
        mIsRefresh = event.isRefresh();
        mIsLoading = event.isLoading();
        mMoviesList = event.getMoviePage().getResults();
        mAdapter.addData(mMoviesList, mIsRefresh);
        mAdapter.notifyDataSetChanged();
        mIsRefresh = false;
        mCurrentPage++;
    }

    @Subscribe
    public void onRetrofitFailure(ErrorResultEvent event) {
        ErrorBundle errorBundle = event.getErrorBundle();
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(getContext());
        mAlertDialog.setCancelable(false);
        mAlertDialog.setTitle(getString(R.string.alert_dialog_title));
        if (errorBundle != null) {
            if (errorBundle.getAppMessage().equals("Unknown exception")) {
                mAlertDialog.setMessage(getString(R.string.unknown_error));
            } else if (errorBundle.getAppMessage().equals("Socket timeout")) {
                mAlertDialog.setMessage(getString(R.string.timeout_error));
            } else if (errorBundle.getAppMessage().equals("Authorization exception")) {
                mAlertDialog.setMessage(getString(R.string.unauthorized_error));
            } else if (errorBundle.getAppMessage().equals("NotFoundException")) {
                mAlertDialog.setMessage(getString(R.string.not_found_error));
            } else {
                mAlertDialog.setMessage(errorBundle.getAppMessage());
            }
        }
        mAlertDialog.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        mAlertDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        if (recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }
        mSortType = adapterView.getItemAtPosition(pos).toString();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_sort_type), mSortType).apply();
        mIsRefresh = true;
        //Set mCurrentPage back to 1 before running AsyncTask
        mCurrentPage = 1;
        String count;
        String sortParam;
        if (mSortType.equals(getString(R.string.pref_popularity))) {
            sortParam = getString(R.string.sort_popularity);
            count = getString(R.string.default_vote_count);
            recyclerView.setAdapter(mAdapter);
            mMoviesClient.discoverMovies(mCurrentPage, mIsRefresh, sortParam, count);
        } else if (mSortType.equals(getString(R.string.pref_rating))) {
            sortParam = getString(R.string.sort_rating);
            count = getString(R.string.vote_count);
            recyclerView.setAdapter(mAdapter);
            mMoviesClient.discoverMovies(mCurrentPage, mIsRefresh, sortParam, count);
        } else {
            if (mCursorData != null && mCursorData.getCount() > 0) {
                recyclerView.setAdapter(mFavoritesAdapter);
                mFavoritesAdapter.swapCursor(mCursorData);
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(mFavoritesAdapter);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.MovieDetailsEntry.CONTENT_URI, MOVIE_GRID_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorData = data;
        if (mSortType.equals(getString(R.string.pref_favorites))) {
            if (mCursorData != null && mCursorData.getCount() > 0) {
                recyclerView.setAdapter(mFavoritesAdapter);
                mFavoritesAdapter.swapCursor(mCursorData);
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(mFavoritesAdapter);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoritesAdapter.swapCursor(null);
    }


    public interface Callback {
        void onItemSelected(int id);
    }
}
