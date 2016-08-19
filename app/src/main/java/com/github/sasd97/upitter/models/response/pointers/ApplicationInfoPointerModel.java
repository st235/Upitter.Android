package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 20.08.16.
 */
public class ApplicationInfoPointerModel {

    @SerializedName("version")
    @Expose
    private int mVersion;

    @SerializedName("code")
    @Expose
    private int mCode;

    public int getVersion() {
        return mVersion;
    }

    public int getCode() {
        return mCode;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Version: %1$d Code %2$d",
                mVersion,
                mCode);
    }
}
