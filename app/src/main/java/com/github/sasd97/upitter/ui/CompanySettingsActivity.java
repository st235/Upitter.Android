package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.adapters.pagers.CompanySettingsPager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import butterknife.BindArray;
import butterknife.BindView;

public class CompanySettingsActivity extends BaseActivity {

    private final static String TAG = "Company Settings";

    @BindArray(R.array.settings_pager) String[] titles;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_settings);
        setToolbar(R.id.toolbar, true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT, 0.2f));
    }

    @Override
    protected void setupViews() {
        viewPager.setAdapter(new CompanySettingsPager(getSupportFragmentManager(), titles));
        tabLayout.setupWithViewPager(viewPager);
    }
}
