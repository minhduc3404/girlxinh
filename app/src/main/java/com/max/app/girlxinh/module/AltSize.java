
package com.max.app.girlxinh.module;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AltSize implements Parcelable {

    @SerializedName("url")

    public String url;
    @SerializedName("width")

    public int width;
    @SerializedName("height")

    public int height;

    protected AltSize(Parcel in) {
        url = in.readString();
        width = in.readInt();
        height = in.readInt();
    }

    public static final Creator<AltSize> CREATOR = new Creator<AltSize>() {
        @Override
        public AltSize createFromParcel(Parcel in) {
            return new AltSize(in);
        }

        @Override
        public AltSize[] newArray(int size) {
            return new AltSize[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(width);
        dest.writeInt(height);
    }
}
