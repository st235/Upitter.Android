package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.github.sasd97.upitter.R;

/**
 * Created by Alex on 09.06.2016.
 */
public final class Palette {

    private static Context context;

    private Palette() {}

    public static void init(Context c) {
        context = c;
    }

    public static int obtainColor(int colorResId) {
        return ContextCompat.getColor(context, colorResId);
    }

    public static int getAvatarPalette() {
        return ContextCompat.getColor(context, R.color.colorPrimary);
    }
}
