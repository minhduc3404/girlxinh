package com.max.app.girlxinh.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.max.app.girlxinh.R;
import com.max.app.girlxinh.adapter.ImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FavouriteFragment extends Fragment {
    @Bind(R.id.rc_photos)
    RecyclerView rcPhoto;

    public FavouriteFragment() {
    }

    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    List<File> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_fragment, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager grid = new GridLayoutManager(getContext(), 3);
        rcPhoto.setLayoutManager(grid);
        RecyclerView.Adapter adapter = new FavouriteAdapter(getContext(), R.layout.favourite_item_view, data);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
