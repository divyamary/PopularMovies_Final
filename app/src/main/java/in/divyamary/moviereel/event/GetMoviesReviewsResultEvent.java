package in.divyamary.moviereel.event;

import in.divyamary.moviereel.model.ReviewsPage;

/**
 * Created by divyamary on 17-01-2016.
 */
public class GetMoviesReviewsResultEvent {

    private ReviewsPage reviewsPage;

    public GetMoviesReviewsResultEvent(ReviewsPage reviewsPage) {
        this.reviewsPage = reviewsPage;
    }

    public ReviewsPage getReviewsPage() {
        return reviewsPage;
    }

    public void setReviewsPage(ReviewsPage reviewsPage) {
        this.reviewsPage = reviewsPage;
    }
}
