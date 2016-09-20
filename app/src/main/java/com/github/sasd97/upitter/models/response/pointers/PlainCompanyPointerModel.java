package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 04.09.2016.
 */

public class PlainCompanyPointerModel {

    @SerializedName("customId")
    @Expose
    private String mCustomId;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("alias")
    @Expose
    private String mAlias;

    @SerializedName("logoUrl")
    @Expose
    private String mLogoUrl;

    public String getId() {
        return mCustomId;
    }

    public String getName() {
        return mName;
    }

    public String getAlias() {
        return mName;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Plain Company %1$s\n%2$s\n%3$s\n%4$s",
                mCustomId,
                mName,
                mAlias,
                mLogoUrl);
    }
}
