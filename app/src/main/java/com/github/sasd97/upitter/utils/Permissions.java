package com.github.sasd97.upitter.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by alexander on 24.06.16.
 */
public class Permissions {

    public static final int REQUEST_RECEIVE_SMS = 3;

    private Permissions() {}

    @TargetApi(Build.VERSION_CODES.M)
    public static void getPermissionToReceiveSMS(AppCompatActivity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {
                }
            }

            activity.requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_RECEIVE_SMS);
        }
    }

}
