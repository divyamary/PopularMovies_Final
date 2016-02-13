package in.divyamary.moviereel.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by divyamary on 15-01-2016.
 */
public class Credits implements Parcelable {

    public static final Creator<Credits> CREATOR = new Creator<Credits>() {
        @Override
        public Credits createFromParcel(Parcel in) {
            return new Credits(in);
        }

        @Override
        public Credits[] newArray(int size) {
            return new Credits[size];
        }
    };
    private List<Cast> cast = new LinkedList<>();
    private List<Crew> crew = new ArrayList<>();


    public Credits() {

    }

    protected Credits(Parcel in) {
        cast = in.createTypedArrayList(Cast.CREATOR);
        crew = in.createTypedArrayList(Crew.CREATOR);
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(LinkedList<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(cast);
        parcel.writeTypedList(crew);
    }
}
