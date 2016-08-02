package com.github.sasd97.upitter.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.CompanyHolder;
import com.github.sasd97.upitter.holders.PeopleHolder;
import com.github.sasd97.upitter.holders.UserHolder;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.ui.adapters.LoginPagerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.Permissions;

import static com.github.sasd97.upitter.Upitter.*;
import static com.github.sasd97.upitter.holders.PeopleHolder.isUserAvailable;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.TWITTER_SIGN_IN_REQUEST;
import static com.github.sasd97.upitter.constants.PermissionsConstants.REQUEST_COMPLEX;

public class LoginActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener {

    private final int COLOR_AMOUNT = 3;
    private final int SCROLL_ENTRY_POINT = 0;

    private int COLOR_INDIGO;
    private int COLOR_BABY_BLUE;

    private final float[] fromColor = new float[COLOR_AMOUNT];
    private final float[] toColor = new float[COLOR_AMOUNT];
    private final float[] hsvColor = new float[COLOR_AMOUNT];

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View rootView;
    private LoginPagerAdapter loginPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if (isUserAvailable()) {
            initUser();
            return;
        }

        initColors();

        loginPagerAdapter = new LoginPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(loginPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);

        Permissions.getPermission(this,
                REQUEST_COMPLEX,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    protected void setupViews() {
        tabLayout = findById(R.id.tab_layout_login_activity);
        viewPager = findById(R.id.viewpager_login_activity);
        rootView = findById(R.id.root_view_login_activity);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == SCROLL_ENTRY_POINT) drawBackgroundByPosition(viewPager.getCurrentItem());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset == SCROLL_ENTRY_POINT) return;
        calculateHsvColor(positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                changeTabLayoutColor(tabLayout, COLOR_BABY_BLUE);
                break;
            case 1:
                changeTabLayoutColor(tabLayout, COLOR_INDIGO);
                break;
            default:
                changeTabLayoutColor(tabLayout, COLOR_BABY_BLUE);
                break;
        }
    }

    private void initUser() {
        Class<?> target;

        switch (UserModel.UserType.getTypeByValue(UserHolder.getUserType())) {
            case People:
                setHolder(PeopleHolder.getHolder());
                target = PeopleTapeActivity.class;
                break;
            case Company:
                setHolder(CompanyHolder.getHolder());
                target = CompanyFeedActivity.class;
                break;
            default:
                setHolder(PeopleHolder.getHolder());
                target = PeopleTapeActivity.class;
                break;
        }

        getHolder().restore();

        Intent intent = new Intent(this, target);
        startActivity(intent);
        finish();
    }

    private void drawBackgroundByPosition(int position) {
        switch (position) {
            case 0:
                rootView.setBackgroundColor(COLOR_INDIGO);
                break;
            case 1:
                rootView.setBackgroundColor(COLOR_BABY_BLUE);
                break;
            default:
                rootView.setBackgroundColor(COLOR_INDIGO);
                break;
        }
    }

    private void initColors() {
        COLOR_INDIGO = ContextCompat.getColor(this, R.color.colorStartBackground);
        COLOR_BABY_BLUE = ContextCompat.getColor(this, R.color.colorEndBackground);
        Color.colorToHSV(COLOR_INDIGO, fromColor);
        Color.colorToHSV(COLOR_BABY_BLUE, toColor);
    }

    private void calculateHsvColor(float percentage) {
        for (int i = 0; i < COLOR_AMOUNT; i++)
            hsvColor[i] = fromColor[i] + (toColor[i] - fromColor[i]) * percentage;
        rootView.setBackgroundColor(Color.HSVToColor(hsvColor));
    }

    private void changeTabLayoutColor(TabLayout tabLayout, int color) {
        tabLayout.setTabTextColors(color, color);
        tabLayout.setSelectedTabIndicatorColor(color);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TWITTER_SIGN_IN_REQUEST)
            loginPagerAdapter
                .getUserLoginFragment()
                .onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode != REQUEST_COMPLEX){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (Permissions.checkGrantState(permissions, grantResults)) {
            Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
}
}
