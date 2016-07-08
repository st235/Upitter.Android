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
public class PostsResponseModel extends BaseResponseModel<List<PostsResponseModel>> {

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

    @SerializedName("dateOffset")
    @Expose
    private String mDateOffset;

    @SerializedName("rating")
    @Expose
    private long mRating;

    private String getId() {
        return mPostId;
    }

    private CompanyResponseModel getCompany() {
        return mCompany;
    }

    private String getTitle() {
        return mTitle;
    }

    private String getText() {
        return mText;
    }

    private String getDateOffset() {
        return mDateOffset;
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
