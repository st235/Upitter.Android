package com.github.sasd97.upitter.models.response.pointers;

import com.github.sasd97.upitter.utils.ListUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 04.09.16.
 */
public class CommentsPointerModel {

    @SerializedName("amount")
    @Expose
    private int mAmount;

    @SerializedName("comments")
    @Expose
    private List<CommentPointerModel> mComments;

    public int getAmount() {
        return mAmount;
    }

    public List<CommentPointerModel> getComments() {
        return mComments;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$d\n%2$s",
                mAmount,
                ListUtils.toString(mComments));
    }
}
