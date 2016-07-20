package com.github.sasd97.upitter.ui.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.services.query.PostQueryService;
import com.github.sasd97.upitter.ui.adapters.TapeRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseFragment;

import java.util.Locale;

import static com.github.sasd97.upitter.Upitter.*;

/**
 * Created by alexander on 08.07.16.
 */

public class TapeFragment extends BaseFragment
        implements PostQueryService.OnPostListener,
        LocationService.OnLocationListener {

    private static final String TAG = "TAPE FRAGMENT";

    private PostQueryService queryService;
    private LocationService locationService;

    private LinearLayoutManager linearLayoutManager;
    private TapeRecyclerAdapter tapeRecyclerAdapter;
    private RecyclerView tapeRecyclerView;

    private FloatingActionButton fab;

    public TapeFragment() {
        super(R.layout.tape_fragment);
    }

    public static TapeFragment getFragment() {
        return new TapeFragment();
    }

    @Override
    protected void bindViews() {
        tapeRecyclerView = findById(R.id.recycler_view_tape_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(getContext());
        tapeRecyclerAdapter = new TapeRecyclerAdapter(getContext(), (CompanyModel) getHolder().get());
        tapeRecyclerView.setLayoutManager(linearLayoutManager);
        tapeRecyclerView.setAdapter(tapeRecyclerAdapter);

        queryService = PostQueryService.getService(this);
        locationService = LocationService.getService(this);
        locationService.init(getContext());
    }


    @Override
    public void onPostObtained(PostsResponseModel posts) {
        Log.d("TAPE_FRAGMENT", posts.toString());
        tapeRecyclerAdapter.addAll(posts.getPosts());
    }

    @Override
    public void onCreatePost() {

    }

    @Override
    public void onError(ErrorModel error) {
        Log.d(TAG, error.toString());
    }

    @Override
    public void onLocationFind(Location location) {
        Log.d(TAG, "On location find");

        queryService.obtainPosts(
                getHolder().get().getAccessToken(),
                Locale.getDefault().getLanguage(),
                20,
                0,
                location.getLatitude(),
                location.getLongitude());
    }

    @Override
    public void onLocationChanged(Location location) {
    }
}
