package com.github.sasd97.upitter.models.response.fileServer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexander on 27.07.16.
 */
public class ExtraResponseModel {

    @SerializedName("width")
    @Expose
    private int mWidth;

    @SerializedName("height")
    @Expose
    private int mHeight;

    @SerializedName("aspectRatio")
    @Expose
    private double mAspectRatio;

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public double getAspectRatio() {
        return mAspectRatio;
    }
}
