package com.github.sasd97.upitter.ui.adapters.pagers;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.ui.fragments.NavigationListFragment;
import com.github.sasd97.upitter.ui.fragments.NavigationPaginationPreviewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 18.08.16.
 */
public class SetupLocationPager extends FragmentPagerAdapter implements NavigationListFragment.OnChooseCoordinateListener {

    private static final int FIRST_POSITION = 0;

    private String[] titles;
    private ArrayList<CoordinatesModel> coordinates;

    private ViewPager pager;

    NavigationListFragment navigationListFragment;
    NavigationPaginationPreviewFragment navigationPaginationPreviewFragment;

    public SetupLocationPager(@NonNull FragmentManager fm,
                              @NonNull String[] titles,
                              @NonNull ArrayList<CoordinatesModel> coordinates,
                              @NonNull ViewPager pager) {
        super(fm);

        this.titles = titles;
        this.coordinates = coordinates;
        this.pager = pager;

        navigationPaginationPreviewFragment = NavigationPaginationPreviewFragment.getFragment(coordinates);
        navigationListFragment = NavigationListFragment.getFragment(coordinates, this);
        navigationListFragment.setPaginationHolder(navigationPaginationPreviewFragment);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return navigationPaginationPreviewFragment;
            case 1:
                return navigationListFragment;
            default:
                return navigationPaginationPreviewFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public void onChoose(int position) {
        pager.setCurrentItem(FIRST_POSITION);
        navigationPaginationPreviewFragment.moveToPosition(position);
    }
}
