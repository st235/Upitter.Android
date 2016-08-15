package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.RequestCodePointerModel;

/**
 * Created by alexander on 24.06.16.
 */

public class AuthorizationRequestCodeContainerModel extends BaseResponseModel<RequestCodePointerModel> {

    public RequestCodePointerModel getRequestCode() {
        return mResponse;
    }

    @Override
    public String toString() {
        return getResponseInfo();
    }
}
