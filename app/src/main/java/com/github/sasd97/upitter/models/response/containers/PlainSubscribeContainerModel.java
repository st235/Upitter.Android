package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.PlainCompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PlainSubscribePointerModel;

/**
 * Created by Alexadner Dadukin on 21.09.2016.
 */

public class PlainSubscribeContainerModel extends BaseResponseModel<PlainSubscribePointerModel> {

    public PlainSubscribePointerModel getSubscribe() {
        return mResponse;
    }
}
