package com.max.app.girlxinh.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.max.app.girlxinh.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

/**
 * Created by Forev on 4/6/2016.
 */
public class CropActivity extends AppCompatActivity {
    public static String ARG_URI = "ARG_URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_activity);

    }
}
