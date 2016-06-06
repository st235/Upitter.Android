package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.adapters.LoginPagerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private final String TAG = "LOGIN_ACTIVITY";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LoginPagerAdapter loginPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loginPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(loginPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void bindViews() {
        tabLayout = findById(R.id.tab_layout_login_activity);
        viewPager = findById(R.id.viewpager_login_activity);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, String.valueOf(positionOffset));
    }

    @Override
    public void onPageSelected(int position) {

    }
}
