package com.github.sasd97.upitter.constants;

import com.github.sasd97.upitter.utils.Dimens;

/**
 * Created by alexander on 19.07.16.
 */
public interface ImageCollageConstants {

    int MEASURE_UNIT = 1;

    int WIDE_PICTURE = 0;
    int SQUARE_PICTURE = 1;
    int TIGHT_PICTURE = 2;

    double MINIMUM_TWICE_COLLAGE_PERCENTAGE = 0.3;

    //region Simple Collage
    int MAXIMUM_TIGHT_IMAGE_HEIGHT = Dimens.dpToPx(450);
    //endregion


    //region Twice Collage
    int MAXIMUM_VERTICAL_COLLAGE = Dimens.dpToPx(450);
    //endregion

    enum Collage {
        SIMPLE_COLLAGE(0, 1),
        TWICE_COLLAGE(2),
        VERTICAL_GRID_COLLAGE(3, 4),
        TWO_LAYER_COLLAGE_SMALL(5, 6),
        TWO_LAYER_COLLAGE_MEDIUM(7, 8),
        TWO_LAYER_COLLAGE_HIGH(9, 10),
        UNKNOWN_COLLAGE;

        private int start, end;

        Collage() {}

        Collage(int middle) {
            this.start = middle;
            this.end = middle;
        }

        Collage(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public boolean in(int value) {
            return value >= start && value <= end;
        }

        public static Collage getType(int value) {
            if (SIMPLE_COLLAGE.in(value)) return SIMPLE_COLLAGE;
            if (TWICE_COLLAGE.in(value)) return TWICE_COLLAGE;
            if (VERTICAL_GRID_COLLAGE.in(value)) return VERTICAL_GRID_COLLAGE;
            if (TWO_LAYER_COLLAGE_SMALL.in(value)) return TWO_LAYER_COLLAGE_SMALL;
            if (TWO_LAYER_COLLAGE_MEDIUM.in(value)) return TWO_LAYER_COLLAGE_MEDIUM;
            if (TWO_LAYER_COLLAGE_HIGH.in(value)) return TWO_LAYER_COLLAGE_HIGH;
            return UNKNOWN_COLLAGE;
        }
    }
}
