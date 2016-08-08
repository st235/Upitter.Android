package com.github.sasd97.upitter.components;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.sasd97.upitter.models.response.fileServer.FileResponseModel;
import com.github.sasd97.upitter.models.response.fileServer.ImageResponseModel;
import com.github.sasd97.upitter.utils.CollageUtils;
import com.github.sasd97.upitter.utils.Dimens;
import com.orhanobut.logger.Logger;

import java.util.List;

import static com.github.sasd97.upitter.constants.ImageCollageConstants.Collage;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.SQUARE_PICTURE;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.TIGHT_PICTURE;
import static com.github.sasd97.upitter.constants.ImageCollageConstants.WIDE_PICTURE;

/**
 * Created by alexander on 19.07.16.
 */

public class CollageLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "Collage Layout Manager";

    private static final int FIRST_VIEW = 0;
    private static final int OFFSET_VIEW_POSITION = 1;
    private static final int LEFT_POSITION = 0;
    private static final int TOP_POSITION = 0;

    private Collage type;
    private List<ImageResponseModel> images;

    private int margin;

    public CollageLayoutManager(List<ImageResponseModel> images) {
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

    private void toSimpleCollage(RecyclerView.Recycler recycler) {
        toSquareCollage(recycler);
//        ImageResponseModel image = images.get(FIRST_VIEW);
//
//        Logger.d(image.toString());
//        Logger.d(image.getType());
//
//        switch (image.getType()) {
//            case SQUARE_PICTURE:
//                toSquareCollage(recycler);
//                break;
//            case TIGHT_PICTURE:
//                toTightCollage(recycler);
//                break;
//            case WIDE_PICTURE:
//                toWideCollage(recycler);
//                break;
//        }
    }

    private void toWideCollage(RecyclerView.Recycler recycler) {
        ImageView imageView = (ImageView) recycler.getViewForPosition(FIRST_VIEW);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imageView);

        final int width = getWidth();
        final int height = width / 2;

        measureChildWithMargins(imageView, getWidth(), getHeight());
        layoutDecorated(imageView, LEFT_POSITION, TOP_POSITION, width, height);
    }

    private void toSquareCollage(RecyclerView.Recycler recycler) {
        ImageView imageView = (ImageView) recycler.getViewForPosition(FIRST_VIEW);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imageView);
        measureChildWithMargins(imageView, getWidth(), getHeight());
        layoutDecorated(imageView, LEFT_POSITION, TOP_POSITION, getWidth(), getHeight());
    }

    private void toTightCollage(RecyclerView.Recycler recycler) {
        ImageView imageView = (ImageView) recycler.getViewForPosition(FIRST_VIEW);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imageView);

        final int width = getWidth();
        final int height = width * 2;

        measureChildWithMargins(imageView, getWidth(), getHeight());
        layoutDecorated(imageView, LEFT_POSITION, TOP_POSITION, width, height);
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
        final int horizontalSectionCount = itemCount - 1;

        final double firstViewPercentage = 0.6;

        int averageX = getHeight() / horizontalSectionCount;
        int horizontalOffset = (int) (getWidth() * firstViewPercentage);
        int verticalOffset = 0;

        ImageView firstView = (ImageView) recycler.getViewForPosition(FIRST_VIEW);
        firstView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        placeView(firstView, 0, 0, getHeight(), horizontalOffset);

        for (int i = OFFSET_VIEW_POSITION; i < itemCount; i++) {
            ImageView imageView = (ImageView) recycler.getViewForPosition(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            placeView(imageView, verticalOffset, horizontalOffset + margin, verticalOffset + averageX, getWidth());
            verticalOffset += averageX + margin;
        }
    }

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
            placeView(imageView, 0, offsetX, firstLayerHeight, offsetX + averageFirstLayerX);
            offsetX += averageFirstLayerX + margin;
        }


        offsetX = 0;
        for (; counter < itemCount; counter++) {
            ImageView imageView = (ImageView) recycler.getViewForPosition(counter);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            placeView(imageView, firstLayerHeight + margin, offsetX, getHeight(), offsetX + averageSecondLayerX);
            offsetX += averageSecondLayerX + margin;
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
        for (ImageResponseModel image: images) wide &= (
                CollageUtils.calculateImageType(image.getHeight(), image.getWidth()) == WIDE_PICTURE);
        return wide;
    }

    private int countSummaryX() {
        int result = 0;
        for (ImageResponseModel image: images) result += image.getHeight();
        return result;
    }

    private int countSummaryY() {
        int result = 0;
        for (ImageResponseModel image: images) result += image.getWidth();
        return result;
    }

    @Override
    public boolean performAccessibilityAction(RecyclerView.Recycler recycler, RecyclerView.State state, int action, Bundle args) {
        return super.performAccessibilityAction(recycler, state, action, args);
    }

    private int countMaxX() {
        int max = 0;
        for (ImageResponseModel image: images)
            if (image.getHeight() > max)
                max = image.getHeight();
        return max;
    }
}
