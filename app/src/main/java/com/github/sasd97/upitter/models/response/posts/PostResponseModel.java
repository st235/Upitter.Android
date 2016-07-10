package com.github.sasd97.upitter.models.response.posts;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;
import com.github.sasd97.upitter.models.response.phone.PhoneResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 08.07.16.
 */
public class PostResponseModel {

    @SerializedName("customId")
    @Expose
    private String mPostId;

    @SerializedName("author")
    @Expose
    private CompanyResponseModel mCompany;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("text")
    @Expose
    private String mText;

    @SerializedName("category")
    @Expose
    private String mCategoryId;

    @SerializedName("dateOffset")
    @Expose
    private String mDateOffset;

    @SerializedName("likes")
    @Expose
    private long mRating;

    @SerializedName("isLikedByMe")
    @Expose
    private boolean mIsLikedByMe;

    public String getId() {
        return mPostId;
    }

    public CompanyResponseModel getCompany() {
        return mCompany;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public String getDateOffset() {
        return mDateOffset;
    }

    public boolean isLikedByMe() {
        return mIsLikedByMe;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Post\n[%1$s\n%2$s]\n" +
                "Author: %3$s\nLikes(Rating): %4$d",
                mTitle,
                mText,
                mCompany.toString(),
                mRating);
    }
}
