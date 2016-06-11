package com.max.app.girlxinh.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.max.app.girlxinh.R;
import com.max.app.girlxinh.adapter.ImageAdapter;
import com.max.app.girlxinh.bus.BusProvider;
import com.max.app.girlxinh.bus.HostChangedEvent;
import com.max.app.girlxinh.bus.SettingEvent;
import com.max.app.girlxinh.dialog.UtilDialog;
import com.max.app.girlxinh.manager.SubjectManager;
import com.max.app.girlxinh.module.PhotoResponse;
import com.max.app.girlxinh.module.Post;
import com.max.app.girlxinh.service.ServiceGenerator;
import com.max.app.girlxinh.service.XKCNService;
import com.max.app.girlxinh.util.EndlessRecyclerOnScrollListener;
import com.max.app.girlxinh.util.RecyclerItemClickListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SnowDark on 1/24/2016.
 */
public class HomeFragment extends Fragment {
    private int offset = 0;
    private int pos_current = 0;
    private static final String TAG = "HomeFragment";
    @Bind(R.id.rc_photos)
    RecyclerView rc_photos;
    SubjectManager subjectManager = SubjectManager.getInstance();
    XKCNService service;
    private ArrayList<Post> posts;
    private ImageAdapter adapter;
    @Bind(R.id.bottomBar)
    CardView bottomBar;
    @Bind(R.id.tv_page)
    TextView tvPage;
    @Bind(R.id.tg_play)
    ToggleButton tgPlay;
    EndlessRecyclerOnScrollListener endlessListener;
    LinearLayoutManager llMng;
    Handler hAutoSlide = new Handler();
    Runnable rAutoSlide = new Runnable() {
        @Override
        public void run() {
            if (!isPlay) {

            } else {
                if (posts.size() > 0) {
                    if (pos_current >= -1 && pos_current < offset + 20) {
                        rc_photos.scrollToPosition(pos_current + 1);
                    }
                }
            }

            hAutoSlide.postDelayed(rAutoSlide, time);
        }
    };
    private boolean isBarShow = true;
    private boolean isPlay = true;
    int time = 5000;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);
        onSetupView();
        setUpService();
        return view;
    }


    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        BusProvider.getInstance().register(this);
        hAutoSlide.removeCallbacks(rAutoSlide);
        hAutoSlide.postDelayed(rAutoSlide, time);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        BusProvider.getInstance().unregister(this);
        //hAutoSlide.removeCallbacks(rAutoSlide);
    }

    @Subscribe
    public void settingAvailable(SettingEvent event) {
        // TODO: React to the event somehow!
        if (event.getType().equals(SettingEvent.TIME)) {
            Log.d(TAG, event.getValue());
            time = Integer.valueOf(event.getValue()) * 1000;
            hAutoSlide.removeCallbacks(rAutoSlide);
            hAutoSlide.postDelayed(rAutoSlide, time);
        }
    }


    @Subscribe
    public void onLocationChanged(HostChangedEvent event) {
        offset = 0;
        pos_current = 0;
        posts.clear();
        adapter.notifyDataSetChanged();
        rc_photos.removeOnScrollListener(endlessListener);
        endlessListener = new EndlessRecyclerOnScrollListener(llMng) {
            @Override
            public void onLoadMore() {
                onLoadPhoto(offset += 20);
            }

            @Override
            public void onScrolled(int pos) {
                if (pos != pos_current)
                    pos_current = pos;
                if (pos_current < 0)
                    pos_current = 0;
                tvPage.setText(String.valueOf(pos_current));
            }
        };
        rc_photos.addOnScrollListener(endlessListener);
        onLoadPhoto(0);
    }

    private void onSetupView() {
        llMng = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return !isPlay;
            }
        };

        rc_photos.setLayoutManager(llMng);
        endlessListener = new EndlessRecyclerOnScrollListener(llMng) {
            @Override
            public void onLoadMore() {
                onLoadPhoto(offset += 20);
            }

            @Override
            public void onScrolled(int pos) {
                if (pos != pos_current)
                    pos_current = pos;
                if (pos_current < 0)
                    pos_current = 0;
                tvPage.setText(String.valueOf(pos_current));
            }
        };

        rc_photos.addOnScrollListener(endlessListener);
        rc_photos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isPlay && isBarShow) {
                    makeHideBottomBar();
                }
            }
        });

        rc_photos.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItem Position: " + position);
                showStateBar();
            }
        }));
    }

    private void makeShowBottomBar() {
        bottomBar.animate().translationY(0).setInterpolator(new AccelerateInterpolator(1)).start();
        isBarShow = true;
    }

    private void makeHideBottomBar() {
        bottomBar.animate().translationY(bottomBar.getHeight() + 2).setInterpolator(new DecelerateInterpolator(1)).start();
        isBarShow = false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showStateBar() {
        if (!isBarShow)
            makeShowBottomBar();
        else
            makeHideBottomBar();
    }

    private void setUpService() {
        service = ServiceGenerator.createService(XKCNService.class, XKCNService.base_url);
    }

    private void onLoadPhoto(int page) {
        service.getPhoto(
                subjectManager.getPages().get(subjectManager.cur_page).url,
                page).enqueue(new Callback<PhotoResponse>() {
            @Override
            public void onResponse(Call<PhotoResponse> call, Response<PhotoResponse> response) {
                posts.addAll(response.body().response.posts);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PhotoResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        posts = new ArrayList<>();
        adapter = new ImageAdapter(getContext(), R.layout.item_photo_view_type_1, R.layout.item_photo_view_type_2, posts);
        rc_photos.setAdapter(adapter);
        adapter.setOnLongClickListener(new ImageAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                hAutoSlide.removeCallbacks(rAutoSlide);
                UtilDialog dlg = new UtilDialog().newInstance(posts.get(position));
                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        hAutoSlide.postDelayed(rAutoSlide, time);
                    }
                });

                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment f = getChildFragmentManager().findFragmentByTag("dlg.utils");
                if (f != null)
                    ft.remove(f);
                dlg.show(ft, "dlg.utils");

            }
        });

        tgPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPlay = !isChecked;
                rc_photos.scrollToPosition(pos_current);
            }
        });

        onLoadPhoto(0);
    }


}
