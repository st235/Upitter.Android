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

    @SerializedName("count")
    @Expose
    private int mAmount;

    @SerializedName("subscribers")
    @Expose
    private List<String> mSubscribersIds;

    public int getAmount() {
        return mAmount;
    }

    public List<String> getSubscribers() {
        return mSubscribersIds;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$d\n%2$s",
                mAmount,
                ListUtils.toString(mSubscribersIds));
    }
}
