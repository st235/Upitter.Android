package com.github.sasd97.upitter.ui.adapters.pagers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.github.sasd97.upitter.ui.fragments.FavoritesFragment;

/**
 * Created by alexander on 01.09.16.
 */

public class CompanyProfilePager extends FragmentPagerAdapter {

    private final int TABS_AMOUNT = 2;

    private String[] titles;

    public CompanyProfilePager(FragmentManager fragmentManager, String[] titles) {
        super(fragmentManager);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return FavoritesFragment.getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return TABS_AMOUNT;
    }
}
