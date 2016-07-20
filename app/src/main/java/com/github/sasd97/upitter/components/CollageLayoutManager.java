package com.github.sasd97.upitter.components;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.sasd97.upitter.utils.CollageUtils;
import com.github.sasd97.upitter.utils.Dimens;

import java.util.List;

import static com.github.sasd97.upitter.constants.ImageCollageConstants.Collage;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.WIDE_PICTURE;

/**
 * Created by alexander on 19.07.16.
 */

public class CollageLayoutManager extends RecyclerView.LayoutManager {

    private static final int FIRST_VIEW = 0;
    private static final int OFFSET_VIEW_POSITION = 1;
    private static final int LEFT_POSITION = 0;
    private static final int TOP_POSITION = 0;


    private static final String TAG = "Collage Layout Manager";

    private Collage type;
    private List<Bitmap> images;

    private int margin;

    public CollageLayoutManager(List<Bitmap> images) {
        this.images = images;
        this.margin = Dimens.dpToPx(1);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView
                .LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.type = Collage.getType(getItemCount());
        Log.d(TAG, String.format("Type of collage: %1$s", type.toString()));
        detachAndScrapAttachedViews(recycler);
        chooseCollage(recycler);
    }

    private void chooseCollage(RecyclerView.Recycler recycler) {
        switch (type) {
            case SIMPLE_COLLAGE:
                toSimpleCollage(recycler);
                break;
            case TWICE_COLLAGE:
                toTwiceCollage(recycler);
                break;
            case VERTICAL_GRID_COLLAGE:

        }
    }

    private void toSimpleCollage(RecyclerView.Recycler recycler) {
        ImageView imageView = (ImageView) recycler.getViewForPosition(FIRST_VIEW);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imageView);
        measureChildWithMargins(imageView, getWidth(), getHeight());
        layoutDecorated(imageView, LEFT_POSITION, TOP_POSITION, getWidth(), getHeight());
    }

    private void toTwiceCollage(RecyclerView.Recycler recycler) {
        final int itemCount = getItemCount();
        final int width = getWidth();
        final int height = getHeight();

        if (isAllWide()) {
            toHorizontalEaseCollage(recycler, itemCount, width, height);
            return;
        }

        toVerticalEaseCollage(recycler, itemCount, width, height);
    }

    private void toHorizontalEaseCollage(RecyclerView.Recycler recycler, int itemCount, int width, int height) {
        final double halfPercentage = 0.5;
        int offset = 0;

        for (int i = 0; i < itemCount; i++) {
            ImageView imageView = (ImageView) recycler.getViewForPosition(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addView(imageView);

            measureChildWithMargins(imageView, getWidth(), offset + (int) (getHeight() * halfPercentage));
            layoutDecorated(imageView, 0, offset, getWidth(), (int) (offset + getHeight() * halfPercentage));

            offset += getHeight() * halfPercentage + margin;
        }
    }

    private void toVerticalEaseCollage(RecyclerView.Recycler recycler, int itemCount, int width, int height) {
        final double halfPercentage = 0.5;
        int offset = 0;

        for (int i = 0; i < itemCount; i++) {
            ImageView imageView = (ImageView) recycler.getViewForPosition(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addView(imageView);

            measureChildWithMargins(imageView, offset + (int) (getWidth() * halfPercentage), getHeight());
            layoutDecorated(imageView, offset, 0, (int) (offset + getWidth() * halfPercentage), getHeight());

            offset += getWidth() * halfPercentage + margin;
        }
    }

    private void toVerticalGridCollage(RecyclerView.Recycler recycler) {
        final int itemCount = getItemCount();
        final double firstViewPercentage = 0.6;

        View firstView = recycler.getViewForPosition(FIRST_VIEW);
        placeView(firstView, 0, 0, getHeight(), (int) (getWidth() * firstViewPercentage));

        for (int i = OFFSET_VIEW_POSITION; i < getItemCount(); i++) {

        }
    }

    private void placeView(View view,
                           int topPosition,
                           int leftPosition,
                           int bottomPosition,
                           int rightPosition) {
        addView(view);
        measureChildWithMargins(view, rightPosition, bottomPosition);
        layoutDecorated(view, leftPosition, topPosition, rightPosition, bottomPosition);
    }

    private boolean isAllWide() {
        boolean wide = true;
        for (Bitmap image: images) wide &= (
                CollageUtils.calculateImageType(image.getHeight(), image.getWidth()) == WIDE_PICTURE);
        return wide;
    }

    private int countSummaryX() {
        int result = 0;
        for (Bitmap image: images) result += image.getHeight();
        return result;
    }

    private int countSummaryY() {
        int result = 0;
        for (Bitmap image: images) result += image.getWidth();
        return result;
    }

    @Override
    public boolean performAccessibilityAction(RecyclerView.Recycler recycler, RecyclerView.State state, int action, Bundle args) {
        return super.performAccessibilityAction(recycler, state, action, args);
    }

    private int countMaxX() {
        int max = 0;
        for (Bitmap image: images)
            if (image.getHeight() > max)
                max = image.getHeight();
        return max;
    }
}
