package com.github.sasd97.upitter.ui.adapters.pagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.constants.GalleryConstants;
import com.github.sasd97.upitter.ui.schemas.AlbumImagePlaceholderSchema;

import java.util.List;

/**
 * Created by Alexadner Dadukin on 26.06.2016.
 */
public class GalleryAlbumPager extends FragmentPagerAdapter {

    private List<String> data;
    private int mode;

    public GalleryAlbumPager(FragmentManager fm, List<String> data, int mode) {
        super(fm);
        this.data = data;
        this.mode = mode;
    }

    @Override
    public Fragment getItem(int position) {
        return AlbumImagePlaceholderSchema.getFragment(position, data.get(position), mode);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Hello";
    }
}