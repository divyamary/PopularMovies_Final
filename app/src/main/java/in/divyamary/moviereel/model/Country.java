package in.divyamary.moviereel.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by divyamary on 15-01-2016.
 */
public class Country implements Parcelable {

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
    private String certification;
    @SerializedName("iso_3166_1")
    private String languageCode;
    private String primary;
    @SerializedName("release_date")
    private String releaseDate;

    public Country() {

    }

    protected Country(Parcel in) {
        certification = in.readString();
        languageCode = in.readString();
        primary = in.readString();
        releaseDate = in.readString();
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(certification);
        parcel.writeString(languageCode);
        parcel.writeString(primary);
        parcel.writeString(releaseDate);
    }
}
