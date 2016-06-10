package com.github.sasd97.upitter.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alex on 10.06.2016.
 */
public abstract class BaseResponseModel {

    @SerializedName("success")
    @Expose
    private boolean mIsSuccess = false;

    @SerializedName("error")
    @Expose
    private ErrorResponseModel mError;

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public boolean isError() {
        return mError != null;
    }

    public ErrorResponseModel getError() {
        return mError;
    }

    public String getResponseInfo() {
        return String.format(Locale.getDefault(), "Response status: %1$b with error: %1$s",
                mIsSuccess,
                mError == null ? "null" : mError.toString());
    }
}
