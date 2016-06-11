package com.max.app.girlxinh.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.max.app.girlxinh.R;


/**
 * Created by SnowDark on 1/24/2016.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    Fragment[] fragments;

    int[] icons = {R.drawable.ic_tab_home,R.drawable.ic_tab_name};

    public MainPagerAdapter(FragmentManager fm, Context context, Fragment[] fragments) {
        super(fm);
        mContext = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments == null)
            return null;
        else
            return fragments[position];
    }

    @Override
    public int getCount() {
        if (fragments == null)
            return 0;
        else
            return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = mContext.getResources().getDrawable(icons[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
