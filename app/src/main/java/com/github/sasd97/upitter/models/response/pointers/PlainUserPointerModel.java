package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 04.09.2016.
 */

public class PlainUserPointerModel {

    @SerializedName("customId")
    @Expose
    private String mCustomId;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("logoUrl")
    @Expose
    private String mLogoUrl;

    @SerializedName("avatar")
    @Expose
    private String mAvatar;

    public String getId() {
        return mCustomId;
    }

    public String getName() {
        return mName;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public String getAvatar() {
        return mAvatar;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Plain User %1$s\n%2$s\n%3$s",
                mCustomId,
                mName,
                mLogoUrl);
    }
}
