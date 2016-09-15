package com.github.sasd97.upitter.ui.fragments;

import android.support.v4.view.ViewPager;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.adapters.pagers.PeopleFeedViewPager;
import com.github.sasd97.upitter.ui.base.BaseFragment;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * Created by alexander on 15.09.16.
 */
public class PeopleFeedFragment extends BaseFragment {

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
        viewPager.setAdapter(new PeopleFeedViewPager(getFragmentManager(), titles));
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}
