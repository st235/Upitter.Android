package com.github.sasd97.upitter.ui.adapters.pagers;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.SubscribersPointerModel;
import com.github.sasd97.upitter.ui.fragments.CompanyInformationFragment;
import com.github.sasd97.upitter.ui.fragments.CompanyProfileFeedFragment;
import com.github.sasd97.upitter.ui.fragments.CompanyProfileSubscribersFragment;

/**
 * Created by alexander on 01.09.16.
 */

public class CompanyProfileBPPager extends FragmentPagerAdapter {

    private final int TABS_AMOUNT = 2;

    private String[] titles;

    private PostsContainerModel posts;
    private CompanyPointerModel company;

    public CompanyProfileBPPager(@NonNull FragmentManager fragmentManager,
                                 @NonNull String[] titles,
                                 @NonNull PostsContainerModel posts,
                                 @NonNull CompanyPointerModel company) {
        super(fragmentManager);
        this.titles = titles;
        this.posts = posts;
        this.company = company;
    }

    @Override
    public Fragment getItem(int position) {
         switch (position) {
            case 0:
                return CompanyProfileFeedFragment.getFragment(posts);
            case 1:
                return CompanyInformationFragment.getFragment(company);
            default:
                return CompanyProfileFeedFragment.getFragment(posts);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return TABS_AMOUNT;
    }
}
