package grungesoft.com.stopmotioncamera.Gallary;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by msc10 on 16/02/2017.
 */
public class AnimalItem implements Parcelable {

    public final static int LOCATION_URL = 1;
    public final static int LOCATION_LOCAL_STORE = 2;


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AnimalItem> CREATOR = new Parcelable.Creator<AnimalItem>() {
        @Override
        public AnimalItem createFromParcel(Parcel in) {
            return new AnimalItem(in);
        }

        @Override
        public AnimalItem[] newArray(int size) {
            return new AnimalItem[size];
        }
    };

    public String name;
    public String detail;
    public String imageLocation;
    public int locationType;
    public String path = null;

    public AnimalItem(String name, String detail, String imageLocation, int locationType) {
        this.name = name;
        this.detail = detail;
        this.locationType = locationType;
        this.imageLocation = imageLocation;
    }



    protected AnimalItem(Parcel in) {
        name = in.readString();
        detail = in.readString();
        imageLocation = in.readString();
        locationType = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(detail);
        dest.writeString(imageLocation);
        dest.writeInt(locationType);
    }
}
