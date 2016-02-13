package in.divyamary.moviereel.event;

import in.divyamary.moviereel.model.VideoResults;

/**
 * Created by divyamary on 17-01-2016.
 */
public class GetMoviesVideosResultEvent {

    private VideoResults videoResults;

    public GetMoviesVideosResultEvent(VideoResults videoResults) {
        this.videoResults = videoResults;
    }

    public VideoResults getVideoResults() {
        return videoResults;
    }

    public void setVideoResults(VideoResults videoResults) {
        this.videoResults = videoResults;
    }
}
