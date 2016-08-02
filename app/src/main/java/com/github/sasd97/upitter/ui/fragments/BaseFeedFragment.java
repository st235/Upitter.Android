package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.behaviors.OnEndlessRecyclerViewScrollListener;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.services.query.RefreshQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.FeedPostRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CategoriesSelectionResult;
import com.github.sasd97.upitter.utils.Palette;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.*;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.CATEGORIES_ATTACH;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST;

/**
 * Created by alexander on 08.07.16.
 */

public class BaseFeedFragment extends BaseFragment
        implements PostQueryService.OnPostListener,
        RefreshQueryService.OnRefreshListener,
        LocationService.OnLocationListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TAPE FRAGMENT";

    private UserModel userModel;
    private Location location;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.swipe_layout_tape) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_tape_fragment) RecyclerView tapeRecyclerView;

    private PostQueryService postQueryService;
    private RefreshQueryService refreshQueryService;
    private FeedPostRecycler feedPostRecycler;
    private ArrayList<Integer> categoriesSelected;

    public BaseFeedFragment() {
        super(R.layout.fragment_base_feed);
    }

    public static BaseFeedFragment getFragment() {
        return new BaseFeedFragment();
    }

    @Override
    protected void setupViews() {
        setHasOptionsMenu(true);
        userModel = getHolder().get();
        categoriesSelected = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        feedPostRecycler = new FeedPostRecycler(getContext(), (CompanyModel) getHolder().get());
        tapeRecyclerView.setLayoutManager(linearLayoutManager);
        tapeRecyclerView.setAdapter(feedPostRecycler);

        postQueryService = PostQueryService.getService(this);
        refreshQueryService = RefreshQueryService.getService(this);
        LocationService locationService = LocationService.getService(this);
        locationService.init(getContext());

        tapeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                    return;
                }

                if (dy < 0 && !fab.isShown()) {
                    fab.show();
                }
            }
        });
        tapeRecyclerView.addOnScrollListener(new OnEndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (feedPostRecycler.getItemCount() <= 20) return;

                refreshQueryService.loadOld(
                        userModel.getAccessToken(),
                        feedPostRecycler.getLastPostId(),
                        100000,
                        location.getLatitude(),
                        location.getLongitude(),
                        categoriesSelected);
            }
        });

        swipeRefreshLayout.setColorSchemeColors(Palette.getSwipeRefreshPalette());
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onPostObtained(PostsResponseModel posts) {
        feedPostRecycler.addAll(posts.getPosts());
    }

    @Override
    public void onCreatePost() {}

    @Override
    public void onError(ErrorModel error) {
        Log.d(TAG, error.toString());
        if (swipeRefreshLayout.isShown())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLocationFind(Location location) {
        this.location = location;

        postQueryService.obtainPosts(
                getHolder().get().getAccessToken(),
                Locale.getDefault().getLanguage(),
                100000,
                location.getLatitude(),
                location.getLongitude(),
                categoriesSelected);
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onRefresh() {
        refreshQueryService.loadNew(
                userModel.getAccessToken(),
                100000,
                location.getLatitude(),
                location.getLongitude(),
                feedPostRecycler.getFirstPostId(),
                categoriesSelected);
    }

    @Override
    public void onLoadNew(PostsResponseModel posts) {
        if (swipeRefreshLayout.isShown())
            swipeRefreshLayout.setRefreshing(false);
        feedPostRecycler.addAhead(posts.getPosts());
    }

    @Override
    public void onLoadOld(PostsResponseModel posts) {
        feedPostRecycler.addBehind(posts.getPosts());
    }

    @Override
    public void onEmpty() {
        Log.d(TAG, "EMPTY");
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

        if (id == R.id.action_choose_category) {
            Intent intent = new Intent(getContext(), CategoriesSelectionResult.class);
            startActivityForResult(intent, CATEGORIES_ACTIVITY_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleCategoriesIntent(@NonNull Intent intent) {
        Log.d(TAG, "Query");
        categoriesSelected = intent.getIntegerArrayListExtra(CATEGORIES_ATTACH);
        feedPostRecycler.refresh();
        postQueryService.obtainPosts(
                getHolder().get().getAccessToken(),
                Locale.getDefault().getLanguage(),
                100000,
                location.getLatitude(),
                location.getLongitude(),
                categoriesSelected);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != BaseActivity.RESULT_OK) return;

        if (requestCode == CATEGORIES_ACTIVITY_REQUEST) {
            handleCategoriesIntent(data);
            return;
        }
    }
}
