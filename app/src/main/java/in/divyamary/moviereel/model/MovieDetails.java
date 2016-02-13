package in.divyamary.moviereel.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetails implements Parcelable {

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };
    private int id;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    private String title;
    private String overview;
    private double popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    private int runtime;
    private String status;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("genres")
    private List<Genre> genreList;
    private Credits credits;
    private Releases releases;
    private VideoResults videos;
    private ReviewsPage reviews;
    @SerializedName("spoken_languages")
    private List<SpokenLanguage> mSpokenLanguages;


    public MovieDetails() {

    }

    protected MovieDetails(Parcel in) {
        id = in.readInt();
        backdropPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        title = in.readString();
        overview = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        releaseDate = in.readString();
        runtime = in.readInt();
        status = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
        credits = in.readParcelable(Credits.class.getClassLoader());
        releases = in.readParcelable(Releases.class.getClassLoader());
        videos = in.readParcelable(VideoResults.class.getClassLoader());
        reviews = in.readParcelable(ReviewsPage.class.getClassLoader());
        mSpokenLanguages = in.readArrayList(SpokenLanguage.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public Credits getCredits() {
        return credits;
    }

    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    public Releases getReleases() {
        return releases;
    }

    public void setReleases(Releases releases) {
        this.releases = releases;
    }

    public VideoResults getVideos() {
        return videos;
    }

    public void setVideos(VideoResults videos) {
        this.videos = videos;
    }

    public ReviewsPage getReviews() {
        return reviews;
    }

    public void setReviews(ReviewsPage reviews) {
        this.reviews = reviews;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return mSpokenLanguages;
    }

    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        mSpokenLanguages = spokenLanguages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(backdropPath);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeDouble(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(releaseDate);
        parcel.writeInt(runtime);
        parcel.writeString(status);
        parcel.writeDouble(voteAverage);
        parcel.writeInt(voteCount);
        parcel.writeParcelable(credits, i);
        parcel.writeParcelable(releases, i);
        parcel.writeParcelable(videos, i);
        parcel.writeParcelable(reviews, i);
        parcel.writeList(mSpokenLanguages);
    }
}
