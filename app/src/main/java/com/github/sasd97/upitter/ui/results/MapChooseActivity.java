package com.github.sasd97.upitter.ui.results;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.IntentKeysConstants;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

public class MapChooseActivity extends BaseActivity implements OnMapReadyCallback, LocationService.OnLocationListener {

    private GoogleMap googleMap;
    private FloatingActionButton confirmFab;

    private LocationService locationService = LocationService.getService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_choose_activity);
        setToolbar(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        confirmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResultClick();
            }
        });
    }

    @Override
    protected void bindViews() {
        confirmFab = findById(R.id.fab);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        locationService.init(this);
        confirmFab.show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });
    }

    @Override
    public void onLocationFind(Location location) {
        moveToPoint(googleMap, new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onLocationChanged(Location location) {
        moveToPoint(googleMap, new LatLng(location.getLatitude(), location.getLongitude()));
    }

    public void onResultClick() {
        LatLng coordinates = googleMap.getCameraPosition().target;
        CoordinatesModel coordinatesModel
                = new CoordinatesModel
                .Builder()
                .latitude(coordinates.latitude)
                .longitude(coordinates.longitude)
                .build();

        Intent result = new Intent();
        result.putExtra(IntentKeysConstants.COORDINATES_ATTACH, coordinatesModel);
        setResult(RESULT_OK, result);
        finish();
    }

    private void moveToPoint(GoogleMap googleMap, LatLng points) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points, 15));
    }
}
