package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Alex on 31.01.2016.
 */

public final class Dimens {

    public static final int ABSOLUTE_ZERO = 0;
    private static final int DEFAULT_ROUND_RADIUS = 4;

    private Dimens() {}

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int drr() {
        return dpToPx(DEFAULT_ROUND_RADIUS);
    }

    public static Point getDisplay(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
