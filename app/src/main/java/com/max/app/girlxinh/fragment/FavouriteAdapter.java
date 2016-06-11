package com.max.app.girlxinh.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.max.app.girlxinh.R;
import com.max.app.girlxinh.main.MainApp;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Forev on 4/7/2016.
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {
    private static final String TAG = "FavouriteAdapter";
    private final Context mContext;
    private final int mLayout;
    List<File> data;

    public FavouriteAdapter(Context context, int favourite_item_view, List<File> data) {
        this.data = data;
        this.mContext = context;
        this.mLayout = favourite_item_view;
    }

    @Override
    public FavouriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
        return new FavouriteHolder(v);
    }

    @Override
    public void onBindViewHolder(FavouriteHolder holder, int position) {
        holder.setDataView(data.get(position));
    }


    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;

        public FavouriteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDataView(final File imgFile) {
            new DecodeAsyncTask().execute(imgFile);
        }

        public class DecodeAsyncTask extends AsyncTask<File, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(File... params) {
                Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(params[0].getPath()), 300, 400, true);
                return bm;
            }

            @Override
            protected void onPostExecute(Bitmap bm) {
                super.onPostExecute(bm);
                ivAvatar.setImageBitmap(bm);
            }
        }

    }

}
