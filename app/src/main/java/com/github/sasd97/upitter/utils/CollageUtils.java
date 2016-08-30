package com.github.sasd97.upitter.utils;

/**
 * Created by alexander on 19.07.16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.sasd97.upitter.models.response.pointers.ImagePointerModel;

import java.util.List;

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

    public static void placeView(RecyclerView.LayoutManager manager,
                           View view,
                           int topPosition,
                           int leftPosition,
                           int bottomPosition,
                           int rightPosition) {
        manager.addView(view);
        manager.measureChildWithMargins(view, rightPosition, bottomPosition);
        manager.layoutDecorated(view, leftPosition, topPosition, rightPosition, bottomPosition);
    }

    public static boolean isAllWide(List<ImagePointerModel> images) {
        boolean wide = true;
        for (ImagePointerModel image: images) wide &= (
                CollageUtils.calculateImageType(image.getHeight(), image.getWidth()) == WIDE_PICTURE);
        return wide;
    }

    public static int countSummaryHeight(List<ImagePointerModel> images) {
        int result = 0;
        for (ImagePointerModel image: images) result += image.getHeight();
        return result;
    }

    public static int countSummaryWidth(List<ImagePointerModel> images) {
        int result = 0;
        for (ImagePointerModel image: images) result += image.getWidth();
        return result;
    }

    public static int countMaxHeight(List<ImagePointerModel> images) {
        int max = 0;
        for (ImagePointerModel image: images)
            if (image.getHeight() > max)
                max = image.getHeight();
        return max;
    }
}
