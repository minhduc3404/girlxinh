package com.max.app.girlxinh.service;

import com.max.app.girlxinh.module.PhotoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Forev on 3/27/2016.
 */
public interface XKCNService {
    String base_url = "http://api.tumblr.com/";
    String token = "y0ZHjyEQJkvxFYxL1EVp4ec0401it2nmGK708Mrs7OU8HknCJf";
    //String page_name = "xkcn.info";
    //String page_name = "spacetraveler555";
    String page_name = "girlxinhvietnamcom";

    //static final String page_name = page;
    //static final String page_name = "seoul-of-my-heart";
    @GET("v2/blog/{host}/posts/photo?api_key=" + token)
    Call<PhotoResponse> getPhoto(@Path("host") String host,
                                 @Query("offset") int page);
}
