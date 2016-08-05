package com.github.sasd97.upitter.holders;

import android.location.Location;

import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.utils.Prefs;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by alexander on 05.08.16.
 */

import static com.github.sasd97.upitter.constants.LocationConstants.DEFAULT_RADIUS;
import static com.github.sasd97.upitter.constants.LocationConstants.RADIUS_KEY;

public class LocationHolder {

    private static int userRadius;
    private static Location userLocation;

    private static boolean isBlocked = false;

    private LocationHolder() {}

    public static void init() {
        userRadius = Prefs.get().getInt(RADIUS_KEY, DEFAULT_RADIUS);
    }

    public static void setLocation(Location location) {
        if (isBlocked) return;
        userLocation = location;
    }

    public static void setfLocation(Location location) {
        userLocation = location;
        isBlocked = true;
    }

    public static void setfLocation(LatLng location) {
        Location target = new Location("");
        target.setLatitude(location.latitude);
        target.setLongitude(location.longitude);
        userLocation = target;
        isBlocked = true;
    }

    public static Location getLocation() {
        return userLocation;
    }

    public static void setRadius(int radius) {
        Prefs.put(RADIUS_KEY, radius);
        userRadius = radius;
    }

    public static int getRadius() {
        return userRadius;
    }

    public static void reset(Location target) {
        isBlocked = false;
        userLocation = target;
    }
}
