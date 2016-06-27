package com.github.sasd97.upitter.models.response.categories;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 27.06.16.
 */
public class CategoryResponseModel extends BaseResponseModel {

    @SerializedName("customId")
    @Expose
    private int mId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("parentCategory")
    @Expose
    private int mParentId;

    @SerializedName("logoUrl")
    @Expose
    private String mAvatarUrl;

    private int getId() {
        return mId;
    }

    private String getTitle() {
        return mTitle;
    }

    private int getParentId() {
        return mParentId;
    }

    private String getLogoUrl() {
        return mAvatarUrl;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Category [%1$d] with parent %2$d\n%3$s\nAvatarUrl: %4$s",
                mId,
                mParentId,
                mTitle,
                mAvatarUrl);
    }
}
