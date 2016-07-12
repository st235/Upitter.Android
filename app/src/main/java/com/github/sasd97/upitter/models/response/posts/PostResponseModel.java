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
public class PostResponseModel extends BaseResponseModel<PostResponseModel> {

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

    @SerializedName("fromNow")
    @Expose
    private String mDateFromNow;

    @SerializedName("likesAmount")
    @Expose
    private String mLikesAmount;

    @SerializedName("commentsAmount")
    @Expose
    private int mCommentsAmount;

    @SerializedName("isLikedByMe")
    @Expose
    private boolean mIsLikedByMe = false;

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

    public String getDateFromNow() {
        return mDateFromNow;
    }

    public String getLikesAmount() {
        return mLikesAmount;
    }

    public int getCommentsAmount() { return mCommentsAmount; }

    public boolean isLikedByMe() {
        return mIsLikedByMe;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Post\n[%1$s\n%2$s]\n" +
                "Author: %3$s\nLikes(Rating): %4$s",
                mTitle,
                mText,
                mCompany == null ? "Null" : mCompany.toString(),
                mLikesAmount);
    }
}
