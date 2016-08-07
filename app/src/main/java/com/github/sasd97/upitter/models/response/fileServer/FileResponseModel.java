package com.github.sasd97.upitter.models.response.fileServer;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 22.07.2016.
 */
public class FileResponseModel extends BaseResponseModel<FileResponseModel> {

    @SerializedName("fid")
    private String mFid;

    public String getFid() {
        return mFid;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "File %s", mFid);
    }
}
