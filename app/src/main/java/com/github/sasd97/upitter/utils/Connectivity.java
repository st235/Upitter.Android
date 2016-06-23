package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Alex on 12.01.2016.
 */

public final class Connectivity {

    private static String IN_ROAMING = "ALLOW_IN_ROAMING";

    private static ConnectivityManager cManager = null;

    private Connectivity() {}

    public static void init(Context context) {
        cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isMobileConnected() {
        boolean isMobileConnected = false;

        NetworkInfo activeNetworkInfo  =
                cManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            isMobileConnected = activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnectedOrConnecting();
        }

        return isMobileConnected;
    }

    public static boolean isWifiConnected() {
        boolean isWifiConnected = false;

        NetworkInfo activeNetworkInfo  =
                cManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            isWifiConnected = activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnectedOrConnecting();
        }

        return isWifiConnected;
    }

    public static boolean isRoaming() {
        boolean isRoaming = false;

        NetworkInfo activeNetworkInfo  =
                cManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            isRoaming = activeNetworkInfo.isRoaming();
        }

        return isRoaming ? !isRoamingAllowed() : isRoaming;
    }

    public static boolean isRoamingAllowed() {
        return Prefs.get().getBoolean(IN_ROAMING, false);
    }

    public static boolean isConnected() {
        return isMobileConnected() || isWifiConnected();
    }

    public static int getConnectionType() {
        NetworkInfo activeNetworkInfo =
                cManager.getActiveNetworkInfo();

        return activeNetworkInfo == null ? -1 : activeNetworkInfo.getType();
    }
}
