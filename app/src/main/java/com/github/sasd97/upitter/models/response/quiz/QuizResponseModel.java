package com.github.sasd97.upitter.models.response.quiz;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 13.07.16.
 */
public class QuizResponseModel extends BaseResponseModel {


    @SerializedName("value")
    @Expose
    private String mValue;

    @SerializedName("count")
    @Expose
    private int mCount = 0;

    @SerializedName("myVote")
    @Expose
    private boolean mIsMyVote = false;

    public String getValue() {
        return mValue;
    }

    public int getCount() {
        return mCount;
    }

    public boolean isMyVote() {
        return mIsMyVote;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Quiz Variant\nValue: %1$s\nCount: %2$d",
                mValue,
                mCount);
    }
}