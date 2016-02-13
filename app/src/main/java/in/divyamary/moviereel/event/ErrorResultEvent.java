package in.divyamary.moviereel.event;

import in.divyamary.moviereel.api.ErrorBundle;

/**
 * Created by divyamary on 10-02-2016.
 */
public class ErrorResultEvent {


    private ErrorBundle mErrorBundle;


    public ErrorResultEvent(ErrorBundle errorBundle) {
        this.mErrorBundle = errorBundle;
    }

    public ErrorBundle getErrorBundle() {
        return mErrorBundle;
    }

    public void setErrorBundle(ErrorBundle errorBundle) {
        mErrorBundle = errorBundle;
    }


}
