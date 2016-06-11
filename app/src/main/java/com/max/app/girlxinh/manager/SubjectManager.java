package com.max.app.girlxinh.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.max.app.girlxinh.main.MainApp;
import com.max.app.girlxinh.module.Page;
import com.max.app.girlxinh.service.ServiceGenerator;
import com.max.app.girlxinh.service.Tumblr;
import com.max.app.girlxinh.util.ReadWriteJsonFileUtils;
import com.max.app.girlxinh.util.converts.StreamToJson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Forev on 4/6/2016.
 */
public class SubjectManager {
    static SubjectManager instance = null;
    private List<Page> pages;
    public int cur_page = 0;
    public boolean isLoaded;

    public static SubjectManager getInstance() {
        if (instance == null)
            instance = new SubjectManager();
        return instance;
    }


    public SubjectManager() {

    }

    public List<Page> getPages() {
        if (pages == null) {
            String json = ReadWriteJsonFileUtils.readFileJson("pagesCache");
            pages = Page.toJsonArray(json);
        }
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

}
