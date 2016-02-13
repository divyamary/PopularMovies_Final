package in.divyamary.moviereel.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyamary on 16-01-2016.
 */
public class Releases implements Parcelable {

    public static final Creator<Releases> CREATOR = new Creator<Releases>() {
        @Override
        public Releases createFromParcel(Parcel in) {
            return new Releases(in);
        }

        @Override
        public Releases[] newArray(int size) {
            return new Releases[size];
        }
    };
    private List<Country> countries = new ArrayList<>();

    public Releases() {

    }

    protected Releases(Parcel in) {
        countries = in.createTypedArrayList(Country.CREATOR);
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(countries);
    }
}
