package com.github.sasd97.upitter.utils;

import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

/**
 * Created by Alex on 10.06.2016.
 */
public final class SlidrUtils {

    private SlidrUtils() {}

    public static SlidrConfig config(SlidrPosition position) {
        return new SlidrConfig.Builder()
                .position(position)
                .sensitivity(0.1f)
                .build();
    }

    public static SlidrConfig config(SlidrPosition position, float sensitivity) {
        return new SlidrConfig.Builder()
                                .position(position)
                                .sensitivity(sensitivity)
                                .build();
    }
}
