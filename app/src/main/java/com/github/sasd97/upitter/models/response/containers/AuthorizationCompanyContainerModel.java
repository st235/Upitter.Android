package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;

/**
 * Created by alexander on 30.06.16.
 */

public class AuthorizationCompanyContainerModel extends BaseResponseModel<CompanyPointerModel> {

    public CompanyPointerModel getBusinessUser() {
        return mResponse;
    }

    @Override
    public String toString() {
        return getResponseInfo();
    }
}
