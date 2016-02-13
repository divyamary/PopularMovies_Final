package in.divyamary.moviereel.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.divyamary.moviereel.MoviesClient;
import in.divyamary.moviereel.R;
import in.divyamary.moviereel.activities.AboutActivity;
import in.divyamary.moviereel.adapter.ViewPagerAdapter;
import in.divyamary.moviereel.api.ErrorBundle;
import in.divyamary.moviereel.bus.MovieBus;
import in.divyamary.moviereel.data.MovieContract;
import in.divyamary.moviereel.event.ErrorResultEvent;
import in.divyamary.moviereel.event.GetMoviesDetailResultEvent;
import in.divyamary.moviereel.helper.Utils;
import in.divyamary.moviereel.model.Cast;
import in.divyamary.moviereel.model.Country;
import in.divyamary.moviereel.model.Credits;
import in.divyamary.moviereel.model.Crew;
import in.divyamary.moviereel.model.Genre;
import in.divyamary.moviereel.model.MovieDetails;
import in.divyamary.moviereel.model.Releases;
import in.divyamary.moviereel.model.SpokenLanguage;
import in.divyamary.moviereel.model.Video;

/**
 * MovieDetailFragment displays details about the selected movie.
 */
public class MovieDetailsFragment extends Fragment implements View.OnClickListener {

