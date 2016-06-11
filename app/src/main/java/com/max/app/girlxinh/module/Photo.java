
package com.max.app.girlxinh.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
;




public class Photo implements Parcelable {

    @SerializedName("caption")

    public String caption;
    @SerializedName("alt_sizes")

    public List<AltSize> altSizes = new ArrayList<AltSize>();
    @SerializedName("original_size")

    public OriginalSize originalSize;

    protected Photo(Parcel in) {
        caption = in.readString();
        altSizes = in.createTypedArrayList(AltSize.CREATOR);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeTypedList(altSizes);
    }
}
