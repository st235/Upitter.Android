package com.github.sasd97.upitter.ui.results;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.ui.adapters.pagers.SetupLocationPager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.LOCATION_LIST;

public class SetupLocationResult extends BaseActivity {

    @BindArray(R.array.setup_location_pager) String[] titles;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private ArrayList<CoordinatesModel> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_location);
        setToolbar(R.id.toolbar, true);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) getToolbar().getLayoutParams();
        params.setScrollFlags(0);
    }

    @Override
    protected void setupViews() {
        locationList = getIntent().getParcelableArrayListExtra(LOCATION_LIST);
        viewPager.setAdapter(new SetupLocationPager(getSupportFragmentManager(), titles, locationList, viewPager));
        tabLayout.setupWithViewPager(viewPager);
    }
}
