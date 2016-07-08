package com.github.sasd97.upitter.models.response.company;

import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.coordinates.CoordinatesResponseModel;
import com.github.sasd97.upitter.models.response.phone.PhoneResponseModel;
import com.github.sasd97.upitter.utils.ListUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 28.06.16.
 */
public class CompanyResponseModel extends BaseResponseModel {

    @SerializedName("customId")
    @Expose
    private String mCustomId;

    @SerializedName("isVerify")
    @Expose
    private boolean mIsVerify;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("logoUrl")
    @Expose
    private String mLogoUrl;

    @SerializedName("activity")
    @Expose
    private List<Integer> mActivities;

    @SerializedName("site")
    @Expose
    private String mSite;

    @SerializedName("contactsPhones")
    @Expose
    private List<String> mContactPhones;

    @SerializedName("coordinates")
    @Expose
    private List<CoordinatesResponseModel> mCoordinates;

    @SerializedName("phone")
    @Expose
    private PhoneResponseModel mPhone;

    @SerializedName("accessToken")
    @Expose
    private String mAccessToken;

    public String getId() {
        return mCustomId;
    }

    public boolean isVerify() {
        return mIsVerify;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() { return mDescription; }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public List<Integer> getActivity() {
        return mActivities;
    }

    public String getSite() {
        return mSite;
    }

    public List<String> getContactPhones() {
        return mContactPhones;
    }

    public List<CoordinatesModel> getCoordinates() {
        return ListUtils.mutate(mCoordinates, new ListUtils.OnListMutateListener<CoordinatesResponseModel, CoordinatesModel>() {
            @Override
            public CoordinatesModel mutate(CoordinatesResponseModel object) {
                return new CoordinatesModel.Builder()
                        .latitude(object.getLatitude())
                        .longitude(object.getLongitude())
                        .addressName(object.getAddress())
                        .build();
            }
        });
    }

    public PhoneResponseModel getPhone() {
        return mPhone;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    @Override
    public String toString() {
        StringBuilder activities = new StringBuilder();
        for (Integer activity: mActivities)
            activities.append(activity).append("\n");

        return String.format(Locale.getDefault(), "Business\nCustom id: %1$s\nName: %2$s\nIs verify: %3$b\nActivities: [%4$s]\nSite: %5$s\n",
                mCustomId,
                mName,
                mIsVerify,
                activities.toString(),
                mSite);
    }
}
