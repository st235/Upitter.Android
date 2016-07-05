package com.github.sasd97.upitter.models.skeletons;

import android.os.Parcelable;

/**
 * Created by alexander on 05.07.16.
 */
public abstract class SearchableSkeleton implements Parcelable {

    public enum IMAGE_TYPE { STRING, INT }

    public abstract String getTitle();
    public abstract String getPreview();
    public abstract String getSubTitle();
    public abstract IMAGE_TYPE getImageType();
    public abstract boolean isChecked();
    public abstract boolean isImage();
    public abstract String getStringImage();
    public abstract Integer getIntImage();
}
