package com.github.sasd97.upitter.utils;

/**
 * Created by alexander on 19.07.16.
 */

import static com.github.sasd97.upitter.constants.ImageCollageConstants.Collage;

import static com.github.sasd97.upitter.constants.ImageCollageConstants.WIDE_PICTURE;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.SQUARE_PICTURE;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.TIGHT_PICTURE;

import static com.github.sasd97.upitter.constants.ImageCollageConstants.MEASURE_UNIT;


public class CollageUtils {

    private CollageUtils() {}

    public static int calculateImageType(int x, int y) {
        double aspect = (double) x / (double) y;
        if (aspect > MEASURE_UNIT) return TIGHT_PICTURE;
        if (aspect < MEASURE_UNIT) return WIDE_PICTURE;
        return SQUARE_PICTURE;
    }

    public static double calculateAspectRatio(int x, int y) {
        return (double) x / (double) y;
    }

    public static Collage getCollageType(int amount) {
        return Collage.getType(amount);
    }
}
