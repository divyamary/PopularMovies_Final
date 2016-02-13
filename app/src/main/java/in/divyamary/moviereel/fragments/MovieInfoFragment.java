package in.divyamary.moviereel.fragments;

import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.divyamary.moviereel.Genres;
import in.divyamary.moviereel.R;
import in.divyamary.moviereel.data.MovieContract;
import in.divyamary.moviereel.helper.PaletteTransformation;
import in.divyamary.moviereel.helper.Utils;
import in.divyamary.moviereel.model.Cast;
import in.divyamary.moviereel.model.Country;
import in.divyamary.moviereel.model.Credits;
import in.divyamary.moviereel.model.Crew;
import in.divyamary.moviereel.model.Genre;
import in.divyamary.moviereel.model.MovieDetails;
import in.divyamary.moviereel.model.Releases;
import in.divyamary.moviereel.model.Review;
import in.divyamary.moviereel.model.SpokenLanguage;
import in.divyamary.moviereel.model.Video;

/**
 * MovieDetailFragment displays details about the selected movie.
 */
public class MovieInfoFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = MovieInfoFragment.class.getSimpleName();

    @Bind(R.id.text_overview)
    TextView overviewTextView;
    @Bind(R.id.text_vote_average)
    TextView voteAvgTextView;
    @Bind(R.id.text_vote_count)
    TextView voteCountTextView;
    @Bind(R.id.text_runtime)
    TextView runtimeTextView;
    @Bind(R.id.text_release_year)
    TextView yearTextView;
    @Bind(R.id.text_release_ddmm)
    TextView ddmmTextView;
    @Bind(R.id.text_director)
    TextView directorTextView;
    @Bind(R.id.text_certificate)
    TextView certificateTextView;
    @Bind(R.id.genre_layout)
    LinearLayout genreLayout;
    @Bind(R.id.cast_layout)
    LinearLayout castLayout;
    @Bind(R.id.text_screenplay)
    TextView screenPlayTextView;
    @Bind(R.id.text_composer)
    TextView composerTextView;
    @Bind(R.id.text_spoken_language)
    TextView languageTextView;
    @Bind(R.id.text_minutes)
    TextView minTitleTextView;
    @Bind(R.id.text_overview_title)
    TextView overviewTitleTextView;
    @Bind(R.id.text_director_title)
    TextView directorTitleTextView;
    @Bind(R.id.text_certification_title)
    TextView certTitleTextView;
    @Bind(R.id.text_composer_title)
    TextView composerTitleTextView;
    @Bind(R.id.text_screenplay_title)
    TextView screenPlayTitleTextView;
    @Bind(R.id.text_spoken_language_title)
    TextView languageTitleTextView;
    @Bind(R.id.text_genre_title)
    TextView genreTitleTextView;
    @Bind(R.id.text_cast_title)
    TextView castTitleTextView;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.image_backdrop)
    ImageView backdropView;
    StringBuilder genres;
    String directorName;
    @Bind(R.id.scrollView)
    NestedScrollView nestedScrollView;
    private MovieDetails mMovieDetails;
    private String mCertification;
    private Uri mPosterImageUri;
    private Uri mBackdropImageUri;
    private String mVideoKey;
    private boolean mIsOffline;
    private String mScreenPlay;
    private String mMusicComposer;
    private StringBuilder mCastNames;
    private LinkedList<Uri> mProfileURIs;
    private StringBuilder mLangCodes;
    private StringBuilder mCastPaths;
    private Target mBackdropTarget = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(getContext().getFilesDir(), mMovieDetails.getId() + "_backdrop.webp");
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
    private Target mPosterTarget = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(getContext().getFilesDir(), mMovieDetails.getId() + "_poster.webp");
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
    private Target mThumbnailTarget = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(getContext().getFilesDir(), mVideoKey + ".webp");
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
    private boolean mIsTwoPane;
    private int mCurrentApiVersion;
    private int mLightVibrantColor;
    private int mDarkVibrantColor;

    public MovieInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_movie_info, container, false);
        ButterKnife.bind(this, rootView);
        mCurrentApiVersion = android.os.Build.VERSION.SDK_INT;
        floatingActionButton.setOnClickListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getBoolean(MovieDetailsFragment.ARGUMENT_IS_MOVIE_DB)) {
                mIsOffline = true;
                floatingActionButton.setClickable(false);
                floatingActionButton.setImageResource(R.drawable.ic_check_white);
            }
            if (arguments.getBoolean(MovieDetailsFragment.ARGUMENT_IS_TWO_PANE)) {
                mIsTwoPane = true;
            }
            mMovieDetails = arguments.getParcelable(MovieDetailsFragment.ARGUMENT_MOVIE_DETAILS);
            populateView(mMovieDetails);
        }
        return rootView;
    }


    private void populateView(MovieDetails movieDetails) {
        overviewTextView.setText(movieDetails.getOverview());
        voteAvgTextView.setText(String.format(getString(R.string.vote_format), movieDetails.getVoteAverage()));
        voteCountTextView.setText(String.valueOf(movieDetails.getVoteCount()) + "" + getString(R.string.text_votes));
        runtimeTextView.setText(String.valueOf(movieDetails.getRuntime()));
        //Format the date into yyyy.MMM d format and split the year from it.
        String releaseDate;
        if (movieDetails.getReleaseDate().length() > 0) {
            releaseDate = movieDetails.getReleaseDate();
            String dateArray[] = Utils.formatIntoNewDate(releaseDate);
            String year = dateArray[0];
            String dayMonth = dateArray[1];
            yearTextView.setText(year);
            ddmmTextView.setText(dayMonth);
        } else {
            yearTextView.setText(getString(R.string.text_not_available));
            ddmmTextView.setText(getString(R.string.text_not_available));
        }
        if (movieDetails.getSpokenLanguages() != null) {
            List<SpokenLanguage> spokenLanguages = movieDetails.getSpokenLanguages();
            if (spokenLanguages.size() > 0) {
                StringBuilder languages = new StringBuilder();
                mLangCodes = new StringBuilder();
                String delimiter = "";
                for (SpokenLanguage spokenLanguage : spokenLanguages) {
                    String langCode = spokenLanguage.getLang_code();
                    Locale locale = new Locale(langCode);
                    String language = locale.getDisplayLanguage();
                    languages.append(delimiter).append(language);
                    mLangCodes.append(delimiter).append(langCode);
                    delimiter = ", ";
                }
                languageTextView.setText(languages);
            }
        }
        if (movieDetails.getReleases() != null) {
            Releases releaseCountries = movieDetails.getReleases();
            List<Country> certificationCountries = releaseCountries.getCountries();
            if (certificationCountries.size() > 0) {
                for (Country country : certificationCountries) {
                    if (country.getLanguageCode() != null &&
                            country.getLanguageCode().equalsIgnoreCase("US")) {
                        if (country.getCertification() != null || !country.getCertification().equalsIgnoreCase("")) {
                            mCertification = country.getCertification();
                        }
                        break;
                    }
                }
                if (mCertification != null && !mCertification.equals("")) {
                    certificateTextView.setText(mCertification);
                } else {
                    certificateTextView.setVisibility(View.GONE);
                    certTitleTextView.setVisibility(View.GONE);
                }
            }
        }
        if (movieDetails.getCredits() != null) {
            populateMovieCredits(movieDetails.getCredits());
        }
        /**Creating horizontal linear layouts with a textview and imageview and adding it to genre_layout
         based on the genre types of a given movie */
        if (movieDetails.getGenreList() != null) {
            createGenreLayout(movieDetails.getGenreList());
        }
        loadPosterAndBackdropImage(movieDetails);
    }

    private void createGenreLayout(List<Genre> genreList) {
        genres = new StringBuilder();
        String delimiter = "";
        if (genreList.size() > 0) {
            for (Genre genre : genreList) {
                LinearLayout childGenreLayout = new LinearLayout(getContext());
                //childGenreLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams childGenreLayoutParam = new LinearLayout
                        .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                childGenreLayoutParam.rightMargin = getResources().getInteger(R.integer.layout_right_margin);
                childGenreLayoutParam.bottomMargin = getResources().getInteger(R.integer.layout_right_margin);
                childGenreLayout.setLayoutParams(childGenreLayoutParam);
                ImageView genreImageView = new ImageView(getContext());
                TableRow.LayoutParams imagelayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                imagelayoutParams.rightMargin = getResources().getInteger(R.integer.layout_right_margin);
                imagelayoutParams.gravity = Gravity.CENTER;
                genreImageView.setLayoutParams(imagelayoutParams);
                //Get the image drawable based on the genre name
                Genres genreType = Genres.getFromName(genre.getName());
                genres.append(delimiter).append(genre.getName());
                delimiter = ",";
                switch (genreType) {
                    case ACTION:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_action));
                        break;
                    case ADVENTURE:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_adventure));
                        break;
                    case ANIMATION:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_animation));
                        break;
                    case COMEDY:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_comedy));
                        break;
                    case CRIME:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_crime));
                        break;
                    case DOCUMENTARY:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_documentary));
                        break;
                    case DRAMA:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_drama));
                        break;
                    case FAMILY:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_family));
                        break;
                    case FANTASY:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_fantasy));
                        break;
                    case FOREIGN:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_foreign));
                        break;
                    case HISTORY:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_history));
                        break;
                    case HORROR:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_horror));
                        break;
                    case MUSICAL:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_music));
                        break;
                    case MYSTERY:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_mystery));
                        break;
                    case ROMANCE:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_romance));
                        break;
                    case SCI_FI:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_scifi));
                        break;
                    case THRILLER:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_thriller));
                        break;
                    case TV_MOVIE:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_tv));
                        break;
                    case WAR:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_war));
                        break;
                    case WESTERN:
                        genreImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_western));
                        break;
                }
                TextView genreView = new TextView(getContext());
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                genreView.setLayoutParams(layoutParams);
                if (mCurrentApiVersion >= android.os.Build.VERSION_CODES.M) {
                    if (mIsTwoPane) {
                        genreView.setTextAppearance(android.R.style.TextAppearance_Medium);
                    }
                } else {
                    if (mIsTwoPane) {
                        genreView.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
                    }
                }
                genreView.setTypeface(Typeface.create(getString(R.string.sans_serif_light), Typeface.NORMAL));
                genreView.setTextColor(getResources().getColor(R.color.grey_900));
                genreView.setText(genre.getName());
                childGenreLayout.addView(genreImageView);
                childGenreLayout.addView(genreView);
                genreLayout.addView(childGenreLayout);
            }
        }
    }

    private void populateMovieCredits(Credits credits) {
        if (credits.getCast() != null) {

            List<Cast> castList = credits.getCast();
            if (castList.size() > 0) {
                int maxNum = castList.size();
                if (maxNum > 5) {
                    maxNum = 5;
                }
                mCastNames = new StringBuilder();
                mCastPaths = new StringBuilder();
                mProfileURIs = new LinkedList<>();
                String delimiter = "";
                for (int i = 0; i < maxNum; i++) {
                    Cast cast = castList.get(i);
                    LinearLayout childCastLayout = new LinearLayout(getContext());
                    childCastLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams childCastLayoutParam = new LinearLayout
                            .LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                    childCastLayoutParam.gravity = Gravity.CENTER;
                    childCastLayoutParam.rightMargin = 10;
                    childCastLayoutParam.leftMargin = 10;
                    childCastLayout.setLayoutParams(childCastLayoutParam);
                    final ImageView castImageView = new ImageView(getContext());
                    TableRow.LayoutParams imagelayoutParams = new TableRow.LayoutParams(150, 250);
                    imagelayoutParams.bottomMargin = getResources().getInteger(R.integer.layout_right_margin);
                    imagelayoutParams.gravity = Gravity.CENTER;
                    castImageView.setLayoutParams(imagelayoutParams);
                    if (!mIsOffline) {
                        if (cast.getProfilePath() != null) {
                            Uri profileImageURI = Utils.getImageURI(getString(R.string.url_base_image),
                                    getString(R.string.profile_image_size), cast.getProfilePath());
                            mProfileURIs.add(profileImageURI);
                            Picasso.with(getContext()).load(profileImageURI).
                                    error(R.drawable.image_cast_error).into(castImageView);
                            mCastPaths.append(delimiter).append(cast.getProfilePath().replaceAll("\\/", ""));
                        } else {
                            Picasso.with(getContext()).load(R.drawable.image_cast_error).into(castImageView);
                            mCastPaths.append(delimiter).append("");
                        }
                    } else {
                        if (cast.getProfilePath() != null) {
                            File castImage = new File(getContext().getFilesDir(), Utils.
                                    removeExtension(cast.getProfilePath()) + ".webp");
                            if (castImage != null) {
                                Picasso.with(getContext()).load(castImage).into(castImageView);
                            }
                        } else {
                            Picasso.with(getContext()).load(R.drawable.image_cast_error).into(castImageView);
                        }
                    }
                    childCastLayout.addView(castImageView);
                    if (cast.getName() != null) {
                        TextView castNameView = new TextView(getContext());
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER;
                        castNameView.setLines(2);
                        castNameView.setLayoutParams(layoutParams);
                        castNameView.setGravity(Gravity.CENTER);
                        if (mCurrentApiVersion >= android.os.Build.VERSION_CODES.M) {
                            castNameView.setBreakStrategy(Layout.BREAK_STRATEGY_HIGH_QUALITY);
                            if (mIsTwoPane) {
                                castNameView.setTextAppearance(android.R.style.TextAppearance_Medium);
                            }
                        } else {
                            if (mIsTwoPane) {
                                castNameView.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
                            }
                        }
                        castNameView.setTypeface(Typeface.create(getString(R.string.sans_serif_light), Typeface.NORMAL));
                        castNameView.setTextColor(getResources().getColor(R.color.grey_900));
                        castNameView.setText(cast.getName());
                        mCastNames.append(delimiter).append(cast.getName());
                        delimiter = ", ";
                        childCastLayout.addView(castNameView);
                    }
                    castLayout.addView(childCastLayout);
                }
            }
        }
        if (credits.getCrew() != null) {
            List<Crew> crewList = credits.getCrew();
            if (crewList.size() > 0) {
                for (Crew crewMember : crewList) {
                    if (crewMember.getJob().equals("Director")) {
                        directorName = crewMember.getName();
                    }
                    if (crewMember.getDepartment() != null && crewMember.getDepartment().equals("Writing")
                            && crewMember.getJob().equals("Screenplay")) {
                        mScreenPlay = crewMember.getName();
                    }
                    if (crewMember.getDepartment() != null && crewMember.getDepartment().equals("Sound")
                            && crewMember.getJob().equals("Original Music Composer")) {
                        mMusicComposer = crewMember.getName();
                    }
                }
            }
            if (directorName != null && !directorName.equals("")) {
                directorTextView.setText(directorName);
            } else {
                directorTitleTextView.setVisibility(View.GONE);
                directorTextView.setVisibility(View.GONE);
            }
            if (mMusicComposer != null && !mMusicComposer.equals("")) {
                composerTextView.setText(mMusicComposer);
            } else {
                composerTitleTextView.setVisibility(View.GONE);
                composerTextView.setVisibility(View.GONE);
            }
            if (mScreenPlay != null && !mScreenPlay.equals("")) {
                screenPlayTextView.setText(mScreenPlay);
            } else {
                screenPlayTitleTextView.setVisibility(View.GONE);
                screenPlayTextView.setVisibility(View.GONE);
            }
        }

    }

    private void loadPosterAndBackdropImage(MovieDetails movieDetails) {
        if (!mIsOffline) {
            mPosterImageUri = Utils.getImageURI(getContext().getString(R.string.url_base_image),
                    getContext().getString(R.string.poster_image_size), movieDetails.getPosterPath());
            mBackdropImageUri = Utils.getImageURI(getContext().getString(R.string.url_base_image),
                    getContext().getString(R.string.backdrop_image_size), movieDetails.getBackdropPath());
            // Load movie backdrop and generate a palette from it.
            Picasso.with(getContext())
                    .load(mBackdropImageUri)
                    .placeholder(R.drawable.image_backdrop_placeholder)
                    .error(R.drawable.image_backdrop_error)
                    .transform(PaletteTransformation.instance())
                    .into(backdropView, new PaletteTransformation.PaletteCallback(backdropView) {
                        @Override
                        public void onError() {
                            Log.e(LOG_TAG, "Palette Error");
                        }

                        @Override
                        public void onSuccess(Palette palette) {
                            applyPaletteColors(palette);
                        }
                    });
        } else {
            File backDropImage = new File(getContext().getFilesDir(), +movieDetails.getId() + "_backdrop.webp");
            Picasso.with(getContext())
                    .load(backDropImage)
                    .placeholder(R.drawable.image_backdrop_placeholder)
                    .error(R.drawable.image_backdrop_error)
                    .transform(PaletteTransformation.instance())
                    .into(backdropView, new PaletteTransformation.PaletteCallback(backdropView) {
                        @Override
                        public void onError() {
                            Log.e(LOG_TAG, "Palette Error");
                        }

                        @Override
                        public void onSuccess(Palette palette) {
                            applyPaletteColors(palette);
                        }
                    });
        }
    }

    private void applyPaletteColors(Palette palette) {
        ((Callback) getActivity()).onImageLoaded(palette);
        int grey900Color = getResources().getColor(R.color.grey_900);
        int grey50Color = getResources().getColor(R.color.grey_50);
        mLightVibrantColor = palette.getLightVibrantColor(grey50Color);
        mDarkVibrantColor = palette.getDarkVibrantColor(grey900Color);
        floatingActionButton.getDrawable().setColorFilter(mLightVibrantColor, PorterDuff.Mode.MULTIPLY);
        floatingActionButton.setBackgroundTintList(ColorStateList.
                valueOf(mDarkVibrantColor));
        voteAvgTextView.setTextColor(palette.getDarkMutedColor(grey900Color));
        voteCountTextView.setTextColor(palette.getMutedColor(grey900Color));
        runtimeTextView.setTextColor(palette.getDarkMutedColor(grey900Color));
        yearTextView.setTextColor(palette.getDarkMutedColor(grey900Color));
        ddmmTextView.setTextColor(palette.getMutedColor(grey900Color));
        minTitleTextView.setTextColor(palette.getMutedColor(grey900Color));
        overviewTitleTextView.setTextColor(palette.getDarkVibrantColor(grey900Color));
        certTitleTextView.setTextColor(palette.getDarkVibrantColor(grey900Color));
        directorTitleTextView.setTextColor(palette.getDarkVibrantColor(grey900Color));
        languageTitleTextView.setTextColor(palette.getDarkVibrantColor(grey900Color));
        genreTitleTextView.setTextColor(palette.getDarkVibrantColor(grey900Color));
        composerTitleTextView.setTextColor(palette.getDarkVibrantColor(grey900Color));
        screenPlayTitleTextView.setTextColor(palette.getDarkVibrantColor(grey900Color));
        languageTitleTextView.setTextColor(palette.getDarkVibrantColor(grey900Color));
        castTitleTextView.setTextColor(palette.getDarkVibrantColor(grey900Color));
    }

    /**
     * On FAB click, save Movie Details in DB
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            Picasso.with(getContext()).load(mBackdropImageUri).into(mBackdropTarget);
            Picasso.with(getContext()).load(mPosterImageUri).into(mPosterTarget);
            for (final Uri profileImageURI : mProfileURIs) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Using Picasso 'get' because all images dont load with 'into'
                            Bitmap bitmap = Picasso.with(getContext()).load(profileImageURI).get();
                            File file = new File(getContext().getFilesDir(),
                                    Utils.removeExtension(profileImageURI.getLastPathSegment()) + ".webp");
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, ostream);
                            ostream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            List<Video> videosList = mMovieDetails.getVideos().getResults();
            List<Review> reviewsList = mMovieDetails.getReviews().getResults();

            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID, mMovieDetails.getId());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_TITLE, mMovieDetails.getTitle());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_GENRES, genres.toString());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_RUNTIME, mMovieDetails.getRuntime());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_VOTE_AVG, mMovieDetails.getVoteAverage());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_VOTE_CNT, mMovieDetails.getVoteCount());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_OVERVIEW, mMovieDetails.getOverview());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_DIRECTOR, directorName);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_POPULARITY, mMovieDetails.getPopularity());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_RELEASE_DATE, mMovieDetails.getReleaseDate());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_CERTFICATION, mCertification);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_LANGUAGES, mLangCodes.toString());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_CASTNAMES, mCastNames.toString());
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_CASTPATHS, mCastPaths.toString());
            //URI of first trailer
            if (videosList.size() > 0 && videosList.get(0).getKey() != null) {
                movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_VIDEO_URI, Utils.getYoutubeURI(
                        videosList.get(0).getKey()).toString());
            } else {
                movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_VIDEO_URI, "");
            }
            getContext().getContentResolver().insert(MovieContract.MovieDetailsEntry.CONTENT_URI, movieValues);

            //Movie Reviews and Videos
            Vector<ContentValues> videoVector = new Vector<ContentValues>(videosList.size());
            for (Video video : videosList) {
                ContentValues videoValues = new ContentValues();
                videoValues.put(MovieContract.MovieVideosEntry.COLUMN_MOVIE_ID, mMovieDetails.getId());
                videoValues.put(MovieContract.MovieVideosEntry.COLUMN_VIDEO_LANG, video.getLanguageCode());
                mVideoKey = video.getKey();
                videoValues.put(MovieContract.MovieVideosEntry.COLUMN_VIDEO_KEY, mVideoKey);
                videoValues.put(MovieContract.MovieVideosEntry.COLUMN_VIDEO_NAME, video.getName());
                videoValues.put(MovieContract.MovieVideosEntry.COLUMN_VIDEO_SITE, video.getSite());
                videoValues.put(MovieContract.MovieVideosEntry.COLUMN_VIDEO_SIZE, video.getSize());
                videoValues.put(MovieContract.MovieVideosEntry.COLUMN_VIDEO_TYPE, video.getType());
                Picasso.with(getContext()).load(Utils.getYoutubeThumbnail(mVideoKey)).placeholder(R.drawable.image_thumbnail_error)
                        .error(R.drawable.image_thumbnail_error).into(mThumbnailTarget);
                videoVector.add(videoValues);
            }
            // add to database
            if (videoVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[videoVector.size()];
                videoVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(MovieContract.MovieVideosEntry.CONTENT_URI, cvArray);
            }

            Vector<ContentValues> reviewVector = new Vector<ContentValues>(reviewsList.size());
            for (Review review : reviewsList) {
                ContentValues reviewValues = new ContentValues();
                reviewValues.put(MovieContract.MovieReviewsEntry.COLUMN_MOVIE_ID, mMovieDetails.getId());
                reviewValues.put(MovieContract.MovieReviewsEntry.COLUMN_REVIEW_AUTHOR, review.getAuthor());
                reviewValues.put(MovieContract.MovieReviewsEntry.COLUMN_REVIEW_CONTENT, review.getContent());
                reviewValues.put(MovieContract.MovieReviewsEntry.COLUMN_REVIEW_URL, review.getUrl());
                reviewVector.add(reviewValues);
            }
            // add to database
            if (reviewVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[reviewVector.size()];
                reviewVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(MovieContract.MovieReviewsEntry.CONTENT_URI, cvArray);
            }


            Snackbar snackbar = Snackbar.make(nestedScrollView, getString(R.string.added_favorites),
                    Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.grey_900));
            snackbar.show();
            view.setClickable(false);
            floatingActionButton.setImageResource(R.drawable.ic_check_white);
            floatingActionButton.getDrawable().setColorFilter(mLightVibrantColor, PorterDuff.Mode.MULTIPLY);
            floatingActionButton.setBackgroundTintList(ColorStateList.
                    valueOf(mDarkVibrantColor));
            ((Callback) getActivity()).onFABClicked();
        }
    }

    public interface Callback {
        void onImageLoaded(Palette palette);

        void onFABClicked();
    }
}
