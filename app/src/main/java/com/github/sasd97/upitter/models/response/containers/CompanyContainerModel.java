package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;

import java.util.List;
import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 15.08.2016.
 */
public class CompanyContainerModel extends BaseResponseModel<CompanyPointerModel> {

    public CompanyPointerModel getCompany() {
        return mResponse;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Response info: %1$s\nResponse: %2$s",
                getResponseInfo(),
                mResponse.toString());
    }
}
