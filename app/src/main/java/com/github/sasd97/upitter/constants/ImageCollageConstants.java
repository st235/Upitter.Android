package com.github.sasd97.upitter.constants;

/**
 * Created by alexander on 19.07.16.
 */
public interface ImageCollageConstants {

    int MEASURE_UNIT = 1;

    int WIDE_PICTURE = 0;
    int SQUARE_PICTURE = 1;
    int TIGHT_PICTURE = 2;

    enum Collage {
        SIMPLE_COLLAGE(0, 1),
        TWICE_COLLAGE(2),
        VERTICAL_GRID_COLLAGE(3, 4),
        TWO_LAYER_COLLAGE(5, 10),
        UNKNOWN_COLLAGE;

        private int start, end;

        private Collage() {}

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
            if (TWO_LAYER_COLLAGE.in(value)) return TWO_LAYER_COLLAGE;
            return UNKNOWN_COLLAGE;
        }
    }
}
