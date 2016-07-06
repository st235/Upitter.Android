package com.github.sasd97.upitter.models.skeletons;

import android.os.Parcelable;

/**
 * Created by alexander on 05.07.16.
 */
public interface SearchableSkeleton extends Parcelable {

    enum IMAGE_TYPE { STRING, INT }

    String getTitle();
    String getPreview();
    String getSubTitle();
    IMAGE_TYPE getImageType();
    boolean isChecked();
    boolean isImage();
    String getStringImage();
    Integer getIntImage();
}
