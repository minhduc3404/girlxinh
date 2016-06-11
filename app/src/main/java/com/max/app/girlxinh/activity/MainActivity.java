package com.max.app.girlxinh.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.max.app.girlxinh.R;
import com.max.app.girlxinh.adapter.MainPagerAdapter;
import com.max.app.girlxinh.adapter.SubjectAdapter;
import com.max.app.girlxinh.bus.BusProvider;
import com.max.app.girlxinh.bus.HostChangedEvent;
import com.max.app.girlxinh.bus.SettingEvent;
import com.max.app.girlxinh.fragment.FavouriteFragment;
import com.max.app.girlxinh.fragment.HomeFragment;
import com.max.app.girlxinh.fragment.ProfileFragment;
import com.max.app.girlxinh.main.MainApp;
import com.max.app.girlxinh.manager.MusicManager;
import com.max.app.girlxinh.manager.SubjectManager;
import com.max.app.girlxinh.util.RecyclerItemClickListener;
import com.max.app.girlxinh.util.SlidingTabLayout;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.FileDescriptor;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MusicManager.MusicLoadCallback {
    private static final String TAG = "MainActivity";
    @Bind(R.id.mViewPager)
    ViewPager mViewPager;
    @Bind(R.id.mSlidingTab)
    SlidingTabLayout mSlidingTab;
    @Bind(R.id.mDrawer)
    DrawerLayout mDrawer;

    @Bind(R.id.rc_subject)
    RecyclerView rcSubject;

    private Fragment[] fragments;
    public boolean isSlidingShow = true;
    private MainPagerAdapter mainAdapter;

    MusicManager musicManager = MusicManager.getInstance(this);
    SubjectManager subjectManager = SubjectManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
       /* mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mSlidingTab = (SlidingTabLayout) findViewById(R.id.mSlidingTab);
        rcSubject = (RecyclerView) findViewById(R.id.rc_subject);*/
        setViewPager();
        makeScreenWakeUp();
    }

    private void setViewPager() {
        fragments = new Fragment[2];
        fragments[0] = new HomeFragment().newInstance();
        //fragments[1] = new FavouriteFragment().newInstance();
        fragments[1] = new ProfileFragment().newInstance();
        mainAdapter = new MainPagerAdapter(getSupportFragmentManager(), this, fragments);
        mViewPager.setAdapter(mainAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mSlidingTab.setCustomTabView(R.layout.custom_tab, 0);
        mSlidingTab.setViewPager(mViewPager);

        mSlidingTab.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return 0xFFFFFFFF;
            }
        });
        mSlidingTab.setVisibility(View.GONE);
        mSlidingTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != 0) {
                    if (!isSlidingShow) {
                        showSliding();
                    }
                } else if (isSlidingShow) {
                    hideSliding();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupLeftDrawer();
    }

    public void makeScreenWakeUp() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void showSliding() {
        if (isSlidingShow != true) {
            isSlidingShow = true;
            mSlidingTab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(1)).start();
            mSlidingTab.setVisibility(View.VISIBLE);
        }

    }

    public void hideSliding() {
        if (isSlidingShow != false) {
            isSlidingShow = false;
            mSlidingTab.animate().translationY(-mSlidingTab.getHeight()).setInterpolator(new AccelerateInterpolator(1)).start();
            mSlidingTab.setVisibility(View.GONE);
        }
    }

    public void setupLeftDrawer() {

        GridLayoutManager gridManager = new GridLayoutManager(this, 4);
        rcSubject.setLayoutManager(gridManager);
        rcSubject.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (subjectManager.cur_page != position) {
                    subjectManager.cur_page = position;
                    BusProvider.getInstance().post(new HostChangedEvent());
                }
                mDrawer.closeDrawers();
            }
        }));
        SubjectAdapter adapter = new SubjectAdapter(this, R.layout.subject_item_view, subjectManager.getPages());
        rcSubject.setAdapter(adapter);
    }

    @Subscribe
    public void settingSubscribe(SettingEvent event) {
        Toast.makeText(this, event.getType(), Toast.LENGTH_SHORT);
    }


    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onLoadMusicCompleted() {
        musicManager.playAuto();
    }

    @OnClick(R.id.rating)
    public void OnRatingClick(View v) {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    @OnClick(R.id.more_app)
    public void onMoreAppClick(View v) {

        Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=MTCDev");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=MTCDev")));
        }
    }

    @OnClick(R.id.share)
    public void onShareApp(View v) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "http://play.google.com/store/apps/details?id=" + this.getPackageName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share for Friends"));
    }
}
