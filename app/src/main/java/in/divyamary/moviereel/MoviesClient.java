package in.divyamary.moviereel;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import in.divyamary.moviereel.api.ApiManager;
import in.divyamary.moviereel.api.ErrorBundle;
import in.divyamary.moviereel.bus.MovieBus;
import in.divyamary.moviereel.event.DiscoverMoviesResultEvent;
import in.divyamary.moviereel.event.ErrorResultEvent;
import in.divyamary.moviereel.event.GetMoviesDetailResultEvent;
import in.divyamary.moviereel.model.MovieDetails;
import in.divyamary.moviereel.model.MoviePage;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MoviesClient {

    final String LOG_TAG = MoviesClient.class.getSimpleName();
    private Context mContext;
    private ProgressDialog progressDialog;

    public MoviesClient(Context context) {
        this.mContext = context;
    }

    public void discoverMovies(int page, final boolean isRefresh, String sortType, String count) {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<MoviePage> call = ApiManager.getTmdbMovieApi().discoverMovies(sortType, count,
                BuildConfig.TMDB_API_KEY, String.valueOf(page));
        call.enqueue(new Callback<MoviePage>() {
            @Override
            public void onResponse(Response<MoviePage> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.v(LOG_TAG, "Retrofit success");
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    MoviePage moviePage = response.body();
                    MovieBus.getInstance().post(new DiscoverMoviesResultEvent(isRefresh, false, moviePage));
                } else {
                    int statusCode = response.code();
                    Log.e(LOG_TAG, "Movie Client status code:" + statusCode);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(LOG_TAG, "Failure in MovieClient" + throwable.getMessage());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                ErrorBundle errorBundle = ErrorBundle.adapt(throwable);
                MovieBus.getInstance().post(new ErrorResultEvent(errorBundle));
            }
        });
    }

    public void getMovieDetails(long movieId) {
        String appendResponse = "credits,releases,videos,reviews";
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<MovieDetails> call = ApiManager.getTmdbMovieApi().getMovieDetail(movieId,
                BuildConfig.TMDB_API_KEY, appendResponse);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Response<MovieDetails> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.v(LOG_TAG, "Retrofit success");
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    MovieDetails movieDetails = response.body();
                    MovieBus.getInstance().post(new GetMoviesDetailResultEvent(movieDetails));
                } else {
                    int statusCode = response.code();
                    Log.e(LOG_TAG, "Movie Client status code:" + statusCode);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                ErrorBundle errorBundle = ErrorBundle.adapt(throwable);
                MovieBus.getInstance().post(new ErrorResultEvent(errorBundle));
            }
        });
    }
}
