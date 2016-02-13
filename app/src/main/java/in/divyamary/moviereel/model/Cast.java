package in.divyamary.moviereel.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by divyamary on 15-01-2016.
 */
public class Cast implements Parcelable {

    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };
    @SerializedName("cast_id")
    private int castId;
    private String character;
    @SerializedName("credit_id")
    private String creditId;
    private int id;
    private String name;
    private int order;
    @SerializedName("profile_path")
    private String profilePath;

    public Cast() {

    }

    protected Cast(Parcel in) {
        castId = in.readInt();
        character = in.readString();
        creditId = in.readString();
        id = in.readInt();
        name = in.readString();
        order = in.readInt();
        profilePath = in.readString();
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(castId);
        parcel.writeString(character);
        parcel.writeString(creditId);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(order);
        parcel.writeString(profilePath);
    }
}
