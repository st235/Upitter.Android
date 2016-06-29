package com.github.sasd97.upitter.ui.results;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.github.sasd97.upitter.R;
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

    private LocationService locationService = LocationService.getService(this);
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_choose_activity);
        setToolbar(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT, 0.2f));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        locationService.init(this);

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
        Log.d("LOCATION", location.toString());
        moveToPoint(googleMap, new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("CHANGED", location.toString());
        moveToPoint(googleMap, new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onAddressReady(Address address) {

    }

    private void moveToPoint(GoogleMap googleMap, LatLng points) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points, 15));
    }
}
