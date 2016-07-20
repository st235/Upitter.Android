package com.github.sasd97.upitter.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.github.sasd97.upitter.constants.PermissionsConstants.REQUEST_RECEIVE_SMS;

/**
 * Created by alexander on 24.06.16.
 */

public class Permissions {

    private static final String TAG = "Permission utils";

    private Permissions() {}

    @TargetApi(Build.VERSION_CODES.M)
    public static void getPermissionToReceiveSMS(AppCompatActivity activity) {
        getPermission(activity, REQUEST_RECEIVE_SMS, Manifest.permission.RECEIVE_SMS);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void getPermission(AppCompatActivity activity, int requestCode, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        List<String> notGrantedPermissions = new ArrayList<>(permissions.length);

        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), permission)
                    == PackageManager.PERMISSION_GRANTED) continue;

            if (activity.shouldShowRequestPermissionRationale(permission)) return; // TODO: work with rationale

            notGrantedPermissions.add(permission);
        }

        if (notGrantedPermissions.size() == 0) return;
        activity.requestPermissions(ListUtils.toArray(String.class, notGrantedPermissions), requestCode);
    }

    public static boolean checkGrantState(@NonNull String[] permissions,
                                          @NonNull int[] state) {
        boolean result = true;
        StringBuilder info = new StringBuilder();

        for (int i = 0; i < permissions.length; i++) {
            info.append(permissions[i]).append(" state ").append(state[i]).append("\n");
            result &= (state[i] == PackageManager.PERMISSION_GRANTED);
        }

        Log.d(TAG, info.toString());
        return result;
    }
}
