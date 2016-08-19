package com.github.sasd97.upitter.ui.adapters.pagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.ui.fragments.AppInfoFragment;
import com.github.sasd97.upitter.ui.fragments.NavigationPaginationPreviewFragment;

/**
 * Created by alexander on 18.08.16.
 */
public class SetupLocationPager extends FragmentPagerAdapter {

    private String[] titles;

    public SetupLocationPager(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NavigationPaginationPreviewFragment.getFragment();
            default:
                return NavigationPaginationPreviewFragment.getFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
