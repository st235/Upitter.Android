package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 15.08.2016.
 */
public class SocialIconPointerModel {

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("avatar")
    @Expose
    private String mAvatar;

    public String getTitle() {
        return mTitle;
    }

    public String getAvatar() {
        return mAvatar;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Social Icon\nTitle: %1$s\nAvatar: %2$d",
                mTitle,
                mAvatar);
    }
}
