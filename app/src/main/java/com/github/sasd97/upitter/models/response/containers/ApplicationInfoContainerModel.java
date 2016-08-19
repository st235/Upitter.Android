package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.ApplicationInfoPointerModel;

/**
 * Created by alexander on 20.08.16.
 */
public class ApplicationInfoContainerModel extends BaseResponseModel<ApplicationInfoPointerModel> {

    @Override
    public String toString() {
        return mResponse.toString();
    }
}
