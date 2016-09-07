package com.github.sasd97.upitter.models.response.pointers;

import com.github.sasd97.upitter.utils.ListUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 05.09.16.
 */
public class SubscriberPointerModel {

    @SerializedName("customId")
    @Expose
    private String mCustomId;

    @SerializedName("nickname")
    @Expose
    private String mNickname;

    @SerializedName("logoUrl")
    @Expose
    private String mLogoUrl;

    public String getId() {
        return mCustomId;
    }

    public String getNick() {
        return mNickname;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$s\n%2$s\n%3$s\n\n",
                mCustomId,
                mNickname,
                mLogoUrl == null ? "Null" : mLogoUrl);
    }
}
