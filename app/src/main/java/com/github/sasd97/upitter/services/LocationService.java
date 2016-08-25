package com.github.sasd97.upitter.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.github.sasd97.upitter.holders.LocationHolder;
import com.github.sasd97.upitter.utils.Connectivity;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by alexander on 23.06.16.
 */

@SuppressWarnings("MissingPermission")
public class LocationService implements LocationListener {

    private final static String TAG = "Location Service";

    public interface OnLocationListener {
        void onLocationFind(Location location);

        void onLocationChanged(Location location);
    }

    private static LocationService service;
    private static boolean isInit = false;

    private final long MIN_TIME = 2000;
    private final float MIN_DISTANCE = 10;

    private Location currentLocation;
    private OnLocationListener listener;
    private LocationManager locationManager;

    private LocationService(OnLocationListener context) {
        listener = context;
    }

    public static LocationService getService(OnLocationListener listener) {
        if (service != null) return service(listener);
        service = new LocationService(listener);
        return service;
    }

    public static LocationService service(OnLocationListener listener) {
        service.listener = listener;
        return service;
    }

    public static boolean isInit() {
        return isInit;
    }

    public void init(Context context) {
        if (isInit) {
            Logger.v("Method was init");
            Logger.v(String.valueOf(currentLocation != null));
            if (currentLocation != null) listener.onLocationFind(currentLocation);
            return;
        }

        Logger.v("Init method is executed");
        Logger.v("Is available call location: %1$b", isPermissionGrant(context));

        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = Connectivity.isConnected() && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                ? LocationManager.NETWORK_PROVIDER : locationManager.getBestProvider(criteria, true);

        if (!isPermissionGrant(context)) {return;}
        if (provider != null) locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);

        currentLocation = getLastKnownLocation(context);

        if (currentLocation != null) {
            Logger.i(currentLocation.toString());
            LocationHolder.setLocation(currentLocation);
            listener.onLocationFind(currentLocation);
        }

        isInit = true;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "location changed");
        currentLocation = location;
        LocationHolder.setLocation(location);
        listener.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Logger.d("Current provider change state. Description: %1$s, flag: %2$d", s, i);
    }

    @Override
    public void onProviderEnabled(String s) {
        Logger.d("Current provider enabled. Description: %1$s", s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Logger.d("Current provider disabled. Description: %1$s", s);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    private Location getLastKnownLocation(Context context) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Logger.d(provider);
            if (!isPermissionGrant(context)) break;
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) continue;
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private boolean isPermissionGrant(Context context) {
        return ActivityCompat
                .checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
