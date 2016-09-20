package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.PlainCompanyPointerModel;

import java.util.List;

/**
 * Created by Alexadner Dadukin on 20.09.2016.
 */

public class UserSubscriptionsContainerModel extends BaseResponseModel<List<PlainCompanyPointerModel>> {

    List<PlainCompanyPointerModel> getSubscriptions() {
        return mResponse;
    }
}
