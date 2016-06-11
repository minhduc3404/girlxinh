
package com.max.app.girlxinh.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
;




public class Post implements Parcelable {
    @SerializedName("id")
    public long id;

    @SerializedName("photos")
    public List<Photo> photos = new ArrayList<Photo>();

    protected Post(Parcel in) {
        id = in.readLong();
        photos = in.createTypedArrayList(Photo.CREATOR);
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeTypedList(photos);
    }
}
