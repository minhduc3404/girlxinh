package com.max.app.girlxinh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.max.app.girlxinh.R;
import com.max.app.girlxinh.module.Page;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Forev on 4/2/2016.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {

    List<Page> pages;
    Context mContext;
    int mLayout;

    public SubjectAdapter(Context context, int layout, List<Page> data) {
        this.mContext = context;
        this.mLayout = layout;
        this.pages = data;
    }

    @Override
    public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
        return new SubjectHolder(view);
    }

    @Override
    public void onBindViewHolder(SubjectHolder holder, int position) {
        holder.setDataView(pages.get(position).avatar, (String.valueOf(pages.get(position).cur_pos)));
    }

    @Override
    public int getItemCount() {
        if (pages != null)
            return pages.size();
        return 0;
    }

    public class SubjectHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;
        @Bind(R.id.tv_page)
        TextView tvPage;

        public SubjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDataView(String urlAvatar, String page) {
            Picasso.with(mContext).load(urlAvatar).into(ivAvatar);
            tvPage.setText(page);
        }

    }
}
