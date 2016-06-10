package com.github.sasd97.upitter.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alex on 10.06.2016.
 */
public class ErrorResponseModel {

    @SerializedName("code")
    @Expose
    private int mCode = -1;

    @SerializedName("message")
    @Expose
    private String mMessage = "";

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Error: [code: %1$d,\n message: %2$s]",
                mCode,
                mMessage);
    }
}
