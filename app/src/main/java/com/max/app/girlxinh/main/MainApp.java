package com.max.app.girlxinh.main;

import android.app.Application;

import com.max.app.girlxinh.module.Page;
import com.max.app.girlxinh.module.Song;

/**
 * Created by Forev on 4/2/2016.
 */
public class MainApp extends Application {
    static MainApp instance;

    public static MainApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
