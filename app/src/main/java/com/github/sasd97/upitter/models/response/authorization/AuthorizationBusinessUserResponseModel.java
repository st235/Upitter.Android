package com.github.sasd97.upitter.models.response.authorization;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.businessUser.BusinessUserResponseModel;

/**
 * Created by alexander on 30.06.16.
 */
public class AuthorizationBusinessUserResponseModel extends BaseResponseModel<BusinessUserResponseModel> {

    public BusinessUserResponseModel getBusinessUser() {
        return mResponse;
    }

    @Override
    public String toString() {
        return getResponseInfo();
    }
}
