package com.github.sasd97.upitter.models.response.pointers;

import com.github.sasd97.upitter.utils.ListUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 05.09.16.
 */
public class SubscribersPointerModel {

    @SerializedName("amount")
    @Expose
    private String mAmount;

    @SerializedName("subscribers")
    @Expose
    private List<SubscriberPointerModel> mSubscribers;

    public String getAmount() {
        return mAmount;
    }

    public List<SubscriberPointerModel> getSubscribers() {
        return mSubscribers;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$s\n%2$s\n\n",
                mAmount,
                ListUtils.toString(mSubscribers));
    }
}
