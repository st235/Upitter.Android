package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.SubscribersPointerModel;

/**
 * Created by alexander on 05.09.16.
 */
public class SubscribersContainerModel extends BaseResponseModel<SubscribersPointerModel> {

    public SubscribersPointerModel getSubscribers() {
        return mResponse;
    }

    @Override
    public String toString() {
        return mResponse.toString();
    }
}
