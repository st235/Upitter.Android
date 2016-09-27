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

import com.github.sasd97.upitter.BuildConfig;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.adapters.pagers.AuthorizationPager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.DialogUtils;
import com.github.sasd97.upitter.utils.Permissions;
import com.orhanobut.logger.Logger;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.BindView;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.TWITTER_SIGN_IN_REQUEST;
import static com.github.sasd97.upitter.constants.PermissionsConstants.REQUEST_COMPLEX;

public class AuthorizationActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener {

    private final int COLOR_AMOUNT = 3;
    private final int SCROLL_ENTRY_POINT = 0;

    private int COLOR_INDIGO;
    private int COLOR_BABY_BLUE;

    private final float[] fromColor = new float[COLOR_AMOUNT];
    private final float[] toColor = new float[COLOR_AMOUNT];
    private final float[] hsvColor = new float[COLOR_AMOUNT];

    @BindView(R.id.viewpager_login_activity) ViewPager viewPager;
    @BindView(R.id.tab_layout_login_activity) TabLayout tabLayout;
    @BindView(R.id.root_view_login_activity) View rootView;

    private AuthorizationPager authorizationPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        Permissions.getPermission(this,
                REQUEST_COMPLEX,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void setupViews() {
        initColors();

        authorizationPager = new AuthorizationPager(this, getSupportFragmentManager());

        viewPager.setAdapter(authorizationPager);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);

        if (BuildConfig.DEBUG) DialogUtils
                .showDebugInfo(this)
                .show();
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
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TWITTER_SIGN_IN_REQUEST)
            authorizationPager
                .getPeopleAuthorizationFragment()
                .onActivityResult(requestCode, resultCode, data);

        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                authorizationPager
                        .getPeopleAuthorizationFragment()
                        .connectWithVK(res.accessToken);
            }

            @Override
            public void onError(VKError error) {

            }
        });
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
