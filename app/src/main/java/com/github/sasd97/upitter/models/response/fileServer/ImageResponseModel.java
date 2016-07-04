package com.github.sasd97.upitter.models.response.fileServer;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 04.07.16.
 */
public class ImageResponseModel extends BaseResponseModel {

    @SerializedName("path")
    @Expose
    private String mPath;

    public String getPath() {
        return mPath;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Path image %1$s",
                mPath);
    }
}
