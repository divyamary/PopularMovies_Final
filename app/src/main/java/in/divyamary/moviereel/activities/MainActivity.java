package in.divyamary.moviereel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.divyamary.moviereel.R;
import in.divyamary.moviereel.fragments.MainFragment;
import in.divyamary.moviereel.fragments.MovieDetailsFragment;
import in.divyamary.moviereel.fragments.MovieInfoFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback, MovieInfoFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int movieId = getIntent() != null ? getIntent().getIntExtra(MovieDetailsFragment.EXTRA_MOVIE_ID, 0) : null;
        ButterKnife.bind(this);
        if (findViewById(R.id.fragment_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                if (movieId != 0) {
                    Bundle args = new Bundle();
                    args.putInt(MovieDetailsFragment.EXTRA_MOVIE_ID, movieId);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_detail_container,
                        movieDetailsFragment, DETAILFRAGMENT_TAG).commit();
            }
        } else {
            mTwoPane = false;
            toolbar.setLogo(R.mipmap.ic_launcher);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mTwoPane) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent settingsIntent = new Intent(this, AboutActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int movieId) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(MovieDetailsFragment.EXTRA_MOVIE_ID, movieId);
            arguments.putBoolean(MovieDetailsFragment.ARGUMENT_IS_TWO_PANE, true);
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            movieDetailsFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container, movieDetailsFragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent movieDetailIntent = new Intent(this, MovieDetailsActivity.class);
            movieDetailIntent.putExtra(MovieDetailsFragment.EXTRA_MOVIE_ID, movieId);
            this.startActivity(movieDetailIntent);
        }
    }

    @Override
    public void onImageLoaded(Palette palette) {
        if (mTwoPane) {
            int defaultColor = getResources().getColor(R.color.grey_900);
            toolbar.setBackgroundColor(palette.getDarkVibrantColor(defaultColor));
        }
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
