package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.fragments.CompanyAuthorizationFragment;
import com.github.sasd97.upitter.ui.fragments.UserLoginFragment;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */

public class LoginPagerAdapter extends FragmentPagerAdapter {

    private final int PAGES_AMOUNT = 2;

    private Context context;
    private UserLoginFragment userLoginFragment;
    private CompanyAuthorizationFragment companyAuthorizationFragment;

    public LoginPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);

        this.context = context;
        userLoginFragment = UserLoginFragment.getFragment();
        companyAuthorizationFragment = CompanyAuthorizationFragment.getFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return userLoginFragment;
            case 1:
                return companyAuthorizationFragment;
            default:
                return userLoginFragment;
        }
    }

    @Override
    public int getCount() {
        return PAGES_AMOUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.user_registration_tab_login_activity);
            case 1:
                return context.getString(R.string.company_registration_tab_login_activity);
            default:
                return context.getString(R.string.user_registration_tab_login_activity);
        }
    }

    public UserLoginFragment getUserLoginFragment() {
        return userLoginFragment;
    }
}
