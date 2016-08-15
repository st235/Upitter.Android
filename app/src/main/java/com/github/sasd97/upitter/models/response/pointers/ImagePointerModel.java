package com.github.sasd97.upitter.models.response.pointers;

import com.github.sasd97.upitter.utils.CollageUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 04.07.16.
 */

public class ImagePointerModel {

    @SerializedName("fid")
    @Expose
    private String mFid;

    @SerializedName("uuid")
    @Expose
    private String mUuid;

    @SerializedName("width")
    @Expose
    private int mWidth;

    @SerializedName("height")
    @Expose
    private int mHeight;

    @SerializedName("aspectRatio")
    @Expose
    private double mAspectRatio;

    @SerializedName("type")
    @Expose
    private String mFileType;

    @SerializedName("originalUrl")
    @Expose
    private String mOriginalUrl;

    @SerializedName("thumbUrl")
    @Expose
    private String mThumbUrl;

    public String getFid() {
        return mFid;
    }

    public String getUuid() {
        return mUuid;
    }

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

    public String getOriginalUrl() {
        return mOriginalUrl;
    }

    public String getThumbUrl() {
        return mThumbUrl;
    }

    public String toJson() {
        Gson builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return builder.toJson(this);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "Path image\n%1$s\n%2$s\nwidth: %3$d\nheight: %4$d\n",
                mOriginalUrl,
                mThumbUrl,
                mWidth,
                mHeight);
    }
}
