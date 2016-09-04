package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.CommentsPointerModel;

/**
 * Created by Alexadner Dadukin on 04.09.2016.
 */
public class CommentsContainerModel extends BaseResponseModel<CommentsPointerModel> {

    public CommentsPointerModel getComments() {
        return mResponse;
    }

    @Override
    public String toString() {
        return mResponse.toString();
    }
}