    public static final String EXTRA_MOVIE_ID = "in.divyamary.moviereel.EXTRA_MOVIE_ID";
    public static final String ARGUMENT_MOVIE_DETAILS = "ARGUMENT_MOVIE_DETAILS";
    public static final String ARGUMENT_IS_MOVIE_DB = "ARGUMENT_IS_MOVIE_DB";
    public static final String ARGUMENT_IS_TWO_PANE = "ARGUMENT_IS_TWO_PANE";
    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    private static final String BUNDLE_MOVIE_TITLE = "BUNDLE_MOVIE_TITLE";
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.text_toolbar_title)
    TextView toolBarTextView;
    @Bind(R.id.button_share)
    ImageButton shareButton;
    @Bind(R.id.button_back)
    @Nullable
    ImageButton backButton;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private boolean mIsBusRegistered;
    private int mMovieId;
    private MoviesClient mMoviesClient;
    private MovieDetails mMovieDetails;
    private boolean mIsViewRestored;
    private boolean mIsMovieInDB;
    private String mFirstVideoURI;
    private String mMovieTitle;
    private boolean mTwoPane;

    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        registerBus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle arguments = getArguments();
        View rootView;
        if (arguments != null && arguments.containsKey(EXTRA_MOVIE_ID)) {
            rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
            ButterKnife.bind(this, rootView);
            if (mIsMovieInDB) {
                arguments.putBoolean(ARGUMENT_IS_MOVIE_DB, true);
            }
            if (arguments.containsKey(ARGUMENT_IS_TWO_PANE)) {
                if (arguments.getBoolean(ARGUMENT_IS_TWO_PANE)) {
                    mTwoPane = true;
                    arguments.putBoolean(ARGUMENT_IS_TWO_PANE, true);
                    toolbar.inflateMenu(R.menu.menu_main);
                    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.action_about) {
                                Intent settingsIntent = new Intent(getActivity(), AboutActivity.class);
                                startActivity(settingsIntent);
                                return true;
                            }
                            return false;
                        }
                    });
                }
            }
            if (backButton != null) {
                backButton.setOnClickListener(this);
            }
            shareButton.setOnClickListener(this);
            if (savedInstanceState != null) {
                if (mMovieTitle != null) {
                    toolBarTextView.setText(mMovieTitle);
                } else if (mMovieDetails != null && mMovieDetails.getTitle() != null) {
                    toolBarTextView.setText(mMovieDetails.getTitle());
                }
                setUpViewPager(arguments);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_empty, container, false);
        }

        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieId = savedInstanceState.getInt(EXTRA_MOVIE_ID);
            mMovieTitle = savedInstanceState.getString(BUNDLE_MOVIE_TITLE);
            mIsViewRestored = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsViewRestored) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                mMovieId = arguments.getInt(EXTRA_MOVIE_ID);
                //Check if movie exists in DB
                Cursor movieInfoCursor = getContext().getContentResolver().query(
                        MovieContract.MovieDetailsEntry.buildMovieDetailsUri(mMovieId),
                        null,
                        MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{String.valueOf(mMovieId)},
                        null);
                if (movieInfoCursor.moveToFirst() && movieInfoCursor.getCount() != 0) {
                    MovieDetails movieDetails = getMovieDetailsFromDB(movieInfoCursor);
                    mIsMovieInDB = true;
                    arguments.putBoolean(ARGUMENT_IS_MOVIE_DB, mIsMovieInDB);
                    arguments.putParcelable(ARGUMENT_MOVIE_DETAILS, movieDetails);
                    setUpViewPager(arguments);
                } else {
                    if (Utils.isInternetConnected(getContext())) {
                        mMoviesClient = new MoviesClient(getContext());
                        mMoviesClient.getMovieDetails(mMovieId);
                    } else {
                        Snackbar snackbar = Snackbar.make(this.getView(),
                                getString(R.string.no_internet),
                                Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.grey_900));
                        snackbar.show();
                    }
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_MOVIE_ID, mMovieId);
        outState.putString(BUNDLE_MOVIE_TITLE, mMovieTitle);
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

    private MovieDetails getMovieDetailsFromDB(Cursor movieInfoCursor) {
        MovieDetails movieDetails = new MovieDetails();
        mMovieTitle = movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_TITLE));
        toolBarTextView.setText(mMovieTitle);
        movieDetails.setId(movieInfoCursor.getInt(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID)));
        movieDetails.setTitle(mMovieTitle);
        movieDetails.setOverview(movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_OVERVIEW)));
        movieDetails.setReleaseDate(movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_RELEASE_DATE)));
        movieDetails.setRuntime(movieInfoCursor.getInt(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_RUNTIME)));
        movieDetails.setPopularity(movieInfoCursor.getDouble(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_POPULARITY)));
        movieDetails.setVoteAverage(movieInfoCursor.getDouble(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_VOTE_AVG)));
        movieDetails.setVoteCount(movieInfoCursor.getInt(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_VOTE_CNT)));
        mFirstVideoURI = movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_VIDEO_URI));
        //Genres
        String genres = movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_GENRES));
        List<String> genreItems = Arrays.asList(genres.split("\\s*,\\s*"));
        List<Genre> genreList = new ArrayList<>(genreItems.size());
        for (String genreType : genreItems) {
            Genre genre = new Genre();
            genre.setName(genreType);
            genreList.add(genre);
        }
        movieDetails.setGenreList(genreList);
        //Spoken Languages
        String languages = movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_LANGUAGES));
        List<String> languageItems = Arrays.asList(languages.split("\\s*,\\s*"));
        List<SpokenLanguage> languageList = new ArrayList<>(languageItems.size());
        for (String langCode : languageItems) {
            SpokenLanguage spokenLanguage = new SpokenLanguage();
            spokenLanguage.setLang_code(langCode);
            languageList.add(spokenLanguage);
        }
        movieDetails.setSpokenLanguages(languageList);
        //Cast name and profile paths
        String castNames = movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_CASTNAMES));
        String castPaths = movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_CASTPATHS));
        List<String> castNameItems = Arrays.asList(castNames.split("\\s*,\\s*"));
        List<String> castPathItems = Arrays.asList(castPaths.split("\\s*,\\s*"));
        LinkedList<Cast> castList = new LinkedList<>();
        for (int i = 0; i < castNameItems.size(); i++) {
            Cast cast = new Cast();
            cast.setName(castNameItems.get(i));
            cast.setProfilePath(castPathItems.get(i));
            castList.add(cast);
        }
        //Crew
        Crew crew = new Crew();
        crew.setJob("Director");
        crew.setName(movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_DIRECTOR)));
        List<Crew> crewList = new ArrayList<>(1);
        crewList.add(crew);
        Credits credits = new Credits();
        credits.setCrew(crewList);
        credits.setCast(castList);
        movieDetails.setCredits(credits);
        //Certificate Information
        Country country = new Country();
        country.setCertification(movieInfoCursor.getString(movieInfoCursor.getColumnIndex(
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_CERTFICATION)));
        country.setLanguageCode("US");
        List<Country> countries = new ArrayList<>(1);
        countries.add(country);
        Releases releases = new Releases();
        releases.setCountries(countries);
        movieDetails.setReleases(releases);
        movieDetails.setGenreList(genreList);
        return movieDetails;
    }

    private void setUpViewPager(Bundle arguments) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), getChildFragmentManager(), arguments);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Subscribe
    public void onGetMovieDetailsResult(GetMoviesDetailResultEvent event) {
        mMovieDetails = event.getMovieDetails();
        toolBarTextView.setText(mMovieDetails.getTitle());
        Bundle arguments = getArguments();
        arguments.putParcelable(ARGUMENT_MOVIE_DETAILS, mMovieDetails);
        setUpViewPager(arguments);
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

    public void applyPalette(Palette palette) {
        int grey50Color = getResources().getColor(R.color.grey_50);
        int grey900Color = getResources().getColor(R.color.grey_900);
        toolbar.setBackgroundColor(palette.getDarkVibrantColor(grey900Color));
        toolBarTextView.setTextColor(palette.getLightMutedColor(grey50Color));
        tabLayout.setBackgroundColor(palette.getLightVibrantColor(grey50Color));
        tabLayout.setTabTextColors(palette.getDarkVibrantColor(grey900Color),
                palette.getDarkVibrantColor(grey900Color));
        tabLayout.setSelectedTabIndicatorColor(palette.getDarkVibrantColor(grey900Color));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_share) {
            String videoURI = null;
            String title;
            if (mIsMovieInDB) {
                videoURI = mFirstVideoURI;
                title = mMovieTitle;
            } else {
                title = mMovieDetails.getTitle();
                List<Video> videoResults = mMovieDetails.getVideos().getResults();
                Video firstVideo;
                if (videoResults.size() > 0) {
                    firstVideo = videoResults.get(0);
                    videoURI = Utils.getYoutubeURI(firstVideo.getKey()).toString();
                }
            }
            Intent shareIntent = new Intent(
                    android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(
                    android.content.Intent.EXTRA_SUBJECT, getContext().getString(R.string.share_subject));
            shareIntent.putExtra(
                    android.content.Intent.EXTRA_TITLE, title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, videoURI);
            getContext().startActivity(Intent.createChooser(shareIntent,
                    getString(R.string.share_chooser)));
        }
        if (view.getId() == R.id.button_back) {
            getActivity().finish();
        }

    }

    @Subscribe
    public void onRetrofitFailure(ErrorResultEvent event) {
        //In two pane, prevent dialog from being created twice.
        if (mTwoPane && !getActivity().hasWindowFocus()) {
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
                    mAlertDialog.setMessage(errorBundle.getRawMessage());
                }
            }
            mAlertDialog.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            mAlertDialog.show();
        }
    }

    public void onFABClicked() {
        mIsMovieInDB = true;
    }
}
