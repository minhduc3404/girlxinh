package com.max.app.girlxinh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Forev on 3/27/2016.
 */
public abstract class BaseFragment extends Fragment {
    int layout_id;

    protected abstract void setLayout();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout_id, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
