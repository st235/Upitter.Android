package com.github.sasd97.upitter.ui.schemas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.AuthorOnMapModel;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.COORDINATES_ATTACH;

/**
 * Created by alexander on 21.07.16.
 */
public class ShowOnMapActivity extends BaseActivity
        implements OnMapReadyCallback,
        LocationService.OnLocationListener {

    private GoogleMap googleMap;
    private AuthorOnMapModel coordinatesToShow;

    private LocationService locationService = LocationService.getService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_choose_activity);
        setToolbar(R.id.toolbar, true);

        coordinatesToShow = getIntent().getParcelableExtra(COORDINATES_ATTACH);

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

        moveToPoint(googleMap, new LatLng(coordinatesToShow.getLatitude(), coordinatesToShow.getLongitude()));
    }

    @Override
    public void onLocationFind(Location location) {
        moveToPoint(googleMap, new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onLocationChanged(Location location) {
        moveToPoint(googleMap, new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void moveToPoint(GoogleMap googleMap, LatLng points) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points, 15));
    }
}
