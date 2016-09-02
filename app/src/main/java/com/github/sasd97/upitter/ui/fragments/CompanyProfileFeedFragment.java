package com.github.sasd97.upitter.ui.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedPostRecycler;
import com.github.sasd97.upitter.ui.base.BaseFragment;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;

/**
 * Created by alexander on 02.09.16.
 */

public class CompanyProfileFeedFragment extends BaseFragment {

    private UserModel userModel;
    private PostsContainerModel posts;

    @BindView(R.id.feed_recycler_view) RecyclerView feedRecyclerView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private FeedPostRecycler feedPostRecycler;

    public CompanyProfileFeedFragment() {
        super(R.layout.fragment_company_profile_feed);
    }

    public static CompanyProfileFeedFragment getFragment(@NonNull PostsContainerModel posts) {
        CompanyProfileFeedFragment fragment = new CompanyProfileFeedFragment();
        fragment.setPosts(posts);
        return fragment;
    }

    @Override
    protected void setupViews() {
        userModel = getHolder().get();

        feedPostRecycler = new FeedPostRecycler(getContext(), userModel, false);
        feedRecyclerView.setLayoutManager(linearLayoutManager);
        feedRecyclerView.setAdapter(feedPostRecycler);

        feedPostRecycler.addAll(posts.getPosts());
    }

    public void setPosts(@NonNull PostsContainerModel posts) {
        this.posts = posts;
    }
}
