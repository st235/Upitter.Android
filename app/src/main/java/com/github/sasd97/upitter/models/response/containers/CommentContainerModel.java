package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.CommentPointerModel;
import com.github.sasd97.upitter.utils.ListUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 04.09.2016.
 */
public class CommentContainerModel extends BaseResponseModel<CommentPointerModel> {

    public CommentPointerModel getComment() {
        return mResponse;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$s\n",
                String.valueOf(mResponse));
    }
}
