package com.github.sasd97.upitter.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.skeletons.PaginationHolder;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Alexander Dadukin on 18.08.2016.
 */

import static com.github.sasd97.upitter.constants.IntentKeysConstants.LOCATION_LIST;

public class NavigationPaginationPreviewFragment extends BaseFragment
        implements OnMapReadyCallback,
        PaginationHolder {

    private static final int MIN_POSITION = 0;

    @BindView(R.id.address_pagination) TextView addressPaginationTxv;
    @BindView(R.id.fab_pagination_left) FloatingActionButton fabPaginationLeft;
    @BindView(R.id.fab_pagination_right) FloatingActionButton fabPaginationRight;

    private int size = 0;
    private int position = 0;
    private GoogleMap googleMap;
    private ArrayList<CoordinatesModel> coordinates;

    public NavigationPaginationPreviewFragment() {
        super(R.layout.fragment_navigation_pagination_preview);
    }

    public static NavigationPaginationPreviewFragment getFragment(ArrayList<CoordinatesModel> coordinates) {
        NavigationPaginationPreviewFragment fragment = new NavigationPaginationPreviewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LOCATION_LIST, coordinates);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupViews() {
        coordinates = getArguments().getParcelableArrayList(LOCATION_LIST);
        size = coordinates.size();
        checkPagination(size);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        moveTo(addressPaginationTxv, googleMap, coordinates.get(position));
        this.googleMap = googleMap;

        for (CoordinatesModel coordinate: coordinates)
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()))
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_upitter_marker)));
    }

    private void checkPagination(int size) {
        if (position == MIN_POSITION) fabPaginationLeft.hide();
        else fabPaginationLeft.show();
        if (position == (size - 1)) fabPaginationRight.hide();
        else fabPaginationRight.show();
    }

    @Override
    public void redraw(List<CoordinatesModel> list) {
        this.position = 0;
        coordinates = new ArrayList<>(list);
        size = coordinates.size();
        checkPagination(size);

        googleMap.clear();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void moveTo(TextView addressHolder, GoogleMap googleMap, CoordinatesModel point) {
        addressHolder.setText(point.getAddressName());
        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(false);
    }

    @OnClick(R.id.fab_pagination_left)
    public void pagingLeft(View v) {
        if (position <= MIN_POSITION) return;
        position--;
        checkPagination(size);
        moveTo(addressPaginationTxv, googleMap, coordinates.get(position));
    }

    @OnClick(R.id.fab_pagination_right)
    public void pagingRight(View v) {
        if (position >= size - 1) return;
        position++;
        checkPagination(size);
        moveTo(addressPaginationTxv, googleMap, coordinates.get(position));
    }

    public void moveToPosition(int position) {
        if (position < MIN_POSITION || position > (size - 1)) return;
        this.position = position;
        checkPagination(size);
        moveTo(addressPaginationTxv, googleMap, coordinates.get(position));
    }
}
