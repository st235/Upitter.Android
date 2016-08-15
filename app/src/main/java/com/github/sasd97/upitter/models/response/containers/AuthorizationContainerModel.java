package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.UserPointerModel;

import java.util.Locale;

/**
 * Created by Alex on 10.06.2016.
 */

public class AuthorizationContainerModel extends BaseResponseModel<UserPointerModel> {

    public UserPointerModel getUser() {
        return mResponse;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Response info: %1$s\nUser: %2$s",
                getResponseInfo(),
                mResponse == null ? "null" : mResponse.toString());
    }
}
