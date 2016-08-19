package com.github.sasd97.upitter.ui.fragments;

import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.BindView;

/**
 * Created by Alexander Dadukin on 18.08.2016.
 */

public class NavigationPaginationPreviewFragment extends BaseFragment implements OnMapReadyCallback {

    @BindView(R.id.address_pagination) TextView addressPaginationTxv;
    @BindView(R.id.fab_pagination_left) FloatingActionButton fabPaginationLeft;
    @BindView(R.id.fab_pagination_right) FloatingActionButton fabPaginationRight;

    public NavigationPaginationPreviewFragment() {
        super(R.layout.fragment_navigation_pagination_preview);
    }

    public static NavigationPaginationPreviewFragment getFragment() {
        return new NavigationPaginationPreviewFragment();
    }

    @Override
    protected void setupViews() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
