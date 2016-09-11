package com.github.sasd97.upitter.ui.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.pointers.SubscribersPointerModel;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedPostRecycler;
import com.github.sasd97.upitter.ui.adapters.recyclers.SubscribersRecycler;
import com.github.sasd97.upitter.ui.base.BaseFragment;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;

/**
 * Created by alexander on 02.09.16.
 */

public class CompanyProfileSubscribersFragment extends BaseFragment {

    private UserModel userModel;
    private SubscribersPointerModel subscribers;

    @BindView(R.id.subscribers_recycler_view) RecyclerView subscribersRecyclerView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    public CompanyProfileSubscribersFragment() {
        super(R.layout.fragment_company_profile_subscribers);
    }

    public static CompanyProfileSubscribersFragment getFragment(@NonNull SubscribersPointerModel subscribers) {
        CompanyProfileSubscribersFragment fragment = new CompanyProfileSubscribersFragment();
        fragment.setSubscribers(subscribers);
        return fragment;
    }

    @Override
    protected void setupViews() {
        userModel = getHolder().get();

        SubscribersRecycler subscribersPostRecycler = new SubscribersRecycler(subscribers.getSubscribers());
        subscribersRecyclerView.setLayoutManager(linearLayoutManager);
        subscribersRecyclerView.setAdapter(subscribersPostRecycler);
    }

    public void setSubscribers(@NonNull SubscribersPointerModel subscribers) {
        this.subscribers = subscribers;
    }
}
