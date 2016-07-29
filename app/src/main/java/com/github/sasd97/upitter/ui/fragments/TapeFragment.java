package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.behaviors.OnEndlessRecyclerViewScrollListener;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.services.query.RefreshQueryService;
import com.github.sasd97.upitter.ui.adapters.TapeRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CategoriesActivity;
import com.github.sasd97.upitter.utils.Palette;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.github.sasd97.upitter.Upitter.*;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.CATEGORIES_ATTACH;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CATEGORIES_ACTIVITY_REQUEST;

/**
 * Created by alexander on 08.07.16.
 */

public class TapeFragment extends BaseFragment
        implements PostQueryService.OnPostListener,
        RefreshQueryService.OnRefreshListener,
        LocationService.OnLocationListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TAPE FRAGMENT";

    private UserModel userModel;
    private Location location;

    private FloatingActionButton fab;

    private PostQueryService postQueryService;
    private RefreshQueryService refreshQueryService;
    private LocationService locationService;

    private RecyclerView tapeRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private TapeRecyclerAdapter tapeRecyclerAdapter;

    private ArrayList<Integer> categoriesSelected;

    public TapeFragment() {
        super(R.layout.tape_fragment);
    }

    public static TapeFragment getFragment() {
        return new TapeFragment();
    }

    @Override
    protected void bindViews() {
        fab = findById(R.id.fab);
        tapeRecyclerView = findById(R.id.recycler_view_tape_fragment);
        swipeRefreshLayout = findById(R.id.swipe_layout_tape);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        userModel = getHolder().get();
        categoriesSelected = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getContext());
        tapeRecyclerAdapter = new TapeRecyclerAdapter(getContext(), (CompanyModel) getHolder().get());
        tapeRecyclerView.setLayoutManager(linearLayoutManager);
        tapeRecyclerView.setAdapter(tapeRecyclerAdapter);

        postQueryService = PostQueryService.getService(this);
        refreshQueryService = RefreshQueryService.getService(this);
        locationService = LocationService.getService(this);
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
                if (tapeRecyclerAdapter.getItemCount() <= 20) return;

                refreshQueryService.loadOld(
                        userModel.getAccessToken(),
                        tapeRecyclerAdapter.getLastPostId(),
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
        tapeRecyclerAdapter.addAll(posts.getPosts());
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
                tapeRecyclerAdapter.getFirstPostId(),
                categoriesSelected);
    }

    @Override
    public void onLoadNew(PostsResponseModel posts) {
        if (swipeRefreshLayout.isShown())
            swipeRefreshLayout.setRefreshing(false);
        tapeRecyclerAdapter.addAhead(posts.getPosts());
    }

    @Override
    public void onLoadOld(PostsResponseModel posts) {
        tapeRecyclerAdapter.addBehind(posts.getPosts());
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
            Intent intent = new Intent(getContext(), CategoriesActivity.class);
            startActivityForResult(intent, CATEGORIES_ACTIVITY_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleCategoriesIntent(@NonNull Intent intent) {
        Log.d(TAG, "Query");
        categoriesSelected = intent.getIntegerArrayListExtra(CATEGORIES_ATTACH);
        tapeRecyclerAdapter.refresh();
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
