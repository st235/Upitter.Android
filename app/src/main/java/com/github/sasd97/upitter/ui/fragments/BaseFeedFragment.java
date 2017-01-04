package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.behaviors.OnEndlessRecyclerViewScrollListener;
import com.github.sasd97.upitter.holders.LocationHolder;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostsPointerModel;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.services.query.RefreshQueryService;
import com.github.sasd97.upitter.ui.results.LocationSelectionResult;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedPostRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CategoriesSelectionResult;
import com.github.sasd97.upitter.utils.DialogUtils;
import com.github.sasd97.upitter.utils.Palette;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.*;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.CATEGORIES_ATTACH;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.LOCATION_CHANGE_REQUEST;

/**
 * Created by alexander on 08.07.16.
 */

public class BaseFeedFragment extends BaseFragment
        implements PostQueryService.OnPostListener,
        RefreshQueryService.OnRefreshListener,
        LocationService.OnLocationListener,
        SwipeRefreshLayout.OnRefreshListener,
        FeedPostRecycler.OnFeedNotAuthorizedClickListener {

    public static final String IS_HEADER_LINE = "FEED_HAS_HEADER_LINE";

    @BindView(R.id.empty_tape) View emptyTapeView;
    @BindView(R.id.swipe_layout_tape) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_tape_fragment) RecyclerView tapeRecyclerView;

    private String accessToken;
    private PostQueryService postQueryService;
    private RefreshQueryService refreshQueryService;
    private FeedPostRecycler feedPostRecycler;
    private ArrayList<Integer> categoriesSelected;
    private LinearLayoutManager linearLayoutManager;

    private boolean isHolder;
    private boolean isNoAccessListener = false;

    public BaseFeedFragment() {
        super(R.layout.fragment_base_feed);
    }

    public static BaseFeedFragment getFragment(boolean isHeaderLine) {
        BaseFeedFragment fragment = new BaseFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_HEADER_LINE, isHeaderLine);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setupViews() {
        setHasOptionsMenu(true);
        categoriesSelected = new ArrayList<>();

        isHolder = getHolder() != null;
        if (isHolder) accessToken = getHolder().get().getAccessToken();
        boolean isHeaderLine = getArguments().getBoolean(IS_HEADER_LINE);

        linearLayoutManager = new LinearLayoutManager(getContext());
        feedPostRecycler = new FeedPostRecycler(getContext(), isHolder ? getHolder().get() : null, isHeaderLine);
        if (isNoAccessListener)
            feedPostRecycler.setOnFeedNotAuthorizedClickListener(this);

        tapeRecyclerView.setLayoutManager(linearLayoutManager);
        tapeRecyclerView.setAdapter(feedPostRecycler);

        postQueryService = PostQueryService.getService(this);
        refreshQueryService = RefreshQueryService.getService(this);
        LocationService locationService = LocationService.getService(this);
        locationService.init(getContext());

        tapeRecyclerView.addOnScrollListener(new OnEndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Logger.i(String.valueOf(feedPostRecycler.getItemCount()));
                if (feedPostRecycler.getItemCount() < 20) return;


                if (isHolder) {
                    refreshQueryService.loadOld(
                            feedPostRecycler.getLastPostId(),
                            accessToken,
                            LocationHolder.getRadius(),
                            LocationHolder.getLocation().getLatitude(),
                            LocationHolder.getLocation().getLongitude(),
                            categoriesSelected);
                } else {
                    refreshQueryService.loadOldAnonymous(
                            feedPostRecycler.getLastPostId(),
                            LocationHolder.getRadius(),
                            LocationHolder.getLocation().getLatitude(),
                            LocationHolder.getLocation().getLongitude(),
                            categoriesSelected);
                }
            }
        });

        swipeRefreshLayout.setColorSchemeColors(Palette.getSwipeRefreshPalette());
        swipeRefreshLayout.setOnRefreshListener(this);

        Logger.e(String.valueOf(locationService.isLocated()));
        if (locationService.isLocated()) {
            load();
        }
    }

    public void addNoAccessListener() {
        if (feedPostRecycler != null)
            feedPostRecycler.setOnFeedNotAuthorizedClickListener(this);
        this.isNoAccessListener = true;
    }

    @Override
    public void onNotAuthorizedClick() {
        DialogUtils
                .showNoAccessDialog(getContext())
                .show();
    }

    @Override
    public void onPostObtained(PostsPointerModel posts) {
        if (posts.getPosts().size() > 0) switchTape(false);
        else switchTape(true);
        feedPostRecycler.addAll(posts.getPosts());
    }

    @Override
    public void onCreatePost() {}

    @Override
    public void onError(ErrorModel error) {
        Logger.e(error.toString());
        if (swipeRefreshLayout.isShown())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLocationFind(Location location) {
        if (feedPostRecycler.getItemCount() == 0) load();
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onRefresh() {
        if (isHolder) {
            refreshQueryService.loadNew(
                    LocationHolder.getRadius(),
                    accessToken,
                    LocationHolder.getLocation().getLatitude(),
                    LocationHolder.getLocation().getLongitude(),
                    feedPostRecycler.getFirstPostId(),
                    categoriesSelected);
        } else {
            refreshQueryService.loadNewAnonymous(
                    LocationHolder.getRadius(),
                    LocationHolder.getLocation().getLatitude(),
                    LocationHolder.getLocation().getLongitude(),
                    feedPostRecycler.getFirstPostId(),
                    categoriesSelected);
        }
    }

    @Override
    public void onLoadNew(PostsPointerModel posts) {
        if (posts.getPosts().size() > 0) switchTape(false);
        else switchTape(true);

        if (swipeRefreshLayout.isShown())
            swipeRefreshLayout.setRefreshing(false);

        linearLayoutManager.scrollToPosition(0);
        feedPostRecycler.addAhead(posts.getPosts());
    }

    @Override
    public void onLoadOld(PostsPointerModel posts) {
        feedPostRecycler.addBehind(posts.getPosts());
    }

    @Override
    public void onPostWatch(int amount) {}

    @Override
    public void onFindPost(PostPointerModel post) {}

    @Override
    public void onEmpty() {
        if (swipeRefreshLayout.isShown())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_choose_category:
                Intent chooseCategory = new Intent(getActivity(), CategoriesSelectionResult.class);
                if (categoriesSelected != null && categoriesSelected.size() != 0) chooseCategory.putIntegerArrayListExtra(CATEGORIES_ATTACH, categoriesSelected);
                startActivityForResult(chooseCategory, CATEGORIES_ACTIVITY_REQUEST);
                return true;
            case R.id.action_choose_location:
                startActivityForResult(new Intent(getContext(), LocationSelectionResult.class), LOCATION_CHANGE_REQUEST);
                return true;
            case R.id.action_clear_category:
                categoriesSelected.clear();
                feedPostRecycler.refresh();

                postQueryService.obtainPosts(
                        LocationHolder.getRadius(),
                        accessToken,
                        LocationHolder.getLocation().getLatitude(),
                        LocationHolder.getLocation().getLongitude(),
                        categoriesSelected);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleCategoriesIntent(@NonNull Intent intent) {
        categoriesSelected = intent.getIntegerArrayListExtra(CATEGORIES_ATTACH);
        feedPostRecycler.refresh();

        if (isHolder) {
            postQueryService.obtainPosts(
                    LocationHolder.getRadius(),
                    accessToken,
                    LocationHolder.getLocation().getLatitude(),
                    LocationHolder.getLocation().getLongitude(),
                    categoriesSelected);
        } else {
            postQueryService.obtainPostsAnonymous(
                    LocationHolder.getRadius(),
                    LocationHolder.getLocation().getLatitude(),
                    LocationHolder.getLocation().getLongitude(),
                    categoriesSelected);
        }
    }

    private void handleLocation() {
        feedPostRecycler.refresh();

        if (isHolder) {
            postQueryService.obtainPosts(
                    LocationHolder.getRadius(),
                    accessToken,
                    LocationHolder.getLocation().getLatitude(),
                    LocationHolder.getLocation().getLongitude(),
                    categoriesSelected);
        } else {
            postQueryService.obtainPostsAnonymous(
                    LocationHolder.getRadius(),
                    LocationHolder.getLocation().getLatitude(),
                    LocationHolder.getLocation().getLongitude(),
                    categoriesSelected);
        }
    }

    private void load() {
        feedPostRecycler.refresh();

        if (isHolder) {
            postQueryService.obtainPosts(
                    LocationHolder.getRadius(),
                    accessToken,
                    LocationHolder.getLocation().getLatitude(),
                    LocationHolder.getLocation().getLongitude(),
                    categoriesSelected);
        } else {
            postQueryService.obtainPostsAnonymous(
                    LocationHolder.getRadius(),
                    LocationHolder.getLocation().getLatitude(),
                    LocationHolder.getLocation().getLongitude(),
                    categoriesSelected);
        }
    }

    private void switchTape(boolean isHidden) {
        emptyTapeView.setVisibility(isHidden ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setVisibility(isHidden ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != BaseActivity.RESULT_OK) return;

        if (requestCode == CATEGORIES_ACTIVITY_REQUEST) {
            handleCategoriesIntent(data);
            return;
        }

        if (requestCode == LOCATION_CHANGE_REQUEST) {
            handleLocation();
            return;
        }
    }
}
