package com.github.sasd97.upitter.models.response.authorization;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.requestCode.RequestCodeResponseModel;

/**
 * Created by alexander on 24.06.16.
 */

public class AuthorizationRequestCodeResponseModel extends BaseResponseModel<RequestCodeResponseModel> {

    public RequestCodeResponseModel getRequestCode() {
        return mResponse;
    }

    @Override
    public String toString() {
        return getResponseInfo();
    }
}
