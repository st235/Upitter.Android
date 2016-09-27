package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.holders.LocationHolder;
import com.github.sasd97.upitter.ui.adapters.pagers.PeopleFeedViewPager;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CategoriesSelectionResult;
import com.github.sasd97.upitter.ui.results.LocationSelectionResult;

import butterknife.BindArray;
import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.CATEGORIES_ATTACH;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.LOCATION_CHANGE_REQUEST;

/**
 * Created by alexander on 15.09.16.
 */
public class PeopleFeedFragment extends BaseFragment {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindArray(R.array.people_feed_titles) String[] titles;
    @BindView(R.id.view_pager) ViewPager viewPager;

    public PeopleFeedFragment() {
        super(R.layout.fragment_people_feed);
    }

    public static PeopleFeedFragment getFragment() {
        return new PeopleFeedFragment();
    }

    @Override
    protected void setupViews() {
        setHasOptionsMenu(true);
        viewPager.setAdapter(new PeopleFeedViewPager(getChildFragmentManager(), titles));
        tabLayout.setupWithViewPager(viewPager);
    }
}
