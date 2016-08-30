package com.github.sasd97.upitter.components;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.github.sasd97.upitter.models.response.pointers.ImagePointerModel;
import com.github.sasd97.upitter.utils.Dimens;
import com.orhanobut.logger.Logger;

import java.util.List;

import static com.github.sasd97.upitter.constants.ImageCollageConstants.Collage;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.WIDE_PICTURE;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.SQUARE_PICTURE;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.TIGHT_PICTURE;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.MINIMUM_TWICE_COLLAGE_PERCENTAGE;

import static com.github.sasd97.upitter.constants.ImageCollageConstants.MAXIMUM_TIGHT_IMAGE_HEIGHT;

import static com.github.sasd97.upitter.utils.CollageUtils.placeView;
import static com.github.sasd97.upitter.utils.CollageUtils.isAllWide;

/**
 * Created by alexander on 19.07.16.
 */

public class CollageLayoutManager extends RecyclerView.LayoutManager {

    private static final int FIRST_VIEW = 0;
    private static final int OFFSET_VIEW_POSITION = 1;
    private static final int LEFT_POSITION = 0;
    private static final int TOP_POSITION = 0;

    private Collage type;
    private List<ImagePointerModel> images;

    private int margin;

    public CollageLayoutManager(List<ImagePointerModel> images) {
        this.images = images;
        this.margin = Dimens.dpToPx(1);
        setMeasurementCacheEnabled(true);
        setAutoMeasureEnabled(true);
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
                toVerticalGridCollage(recycler);
                break;
            case TWO_LAYER_COLLAGE_SMALL:
                toTwoLayerCollage(recycler, 1, 0.7);
                break;
            case TWO_LAYER_COLLAGE_MEDIUM:
                toTwoLayerCollage(recycler, 2, 0.75);
                break;
            case TWO_LAYER_COLLAGE_HIGH:
                toTwoLayerCollage(recycler, 3, 0.8);
                break;
            case UNKNOWN_COLLAGE:
                break;
        }
    }

    //region Simple Collage
    private void toSimpleCollage(RecyclerView.Recycler recycler) {
        ImagePointerModel image = images.get(FIRST_VIEW);

        switch (image.getType()) {
            case SQUARE_PICTURE:
                toSquareCollage(recycler);
                break;
            case TIGHT_PICTURE:
                toNotSquareCollage(recycler, image);
                break;
            case WIDE_PICTURE:
                toNotSquareCollage(recycler, image);
                break;
        }
    }

    private void toNotSquareCollage(RecyclerView.Recycler recycler, ImagePointerModel image) {
        ImageView imageView = (ImageView) recycler.getViewForPosition(FIRST_VIEW);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imageView);

        final int width = getWidth();
        final double coefficient = (float) image.getWidth() / (float) width;
        int height = (int) (image.getHeight()/ coefficient);

        if (height > MAXIMUM_TIGHT_IMAGE_HEIGHT) height = MAXIMUM_TIGHT_IMAGE_HEIGHT;

        measureChildWithMargins(imageView, width, height);
        Logger.d(String.valueOf(coefficient));
        layoutDecorated(imageView, LEFT_POSITION, TOP_POSITION, width, height);
    }

    private void toSquareCollage(RecyclerView.Recycler recycler) {
        ImageView imageView = (ImageView) recycler.getViewForPosition(FIRST_VIEW);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imageView);

        final int width = getWidth();
        final int height = width;

        measureChildWithMargins(imageView, width, height);
        layoutDecorated(imageView, LEFT_POSITION, TOP_POSITION, width, height);
    }
    //endregion

    //region Twice Collage
    private void toTwiceCollage(RecyclerView.Recycler recycler) {
        final int itemCount = getItemCount();
        final int parentWidth = getWidth();

        if (isAllWide(images)) {
            toHorizontalEaseCollage(recycler, itemCount, parentWidth);
            return;
        }

        toVerticalEaseCollage(recycler, itemCount, parentWidth);
    }

    private void toHorizontalEaseCollage(RecyclerView.Recycler recycler, int itemCount, int parentWidth) {
        ImagePointerModel sample = images.get(FIRST_VIEW);
        final double coefficient = (float) sample.getWidth() / (float) parentWidth;

        int offset = 0;

        for (int i = 0; i < itemCount; i++) {
            ImagePointerModel image = images.get(i);
            ImageView imageView = (ImageView) recycler.getViewForPosition(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addView(imageView);

            final int tHeight = (int) (image.getHeight() / coefficient);

            measureChildWithMargins(imageView, parentWidth, offset + tHeight);
            layoutDecorated(imageView, 0, offset, parentWidth, offset + tHeight);

            offset += tHeight + margin;
        }
    }

    private void toVerticalEaseCollage(RecyclerView.Recycler recycler, int itemCount, int parentWidth) {
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
    //endregion

    //region Vertical Grid collage (3-4)
    private void toVerticalGridCollage(RecyclerView.Recycler recycler) {
        final int itemCount = getItemCount();
        final int horizontalSectionCount = itemCount - 1;

        final double firstViewPercentage = 0.6;

        int averageX = getHeight() / horizontalSectionCount;
        int horizontalOffset = (int) (getWidth() * firstViewPercentage);
        int verticalOffset = 0;

        ImageView firstView = (ImageView) recycler.getViewForPosition(FIRST_VIEW);
        firstView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        placeView(this, firstView, 0, 0, getHeight(), horizontalOffset);

        for (int i = OFFSET_VIEW_POSITION; i < itemCount; i++) {
            ImageView imageView = (ImageView) recycler.getViewForPosition(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            placeView(this, imageView, verticalOffset, horizontalOffset + margin, verticalOffset + averageX, getWidth());
            verticalOffset += averageX + margin;
        }
    }
    //endregion

    //region Two Layer collage (5-10)
    private void toTwoLayerCollage(RecyclerView.Recycler recycler, int firstLayerAmount, double firstLayerPercentage) {
        final int itemCount = getItemCount();
        final int averageFirstLayerX = getWidth() / firstLayerAmount;
        final int averageSecondLayerX = getWidth() / (itemCount - firstLayerAmount);
        int counter = 0;

        int offsetX = 0;
        int firstLayerHeight = (int) (getHeight() * firstLayerPercentage);
        for (; counter < firstLayerAmount; counter++) {
            ImageView imageView = (ImageView) recycler.getViewForPosition(counter);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            placeView(this, imageView, 0, offsetX, firstLayerHeight, offsetX + averageFirstLayerX);
            offsetX += averageFirstLayerX + margin;
        }


        offsetX = 0;
        for (; counter < itemCount; counter++) {
            ImageView imageView = (ImageView) recycler.getViewForPosition(counter);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            placeView(this, imageView, firstLayerHeight + margin, offsetX, getHeight(), offsetX + averageSecondLayerX);
            offsetX += averageSecondLayerX + margin;
        }
    }
    //endregion

    @Override
    public boolean performAccessibilityAction(RecyclerView.Recycler recycler, RecyclerView.State state, int action, Bundle args) {
        return super.performAccessibilityAction(recycler, state, action, args);
    }
}
