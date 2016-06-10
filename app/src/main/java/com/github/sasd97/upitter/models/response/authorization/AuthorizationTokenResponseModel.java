package com.github.sasd97.upitter.models.response.authorization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alex on 10.06.2016.
 */
public class AuthorizationTokenResponseModel {

    @SerializedName("token")
    @Expose
    private String mToken;

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Token: %1$s",
                mToken);
    }
}
