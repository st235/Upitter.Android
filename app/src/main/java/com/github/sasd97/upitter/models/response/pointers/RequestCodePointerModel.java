package com.github.sasd97.upitter.models.response.pointers;

import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 24.06.16.
 */

public class RequestCodePointerModel {

    @SerializedName("isAuthorized")
    @Expose
    private boolean mIsAuthorized;

    @SerializedName("attempts")
    @Expose
    private int mAttemptsAmount = -1;

    @SerializedName("temporaryToken")
    @Expose
    private String mTemporaryToken;

    @SerializedName("businessUser")
    @Expose
    private CompanyPointerModel mBusinessUser;

    public boolean isAuthorized() {
        return mIsAuthorized;
    }

    public String getTemporaryToken() {
        return mTemporaryToken;
    }

    public int getAttemptsAmount() {
        return mAttemptsAmount;
    }

    public CompanyPointerModel getCompany() {
        return mBusinessUser;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Is authorized: %1$b, TemporaryToken: %2$s",
                mIsAuthorized,
                mTemporaryToken);
    }
}
