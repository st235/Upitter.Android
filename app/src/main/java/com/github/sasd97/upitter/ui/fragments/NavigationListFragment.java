package com.github.sasd97.upitter.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.IntentKeysConstants;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.skeletons.PaginationHolder;
import com.github.sasd97.upitter.services.GeocoderService;
import com.github.sasd97.upitter.ui.adapters.recyclers.SetupLocationRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CompanyCoordinatesSelectionResult;
import com.github.sasd97.upitter.ui.results.SetupLocationResult;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.LOCATION_LIST;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CHOOSE_ON_MAP_POINT_REQUEST;

/**
 * Created by alexander on 19.08.16.
 */

public class NavigationListFragment extends BaseFragment
        implements SetupLocationRecycler.OnCoordinateClickListener,
        GeocoderService.OnAddressListener {

    public interface OnChooseCoordinateListener {
        void onChoose(int position);
    }

    @BindView(R.id.recycler_navigation_list) RecyclerView coordinatesRecyclerView;

    private PaginationHolder paginationHolder;
    private SetupLocationRecycler locationRecycler;
    private List<CoordinatesModel> coordinatesModels;
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

    public void setPaginationHolder(@NonNull PaginationHolder paginationHolder) {
        this.paginationHolder = paginationHolder;
    }

    public void setChooseCoordinatesListener(@NonNull OnChooseCoordinateListener listener) {
        this.listener = listener;
    }

    @Override
    protected void setupViews() {
        coordinatesModels = getArguments().getParcelableArrayList(LOCATION_LIST);
        if (coordinatesModels == null) return;

        locationRecycler = new SetupLocationRecycler(coordinatesModels, this);
        coordinatesRecyclerView.setLayoutManager(linearLayoutManager);
        coordinatesRecyclerView.setAdapter(locationRecycler);
    }

    @Override
    public void onCoordinateClick(int position) {
        listener.onChoose(position);
    }

    @OnClick(R.id.add_coordinates)
    public void onAddLocationClick(View v) {
        startActivityForResult(new Intent(getActivity(), CompanyCoordinatesSelectionResult.class), CHOOSE_ON_MAP_POINT_REQUEST);
    }

    @OnClick(R.id.finish_coordinates_editing)
    public void onFinishEditingClick(View v) {
        Intent intent = new Intent(getContext(), SetupLocationResult.class);
        ArrayList<CoordinatesModel> concreteCoordinates = new ArrayList<>(coordinatesModels);
        intent.putParcelableArrayListExtra(LOCATION_LIST, concreteCoordinates);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onAddressReady(CoordinatesModel coordinatesModel) {
        locationRecycler.add(coordinatesModel);
        paginationHolder.redraw(coordinatesModels);
    }

    @Override
    public void onDelete(int position, List<CoordinatesModel> coordinatesModels) {
        paginationHolder.redraw(coordinatesModels);
    }

    @Override
    public void onAddressFail() {

    }

    private void handleCoordinates(Intent data) {
        CoordinatesModel coordinatesModel = data.getParcelableExtra(IntentKeysConstants.COORDINATES_ATTACH);
        GeocoderService.find(getContext(), coordinatesModel, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != BaseActivity.RESULT_OK) return;
        if (requestCode == CHOOSE_ON_MAP_POINT_REQUEST) handleCoordinates(data);
    }
}
