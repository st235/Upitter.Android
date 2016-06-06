package com.github.sasd97.upitter.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.ui.fragments.UserRegistrationFragment;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */

public class LoginPagerAdapter extends FragmentPagerAdapter {

    public LoginPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return new UserRegistrationFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }
}
