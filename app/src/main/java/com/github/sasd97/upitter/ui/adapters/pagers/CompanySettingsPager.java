package com.github.sasd97.upitter.ui.adapters.pagers;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.ui.fragments.CompanyAdditionalSettingsFragment;
import com.github.sasd97.upitter.ui.fragments.CompanyBaseSettingsFragment;

import butterknife.BindArray;
import butterknife.BindString;

/**
 * Created by alexander on 06.08.16.
 */
public class CompanySettingsPager extends FragmentPagerAdapter {

    private static final int SETTINGS_PAGES_COUNT = 2;

    private String[] titles;
    private CompanyModel company;

    public CompanySettingsPager(@NonNull FragmentManager fm,
                                @NonNull String[] titles,
                                @NonNull CompanyModel company) {
        super(fm);
        this.titles = titles;
        this.company = company;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CompanyBaseSettingsFragment.getFragment(company);
            case 1:
                return CompanyAdditionalSettingsFragment.getFragment(company);
            default:
                return CompanyBaseSettingsFragment.getFragment(company);
        }
    }

    @Override
    public int getCount() {
        return SETTINGS_PAGES_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
