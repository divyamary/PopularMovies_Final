package in.divyamary.moviereel.event;

import in.divyamary.moviereel.model.MoviePage;

/**
 * Created by divyamary on 13-01-2016.
 */
public class DiscoverMoviesResultEvent {

    private boolean isRefresh;
    private boolean isLoading;
    private MoviePage moviePage;

    public DiscoverMoviesResultEvent(Boolean isRefresh, Boolean isFetchCompleted, MoviePage moviePage) {
        this.isRefresh = isRefresh;
        this.isLoading = isFetchCompleted;
        this.moviePage = moviePage;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        this.isRefresh = refresh;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }

    public MoviePage getMoviePage() {
        return moviePage;
    }

    public void setMoviePage(MoviePage moviePage) {
        this.moviePage = moviePage;
    }
}
