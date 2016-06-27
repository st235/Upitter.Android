package com.github.sasd97.upitter.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 23.06.16.
 */
public class LocationService implements LocationListener {

    public interface OnLocationListener {
        void onLocationFind(Location location);
        void onLocationChanged(Location location);
        void onAddressReady(Address address);
    }

    private final long MIN_TIME = 2000;
    private final float MIN_DISTANCE = 10;

    private LocationManager locationManager;
    private Geocoder geocoder;

    private Location currentLocation;
    private Address currentAddress;

    private OnLocationListener listener;

    private LocationService(OnLocationListener context) {
        listener = context;
    }

    public static LocationService getService(OnLocationListener listener) {
        return new LocationService(listener);
    }

    public void init(Context context) {
        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        geocoder = new Geocoder(context, Locale.ENGLISH);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat
                .checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);

        currentLocation = locationManager.getLastKnownLocation(provider);
        if (currentLocation != null) listener.onLocationFind(currentLocation);
    }

    @Override
    public void onLocationChanged(Location location) {
        listener.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void getAddress(Location location) {
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            currentAddress = addresses.get(0);
            listener.onAddressReady(currentAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
