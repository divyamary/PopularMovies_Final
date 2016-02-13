package in.divyamary.moviereel.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyamary on 15-01-2016.
 */
public class ReviewsPage implements Parcelable {

    public static final Creator<ReviewsPage> CREATOR = new Creator<ReviewsPage>() {
        @Override
        public ReviewsPage createFromParcel(Parcel in) {
            return new ReviewsPage(in);
        }

        @Override
        public ReviewsPage[] newArray(int size) {
            return new ReviewsPage[size];
        }
    };
    private int page;
    private List<Review> results = new ArrayList<>();
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    protected ReviewsPage(Parcel in) {
        page = in.readInt();
        totalPages = in.readInt();
        totalResults = in.readInt();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(page);
        parcel.writeInt(totalPages);
        parcel.writeInt(totalResults);
    }
}
