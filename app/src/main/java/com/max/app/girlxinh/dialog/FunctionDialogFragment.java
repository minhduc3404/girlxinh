package com.max.app.girlxinh.dialog;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.max.app.girlxinh.R;
import com.max.app.girlxinh.module.Photo;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Forev on 4/4/2016.
 */
public class FunctionDialogFragment extends DialogFragment {
    public static String ARG_PHOTO = "ARG_PHOTO";
    @Bind(R.id.ivPhoto)
    ImageView ivPhoto;

    public static FunctionDialogFragment newInstance(Photo p) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO, p);
        FunctionDialogFragment fragment = new FunctionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_dialog_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getArguments();
        Photo p = b.getParcelable(ARG_PHOTO);
        Picasso.with(getContext()).load(p.altSizes.get(0).url).into(ivPhoto);
    }
}
