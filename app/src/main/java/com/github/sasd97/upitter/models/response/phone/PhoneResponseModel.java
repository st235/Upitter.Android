package com.github.sasd97.upitter.models.response.phone;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 30.06.16.
 */

public class PhoneResponseModel extends BaseResponseModel {

    @SerializedName("code")
    @Expose
    private String mPhoneCode;

    @SerializedName("body")
    @Expose
    private String mPhoneBody;

    @SerializedName("fullNumber")
    @Expose
    private String mFullNumber;

    public String getPhoneCode() {
        return mPhoneCode;
    }

    public String getPhoneBody() {
        return mPhoneBody;
    }

    public String getFullNumber() {
        return mFullNumber;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Phone:\nCode: %1$s\nBody: %2$s\nFull number: %3$s",
                mPhoneCode,
                mPhoneBody,
                mFullNumber);
    }
}
