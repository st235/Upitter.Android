package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.services.query.AuthorizationQueryService;
import com.github.sasd97.upitter.ui.adapters.LoginPagerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.GOOGLE_SIGN_IN_REQUEST;

public class LoginActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener,
        AuthorizationQueryService.OnAuthorizationListener {

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

    private AuthorizationQueryService queryService = AuthorizationQueryService.getService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initColors();

        loginPagerAdapter = new LoginPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(loginPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void bindViews() {
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

    public void drawBackgroundByPosition(int position) {
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
        COLOR_INDIGO = ContextCompat.getColor(this, R.color.colorPrimary);
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
    public void onServerNotify() {
        Log.d("NOTIFIED", "HOOORAY");
    }

    @Override
    public void onError(int code, String message) {
        Log.d("SERVER_EVENT", "OHHHHH" + code + ";" + message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_SIGN_IN_REQUEST && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String mFullName = acct.getDisplayName();
                String mEmail = acct.getEmail();
                queryService.notifyServerBySignIn(acct.getIdToken());
                Log.d("USER_INFO", result.isSuccess() + ";" + mFullName + ";" + mEmail + ";" + acct.getIdToken() + ";");
            } else {
                Toast.makeText(this, "result false", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "wrong", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
