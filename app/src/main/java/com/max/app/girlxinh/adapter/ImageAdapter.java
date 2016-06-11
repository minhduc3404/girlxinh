package com.max.app.girlxinh.adapter;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.max.app.girlxinh.R;
import com.max.app.girlxinh.bus.BusProvider;
import com.max.app.girlxinh.bus.SettingEvent;
import com.max.app.girlxinh.module.Post;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forev on 3/27/2016.
 */
public class ImageAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ImageAdapter";
    List<Post> posts;
    Context mContext;
    int item_view_type1;
    int item_view_type2;
    int quality = QUALITY.HIGH;
    private boolean isSetWallPaper = false;

    static class TYPE {
        static int ONE = 1, TWO = 2;
    }

    static class QUALITY {
        static int HIGH = 0, MEDIUM = 1, LOW = 3;
    }

    public ImageAdapter(Context context, int item_view_type_1, int item_view_type_2, ArrayList data) {
        this.mContext = context;
        this.posts = data;
        this.item_view_type1 = item_view_type_1;
        this.item_view_type2 = item_view_type_2;
        BusProvider.getInstance().register(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE.ONE) {
            v = LayoutInflater.from(mContext).inflate(item_view_type1, parent, false);
        } else {
            v = LayoutInflater.from(mContext).inflate(item_view_type2, parent, false);
        }
        CardView cvHolder = (CardView) v.findViewById(R.id.cvHolder);

        return new PhotoHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PhotoHolder h = (PhotoHolder) holder;
        String url = posts.get(position).photos.get(0).altSizes.get(quality).url;
        h.setPhoto(url);
        h.getCardView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLongClickListener != null)
                    onLongClickListener.onLongClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (posts.get(position).photos.get(0).altSizes.get(quality).height < posts.get(position).photos.get(0).altSizes.get(quality).width)
            return TYPE.ONE;
        return TYPE.TWO;
    }

    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        return 0;
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;

        public PhotoHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        }

        public CardView getCardView() {
            return (CardView) itemView.findViewById(R.id.cvHolder);

        }

        public void setPhoto(String url) {
            Picasso.with(mContext).load(url).into(iv_image);
            if (isSetWallPaper)
                Picasso.with(mContext).load(url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        final WallpaperManager wm = WallpaperManager.getInstance(mContext);
                        new AsyncTask<Void, Bitmap, Bitmap>() {
                            @Override
                            protected Bitmap doInBackground(Void... params) {
                                try {
                                    wm.setBitmap(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Bitmap aVoid) {
                                super.onPostExecute(aVoid);
                            }
                        }.execute();
                        //wm.setBitmap(bitmap);
                        iv_image.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        }
    }

    @Subscribe
    public void settingAvailable(SettingEvent event) {
        // TODO: React to the event somehow!
        if (event.getType().equals(SettingEvent.QUALITY)) {
            Log.d(TAG, event.getValue());
            switch (event.getValue()) {
                case "0":
                    this.quality = QUALITY.HIGH;
                    break;

                case "1":
                    this.quality = QUALITY.MEDIUM;
                    break;

                case "2":
                    this.quality = QUALITY.LOW;
                    break;
            }
        }

        if (event.getType().equals(SettingEvent.WALLPAPER)) {
            boolean value = Boolean.valueOf(event.getValue());
            this.isSetWallPaper = value;
        }
    }


    OnLongClickListener onLongClickListener;

    public void setOnLongClickListener(OnLongClickListener l) {
        this.onLongClickListener = l;
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }
}
