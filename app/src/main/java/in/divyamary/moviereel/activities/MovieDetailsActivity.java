package in.divyamary.moviereel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;

import in.divyamary.moviereel.R;
import in.divyamary.moviereel.fragments.MovieDetailsFragment;
import in.divyamary.moviereel.fragments.MovieInfoFragment;

public class MovieDetailsActivity extends AppCompatActivity implements MovieInfoFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent movieDetailIntent = getIntent();
        if (null != movieDetailIntent) {
            if (movieDetailIntent.hasExtra(MovieDetailsFragment.EXTRA_MOVIE_ID)) {
                Bundle arguments = new Bundle();
                arguments.putInt(MovieDetailsFragment.EXTRA_MOVIE_ID, getIntent().getIntExtra(MovieDetailsFragment.EXTRA_MOVIE_ID, 0));
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.movie_detail_container);
                if (fragment != null && fragment instanceof MovieDetailsFragment) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                            .commit();
                } else {
                    MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                    movieDetailsFragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.movie_detail_container, movieDetailsFragment, DETAILFRAGMENT_TAG)
                            .commit();
                }
            }
        }
    }

    @Override
    public void onImageLoaded(Palette palette) {
        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) getSupportFragmentManager().
                findFragmentByTag(DETAILFRAGMENT_TAG);
        if (movieDetailsFragment != null) {
            movieDetailsFragment.applyPalette(palette);
        }
    }

    @Override
    public void onFABClicked() {
        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) getSupportFragmentManager().
                findFragmentByTag(DETAILFRAGMENT_TAG);
        if (movieDetailsFragment != null) {
            movieDetailsFragment.onFABClicked();
        }
    }
}
