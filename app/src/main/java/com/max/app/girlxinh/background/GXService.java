package com.max.app.girlxinh.background;

import android.app.ActionBar;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.max.app.girlxinh.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

/**
 * Created by Forev on 3/29/2016.
 */
public class GXService extends Service {
    public static final String ARG_IMG_URL = "ARG_IMG_URL";
    private static final String TAG = "GXService";

    boolean isCompleted = true;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void onSetPhotoBackground(String url) {
        Picasso.with(getBaseContext()).load(url).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "on Load completed");


                WallpaperManager myWallpaperManager
                        = WallpaperManager.getInstance(getApplicationContext());

                try {
                    myWallpaperManager.setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    isCompleted = true;
                }

            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d(TAG, "FAILED");
                isCompleted = true;
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d(TAG, "Prepare Load");
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url_img = intent.hasExtra(ARG_IMG_URL) ? intent.getStringExtra(ARG_IMG_URL) : null;
        if (url_img != null) onSetPhotoBackground(url_img);

        return START_STICKY;
    }
}
