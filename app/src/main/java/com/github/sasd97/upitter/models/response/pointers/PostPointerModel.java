package com.github.sasd97.upitter.models.response.pointers;

import com.github.sasd97.upitter.models.AuthorOnMapModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 08.07.16.
 */

public class PostPointerModel {

    @SerializedName("customId")
    @Expose
    private String mPostId;

    @SerializedName("author")
    @Expose
    private CompanyPointerModel mCompany;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("text")
    @Expose
    private String mText;

    @SerializedName("category")
    @Expose
    private String mCategoryId;

    @SerializedName("variants")
    @Expose
    private List<QuizPointerModel> mQuiz;

    @SerializedName("images")
    @Expose
    private List<ImagePointerModel> mImages;

    @SerializedName("fromNow")
    @Expose
    private String mDateFromNow;

    @SerializedName("likesAmount")
    @Expose
    private String mLikesAmount;

    @SerializedName("votersAmount")
    @Expose
    private int mVotersAmount;

    @SerializedName("commentsAmount")
    @Expose
    private int mCommentsAmount;

    @SerializedName("watchesAmount")
    @Expose
    private int mWatchesAmount;

    @SerializedName("isLikedByMe")
    @Expose
    private boolean mIsLikedByMe = false;

    @SerializedName("isVotedByMe")
    @Expose
    private boolean mIsVotedByMe = false;

    @SerializedName("coordinates")
    @Expose
    private CoordinatesPointerModel mCoordinates;

    public String getId() {
        return mPostId;
    }

    public CompanyPointerModel getCompany() {
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

    public List<QuizPointerModel> getQuiz() {
        return mQuiz;
    }

    public List<ImagePointerModel> getImages() { return mImages; }

    public int getCommentsAmount() { return mCommentsAmount; }

    public int getVotersAmount() { return mVotersAmount; }

    public int getWatchesAmount() { return mWatchesAmount; }

    public CoordinatesPointerModel getCoordinates() {
        return mCoordinates;
    }

    public boolean isLikedByMe() {
        return mIsLikedByMe;
    }

    public boolean isVotedByMe() {
        return mIsVotedByMe;
    }

    public AuthorOnMapModel toAuthorOnMapModel() {
        return new AuthorOnMapModel
                .Builder()
                .authorName(mCompany.getName())
                .authorAvatarUrl(mCompany.getLogoUrl())
                .latitude(mCoordinates.getLatitude())
                .longitude(mCoordinates.getLongitude())
                .build();
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Post\n[%1$s\n%2$s]\n" +
                "Author: %3$s\nLikes(Rating): %4$s\nCoordinates: (%5$f, %6$f)",
                mTitle,
                mText,
                mCompany == null ? "Null" : mCompany.toString(),
                mLikesAmount,
                mCoordinates.getLatitude(),
                mCoordinates.getLongitude());
    }
}
