package com.max.app.girlxinh.dialog;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.max.app.girlxinh.R;
import com.max.app.girlxinh.databaseHelper.SQLHelper;
import com.max.app.girlxinh.module.Photo;
import com.max.app.girlxinh.module.Post;
import com.max.app.girlxinh.util.SaveFileStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Forev on 4/5/2016.
 */
public class UtilDialog extends DialogFragment {
    private static final String ARG_POST = "ARG_POST";
    private static final String TAG = "UtilDialog";

    Post post;
    @Bind(R.id.pbWaiting)
    ProgressBar pbWaiting;

    @Bind(R.id.ivPhoto)
    ImageView ivPhoto;
    private DialogInterface.OnDismissListener dissmisListener;

    public static UtilDialog newInstance(Post p) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST, p);
        UtilDialog fragment = new UtilDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.utils_dialog_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Post post = getArguments().getParcelable(ARG_POST);
        if (post != null) {
            this.post = post;
            Picasso.with(getContext()).load(post.photos.get(0).altSizes.get(0).url).into(ivPhoto);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().alpha = 0.8f;
    }

    @OnClick(R.id.cvShare)
    public void onShareClick(View v) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    100
            );
        }

        pbWaiting.setVisibility(View.VISIBLE);
        Picasso.with(getContext()).load(post.photos.get(0).altSizes.get(0).url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                SaveFileStorage.saveBitmap(bitmap, "/photo_beautiful_girl.png", new SaveFileStorage.SaveFileTask.OnCompleteListener() {
                    @Override
                    public void onCompleted(File file, boolean isSuccess) {
                        if (isSuccess) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                            shareIntent.setType("image/*");
                            startActivity(Intent.createChooser(shareIntent, "SHARE PHOTO"));
                        } else {

                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pbWaiting.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }


    @OnClick(R.id.cvWallpaper)
    public void onWallpaperClick(View v) {
        Picasso.with(getContext()).load(post.photos.get(0).altSizes.get(0).url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                /*SaveFileStorage.saveBitmap(bitmap, "/photo_beautiful_girl.png", new SaveFileStorage.SaveFileTask.OnCompleteListener() {
                    @Override
                    public void onCompleted(File file, boolean isSuccess) {
                        File fDes = new File(Environment.getExternalStorageDirectory(), "/photo_wallpaper.png");
                        UCrop uCrop = UCrop.of(Uri.fromFile(file), Uri.fromFile(fDes));
                        uCrop.start(getActivity());
                    }
                });*/
                WallpaperManager wm = WallpaperManager.getInstance(getActivity());
                try {
                    wm.setBitmap(bitmap);

                    Toast.makeText(getContext(), "SET WALLPAPER SUCCESS", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }


    @OnClick(R.id.cvDownload)
    public void onDownloadClick(View v) {
        pbWaiting.setVisibility(View.VISIBLE);
        final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        File girlxinh = new File(path + "/BeautifulGirls/");
        if (!girlxinh.exists())
            girlxinh.mkdirs();

        Picasso.with(getContext()).load(post.photos.get(0).altSizes.get(0).url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                final String nameFile = sdf.format(new Date());
                SaveFileStorage.saveBitmap(bitmap, nameFile, new SaveFileStorage.SaveFileTask.OnCompleteListener() {
                    @Override
                    public void onCompleted(File file, final boolean isSuccess) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                if (isSuccess) {
                                    Toast.makeText(getContext(), "SAVE FILE " + nameFile + " SUCCESS", Toast.LENGTH_LONG).show();
                                } else {

                                }
                                pbWaiting.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "CAN'T LOAD PHOTO TO SAVE", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @OnClick(R.id.cvFavourite)
    public void onFavouriteClick(View v) {

    }




    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dissmisListener != null)
            dissmisListener.onDismiss(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                WallpaperManager wm = WallpaperManager.getInstance(getActivity());
                try {
                    wm.setBitmap(BitmapFactory.decodeFile(String.valueOf(new File(resultUri.getPath()))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener l) {
        dissmisListener = l;
    }

}
