package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexander on 26.09.16.
 */

public class ComplaintPointerModel {

    @SerializedName("customId")
    @Expose
    private String mCustomId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    public String getId() {
        return mCustomId;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
