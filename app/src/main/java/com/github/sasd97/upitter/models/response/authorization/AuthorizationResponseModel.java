package com.github.sasd97.upitter.models.response.authorization;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.user.UserResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alex on 10.06.2016.
 */
public class AuthorizationResponseModel extends BaseResponseModel {

    @SerializedName("response")
    @Expose
    private UserResponseModel mUserResponseModel;

    public UserResponseModel getUser() {
        return mUserResponseModel;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Response info: %1$s\nUser: %2$s",
                getResponseInfo(),
                mUserResponseModel == null ? "null" : mUserResponseModel.toString());
    }
}
