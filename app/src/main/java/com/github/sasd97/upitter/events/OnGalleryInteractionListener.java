package com.github.sasd97.upitter.events;

import com.github.sasd97.upitter.models.skeletons.ImageSkeleton;

/**
 * Created by Alex on 07.02.2016.
 */

public interface OnGalleryInteractionListener {

    void onSingleChoose(int position, ImageSkeleton path);
    void onMultiChoose(int position, int counter, ImageSkeleton path);
}
