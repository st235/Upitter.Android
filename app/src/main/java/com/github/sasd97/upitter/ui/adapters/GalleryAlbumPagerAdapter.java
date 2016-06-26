package com.github.sasd97.upitter.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.ui.fragments.GalleryAlbumPlaceholderFragment;

import java.util.ArrayList;

/**
 * Created by Alexadner Dadukin on 26.06.2016.
 */
public class GalleryAlbumPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> data;

    public GalleryAlbumPagerAdapter(FragmentManager fm, ArrayList<String> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return GalleryAlbumPlaceholderFragment.getFragment(position, data.get(position));
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