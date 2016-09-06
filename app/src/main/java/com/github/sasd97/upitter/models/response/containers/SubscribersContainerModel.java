package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.SubscriberPointerModel;

/**
 * Created by alexander on 05.09.16.
 */
public class SubscribersContainerModel extends BaseResponseModel<SubscriberPointerModel> {

    public SubscriberPointerModel getSubscribers() {
        return mResponse;
    }

    @Override
    public String toString() {
        return mResponse.toString();
    }
}
