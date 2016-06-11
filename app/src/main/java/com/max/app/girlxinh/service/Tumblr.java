package com.max.app.girlxinh.service;

import com.max.app.girlxinh.module.Page;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Forev on 4/2/2016.
 */
public interface Tumblr {
    String base_url = "https://tumblr-api.herokuapp.com/";
    @GET("/pages")
    Call<List<Page>> getAllPages(@Query("app_id")String app_id);
}
