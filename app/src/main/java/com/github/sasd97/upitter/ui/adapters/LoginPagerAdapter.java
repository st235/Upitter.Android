package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.services.query.AuthorizationQueryService;
import com.github.sasd97.upitter.ui.fragments.CompanyLoginFragment;
import com.github.sasd97.upitter.ui.fragments.UserLoginFragment;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */

public class LoginPagerAdapter extends FragmentPagerAdapter {

    private final int PAGES_AMOUNT = 2;

    private Context context;
    private AuthorizationQueryService service;

    public LoginPagerAdapter(Context context, AuthorizationQueryService service, FragmentManager fragmentManager) {
        super(fragmentManager);

        this.context = context;
        this.service = service;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UserLoginFragment.getFragment(service);
            case 1:
                return CompanyLoginFragment.getFragment();
            default:
                return UserLoginFragment.getFragment(service);
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
}
