package in.divyamary.moviereel.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.divyamary.moviereel.R;
import in.divyamary.moviereel.fragments.MovieInfoFragment;
import in.divyamary.moviereel.fragments.MovieReviewsFragment;
import in.divyamary.moviereel.fragments.MovieVideosFragment;

/**
 * Created by divyamary on 16-01-2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Bundle mArguments;
    private Context mContext;

    public ViewPagerAdapter(Context context, FragmentManager manager, Bundle arguments) {
        super(manager);
        this.mContext = context;
        this.mArguments = arguments;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                MovieInfoFragment movieInfoFragment = new MovieInfoFragment();
                movieInfoFragment.setArguments(mArguments);
                return movieInfoFragment;
            }
            case 1: {
                MovieReviewsFragment movieReviewsFragment = new MovieReviewsFragment();
                movieReviewsFragment.setArguments(mArguments);
                return movieReviewsFragment;
            }
            case 2: {
                MovieVideosFragment movieVideosFragment = new MovieVideosFragment();
                movieVideosFragment.setArguments(mArguments);
                return movieVideosFragment;
            }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return mContext.getString(R.string.info_tab);
            }
            case 1: {
                return mContext.getString(R.string.reviews_tab);
            }
            case 2: {
                return mContext.getString(R.string.videos_tab);
            }
            default:
                return null;
        }
    }


}
