package com.github.sasd97.upitter.models.response.authorization;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;

/**
 * Created by alexander on 30.06.16.
 */
public class AuthorizationCompanyResponseModel extends BaseResponseModel<CompanyResponseModel> {

    public CompanyResponseModel getBusinessUser() {
        return mResponse;
    }

    @Override
    public String toString() {
        return getResponseInfo();
    }
}
