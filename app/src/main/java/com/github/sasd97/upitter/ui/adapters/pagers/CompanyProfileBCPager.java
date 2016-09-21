package com.github.sasd97.upitter.ui.adapters.pagers;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.SubscribersPointerModel;
import com.github.sasd97.upitter.ui.fragments.CompanyProfileFeedFragment;
import com.github.sasd97.upitter.ui.fragments.CompanyProfileSubscribersFragment;
import com.orhanobut.logger.Logger;

/**
 * Created by alexander on 01.09.16.
 */

public class CompanyProfileBCPager extends FragmentPagerAdapter {

    private final int TABS_AMOUNT = 2;

    private String[] titles;

    private PostsContainerModel posts;
    private SubscribersPointerModel subscribers;

    public CompanyProfileBCPager(@NonNull FragmentManager fragmentManager,
                                 @NonNull String[] titles,
                                 @NonNull PostsContainerModel posts,
                                 @NonNull SubscribersPointerModel subscribers) {
        super(fragmentManager);
        this.titles = titles;
        this.posts = posts;
        this.subscribers = subscribers;
    }

    @Override
    public Fragment getItem(int position) {
         switch (position) {
            case 0:
                return CompanyProfileFeedFragment.getFragment(posts);
            case 1:
                return CompanyProfileSubscribersFragment.getFragment(subscribers);
            default:
                return CompanyProfileFeedFragment.getFragment(posts);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return titles[position].concat(String.valueOf(posts.getCount()));
            case 1:
                return titles[position].concat(String.valueOf(subscribers.getAmount()));
            default:
                return titles[0].concat(String.valueOf(posts.getCount()));
        }
    }

    @Override
    public int getCount() {
        return TABS_AMOUNT;
    }
}
