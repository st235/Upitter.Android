package com.github.sasd97.upitter.models.response.pointers;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 15.08.2016.
 */
public class SocialIconPointerModel {

    @SerializedName("customId")
    @Expose
    private String mCustomId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("icon")
    @Expose
    private String mIcon;

    @SerializedName("link")
    @Expose
    private String mLink;

    public String getId() {
        return mCustomId;
    }
    
    public String getTitle() {
        return mTitle;
    }

    public String getIcon() {
        return mIcon;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(@NonNull String link) {
        mLink = link;
    }

    public boolean isIcon() {
        return mIcon != null && !mIcon.trim().equalsIgnoreCase("");
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Social Icon\nTitle: %1$s\nAvatar: %2$s",
                mTitle,
                mIcon);
    }
}
