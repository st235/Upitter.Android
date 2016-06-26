package com.github.sasd97.upitter.events;

import com.github.sasd97.upitter.models.skeletons.ImageSkeleton;

import java.util.ArrayList;

/**
 * Created by Alex on 07.02.2016.
 */

public interface OnGalleryInteractionListener {

    void onThumbnailClick(int position, ImageSkeleton path);
    void onMultiSelectionCounterClick(int position, ArrayList<ImageSkeleton> selected);
    void onMultiSelectionLimitExceeded();
}
