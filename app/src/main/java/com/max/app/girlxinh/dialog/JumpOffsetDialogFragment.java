package com.max.app.girlxinh.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.max.app.girlxinh.R;

import butterknife.ButterKnife;

/**
 * Created by Forev on 4/4/2016.
 */
public class JumpOffsetDialogFragment extends DialogFragment {
    public static JumpOffsetDialogFragment newInstance() {
        Bundle args = new Bundle();

        JumpOffsetDialogFragment fragment = new JumpOffsetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_offset_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
