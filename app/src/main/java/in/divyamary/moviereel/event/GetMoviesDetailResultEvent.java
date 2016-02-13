package in.divyamary.moviereel.event;

import in.divyamary.moviereel.model.MovieDetails;

/**
 * Created by divyamary on 15-01-2016.
 */
public class GetMoviesDetailResultEvent {

    private MovieDetails movieDetails;

    public GetMoviesDetailResultEvent(MovieDetails movieDetails) {
        this.movieDetails = movieDetails;
    }

    public MovieDetails getMovieDetails() {
        return movieDetails;
    }

    public void setMovieDetails(MovieDetails movieDetails) {
        this.movieDetails = movieDetails;
    }
}
