package com.github.sasd97.upitter.ui.adapters.pagers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.fragments.BaseFeedFragment;

/**
 * Created by alexander on 15.09.16.
 */
public class PeopleFeedViewPager extends FragmentPagerAdapter {

    private String[] titles;

    public PeopleFeedViewPager(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BaseFeedFragment.getFragment();
            case 1:
                return BaseFeedFragment.getFragment();
            default:
                return BaseFeedFragment.getFragment();
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
