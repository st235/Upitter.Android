package com.github.sasd97.upitter.models.response.authorization;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alex on 10.06.2016.
 */
public class AuthorizationResponseModel extends BaseResponseModel {

    @SerializedName("response")
    @Expose
    private AuthorizationTokenResponseModel mAuthorizationTokenResponseModel;

    public AuthorizationTokenResponseModel getToken() {
        return mAuthorizationTokenResponseModel;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Token: %1$s",
                mAuthorizationTokenResponseModel == null ? "null" : mAuthorizationTokenResponseModel.toString());
    }
}
