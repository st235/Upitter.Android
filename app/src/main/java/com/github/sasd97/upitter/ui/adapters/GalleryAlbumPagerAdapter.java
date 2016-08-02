package com.github.sasd97.upitter.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.ui.schemas.AlbumImagePlaceholderSchema;

import java.util.List;

/**
 * Created by Alexadner Dadukin on 26.06.2016.
 */
public class GalleryAlbumPagerAdapter extends FragmentPagerAdapter {

    private List<String> data;

    public GalleryAlbumPagerAdapter(FragmentManager fm, List<String> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return AlbumImagePlaceholderSchema.getFragment(position, data.get(position));
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