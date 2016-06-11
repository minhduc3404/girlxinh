
package com.max.app.girlxinh.module;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
;




public class Response {

    @SerializedName("posts")

    public List<Post> posts = new ArrayList<Post>();

}
