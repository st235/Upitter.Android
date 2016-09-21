package com.github.sasd97.upitter.models.response.pointers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexadner Dadukin on 21.09.2016.
 */

public class PlainSubscribePointerModel {

    @SerializedName("subscribe")
    @Expose
    private boolean mIsSubscribed = false;

    @SerializedName("subscribersAmount")
    @Expose
    private String mSubscriptionAmount;

    public boolean isSubscribed() {
        return mIsSubscribed;
    }

    public String getSubscriptionAmount() {
        return mSubscriptionAmount;
    }
}
