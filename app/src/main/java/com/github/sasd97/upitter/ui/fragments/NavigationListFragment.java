package com.github.sasd97.upitter.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.ui.adapters.recyclers.SetupLocationRecycler;
import com.github.sasd97.upitter.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.LOCATION_LIST;

/**
 * Created by alexander on 19.08.16.
 */

public class NavigationListFragment extends BaseFragment implements SetupLocationRecycler.OnCoordinateClickListener {

    public interface OnChooseCoordinateListener {
        void onChoose(int position);
    }

    @BindView(R.id.recycler_navigation_list) RecyclerView coordinatesRecyclerView;

    private SetupLocationRecycler locationRecycler;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    private OnChooseCoordinateListener listener;

    public NavigationListFragment() {
        super(R.layout.fragment_navigation_list);
    }

    public static NavigationListFragment getFragment(@NonNull ArrayList<CoordinatesModel> coordinates,
                                                     @NonNull OnChooseCoordinateListener listener) {
        NavigationListFragment fragment = new NavigationListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LOCATION_LIST, coordinates);
        fragment.setArguments(args);
        fragment.setChooseCoordinatesListener(listener);
        return fragment;
    }

    public void setChooseCoordinatesListener(@NonNull OnChooseCoordinateListener listener) {
        this.listener = listener;
    }

    @Override
    protected void setupViews() {
        List<CoordinatesModel> coordinatesModels = getArguments().getParcelableArrayList(LOCATION_LIST);

        locationRecycler = new SetupLocationRecycler(coordinatesModels, this);
        coordinatesRecyclerView.setLayoutManager(linearLayoutManager);
        coordinatesRecyclerView.setAdapter(locationRecycler);
    }

    @Override
    public void onCoordinateClick(int position) {
        listener.onChoose(position);
    }
}
