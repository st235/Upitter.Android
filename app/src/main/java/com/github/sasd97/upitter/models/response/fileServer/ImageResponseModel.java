package com.github.sasd97.upitter.models.response.fileServer;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.utils.CollageUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 04.07.16.
 */

public class ImageResponseModel extends BaseResponseModel {

    @SerializedName("width")
    @Expose
    private int mWidth;

    @SerializedName("height")
    @Expose
    private int mHeight;

    @SerializedName("aspectRatio")
    @Expose
    private double mAspectRatio;

    @SerializedName("path")
    @Expose
    private String mPath;

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public double getAspectRatio() {
        return mAspectRatio;
    }

    public int getType() {
        return CollageUtils.calculateImageType(mHeight, mWidth);
    }

    public String getPath() {
        return mPath;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Path image %1$s",
                mPath);
    }
}
