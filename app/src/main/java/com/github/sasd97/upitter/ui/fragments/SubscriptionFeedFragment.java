package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.behaviors.OnEndlessRecyclerViewScrollListener;
import com.github.sasd97.upitter.holders.LocationHolder;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostsPointerModel;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.services.query.RefreshQueryService;
import com.github.sasd97.upitter.services.query.SubscriptionPostQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedPostRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CategoriesSelectionResult;
import com.github.sasd97.upitter.ui.results.LocationSelectionResult;
import com.github.sasd97.upitter.utils.Palette;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.CATEGORIES_ATTACH;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.LOCATION_CHANGE_REQUEST;

/**
 * Created by alexander on 08.07.16.
 */

public class SubscriptionFeedFragment extends BaseFragment
        implements SubscriptionPostQueryService.OnSubscriptionPostListener,
        RefreshQueryService.OnRefreshListener,
        LocationService.OnLocationListener,
        SwipeRefreshLayout.OnRefreshListener {

    private UserModel userModel;

    @BindView(R.id.swipe_layout_tape) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_tape_fragment) RecyclerView tapeRecyclerView;

    private SubscriptionPostQueryService postQueryService;
    private RefreshQueryService refreshQueryService;
    private FeedPostRecycler feedPostRecycler;
    private ArrayList<Integer> categoriesSelected;
    private LinearLayoutManager linearLayoutManager;

    private Call<PostsContainerModel> requestPosts;

    public SubscriptionFeedFragment() {
        super(R.layout.fragment_base_feed);
    }

    public static SubscriptionFeedFragment getFragment() {
        return new SubscriptionFeedFragment();
    }

    @Override
    protected void setupViews() {
        setHasOptionsMenu(true);
        userModel = getHolder().get();
        categoriesSelected = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getContext());
        feedPostRecycler = new FeedPostRecycler(getContext(), getHolder().get(), false);
        tapeRecyclerView.setLayoutManager(linearLayoutManager);
        tapeRecyclerView.setAdapter(feedPostRecycler);

        postQueryService = SubscriptionPostQueryService.getService(this);
        refreshQueryService = RefreshQueryService.getService(this);
        LocationService locationService = LocationService.getService(this);
        locationService.init(getContext());

        tapeRecyclerView.addOnScrollListener(new OnEndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Logger.i("Infinity scroll detected");
                Logger.i(String.valueOf(feedPostRecycler.getItemCount()));
                if (feedPostRecycler.getItemCount() < 20) return;

                refreshQueryService.loadOld(
                        feedPostRecycler.getLastPostId(),
                        userModel.getAccessToken(),
                        LocationHolder.getRadius(),
                        LocationHolder.getLocation().getLatitude(),
                        LocationHolder.getLocation().getLongitude(),
                        categoriesSelected);
            }
        });

        swipeRefreshLayout.setColorSchemeColors(Palette.getSwipeRefreshPalette());
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onPostObtained(PostsPointerModel posts) {
        feedPostRecycler.addAll(posts.getPosts());
    }

    @Override
    public void onError(ErrorModel error) {
        Logger.e(error.toString());
        if (swipeRefreshLayout.isShown())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLocationFind(Location location) {
        postQueryService.obtainSubscriptionPosts(getHolder().get().getAccessToken());
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onRefresh() {
        refreshQueryService.loadNew(
                LocationHolder.getRadius(),
                userModel.getAccessToken(),
                LocationHolder.getLocation().getLatitude(),
                LocationHolder.getLocation().getLongitude(),
                feedPostRecycler.getFirstPostId(),
                categoriesSelected);
    }

    @Override
    public void onLoadNew(PostsPointerModel posts) {
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
    public void onEmpty() {
        Logger.i("Empty");

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
                Intent intent = new Intent(getContext(), CategoriesSelectionResult.class);
                startActivityForResult(intent, CATEGORIES_ACTIVITY_REQUEST);
                return true;
            case R.id.action_choose_location:
                startActivityForResult(new Intent(getContext(), LocationSelectionResult.class), LOCATION_CHANGE_REQUEST);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleCategoriesIntent(@NonNull Intent intent) {
        categoriesSelected = intent.getIntegerArrayListExtra(CATEGORIES_ATTACH);
        feedPostRecycler.refresh();
        postQueryService.obtainSubscriptionPosts(getHolder().get().getAccessToken());
    }

    private void handleLocation() {
        feedPostRecycler.refresh();
        postQueryService.obtainSubscriptionPosts(getHolder().get().getAccessToken());
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
