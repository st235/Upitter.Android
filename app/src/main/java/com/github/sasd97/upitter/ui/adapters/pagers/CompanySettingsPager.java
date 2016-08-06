package com.github.sasd97.upitter.ui.adapters.pagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by alexander on 06.08.16.
 */
public class CompanySettingsPager extends FragmentPagerAdapter {

    private static final int SETTINGS_PAGES_COUNT = 2;

    public CompanySettingsPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
