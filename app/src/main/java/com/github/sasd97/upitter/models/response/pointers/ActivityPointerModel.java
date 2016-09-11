package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexadner Dadukin on 11.09.2016.
 */
public class ActivityPointerModel {

    @SerializedName("title")
    @Expose
    private String mTitle;

    public String getTitle() {
        return mTitle;
    }
}
