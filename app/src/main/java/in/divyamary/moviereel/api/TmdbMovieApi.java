package in.divyamary.moviereel.api;


import in.divyamary.moviereel.model.MovieDetails;
import in.divyamary.moviereel.model.MoviePage;
import in.divyamary.moviereel.model.ReviewsPage;
import in.divyamary.moviereel.model.VideoResults;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by divyamary on 13-01-2016.
 */
public interface TmdbMovieApi {

    @GET("discover/movie")
    Call<MoviePage> discoverMovies(@Query("sort_by") String sortBy, @Query("vote_count.gte")
    String votecount, @Query("api_key") String apiKey, @Query("page") String page);

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetail(@Path("id") long movieID, @Query("api_key") String apiKey,
                                      @Query("append_to_response") String appendResponse);

    @GET("movie/{id}/reviews")
    Call<ReviewsPage> getMovieReviews(@Path("id") long movieID, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideoResults> getMovieVideos(@Path("id") long movieID, @Query("api_key") String apiKey);
}
