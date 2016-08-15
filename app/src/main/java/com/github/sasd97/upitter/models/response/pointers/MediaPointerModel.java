package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 22.07.2016.
 */

public class MediaPointerModel {

    @SerializedName("path")
    private String mPath;

    public String getPath() {
        return mPath;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Media %s", mPath);
    }
}
