package com.github.sasd97.upitter.ui.results;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.adapters.pagers.SetupLocationPager;
import com.github.sasd97.upitter.ui.base.BaseActivity;

import butterknife.BindArray;
import butterknife.BindView;

public class SetupLocationResult extends BaseActivity {


    @BindArray(R.array.setup_location_pager) String[] titles;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_location);
        setToolbar(R.id.toolbar, true);
    }

    @Override
    protected void setupViews() {
        viewPager.setAdapter(new SetupLocationPager(getSupportFragmentManager(), titles));
        tabLayout.setupWithViewPager(viewPager);
    }
}
