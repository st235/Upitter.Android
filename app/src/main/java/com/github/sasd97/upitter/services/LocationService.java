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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.github.sasd97.upitter.models.CoordinatesModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 23.06.16.
 */
public class LocationService implements LocationListener {

    private final static String TAG = "Location Service";

    public interface OnLocationListener {
        void onLocationFind(Location location);
        void onLocationChanged(Location location);
    }

    private final long MIN_TIME = 2000;
    private final float MIN_DISTANCE = 10;

    private LocationManager locationManager;

    private Location currentLocation;

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
        if (currentLocation != null) {
            Log.d(TAG, "location initialized");
            listener.onLocationFind(currentLocation);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "location changed");
        listener.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, String.format("Current provider change state. Description: %1$s, flag: %2$d", s, i));
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(TAG, String.format("Current provider enabled. Description: %1$s", s));
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG, String.format("Current provider disabled. Description: %1$s", s));
    }
}
