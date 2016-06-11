package com.max.app.girlxinh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.max.app.girlxinh.R;
import com.max.app.girlxinh.manager.SubjectManager;
import com.max.app.girlxinh.module.Page;
import com.max.app.girlxinh.service.ServiceGenerator;
import com.max.app.girlxinh.service.Tumblr;
import com.max.app.girlxinh.util.ReadWriteJsonFileUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Forev on 4/2/2016.
 */
public class LoadingActivity extends AppCompatActivity {
    private static final String TAG = "LoadingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tumblr servicePhoto = ServiceGenerator.createService(Tumblr.class, Tumblr.base_url);
        servicePhoto.getAllPages(getString(R.string.app_id)).enqueue(new Callback<List<Page>>() {
            boolean isLoaded = false;

            @Override
            public void onResponse(Call<List<Page>> call, Response<List<Page>> response) {
                if (response.body() != null) {
                    SubjectManager.getInstance().setPages(response.body());
                    isLoaded = true;
                    ReadWriteJsonFileUtils.writeFileJson("pagesCache", Page.arrayToJson(response.body()));
                }
                Log.d(TAG, ReadWriteJsonFileUtils.readFileJson("pagesCache"));
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                LoadingActivity.this.finish();
            }

            @Override
            public void onFailure(Call<List<Page>> call, Throwable t) {
                isLoaded = false;
            }
        });
    }
}
