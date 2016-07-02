package com.github.sasd97.upitter.models;

import com.github.sasd97.upitter.models.skeletons.RequestSkeleton;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alexadner Dadukin on 30.06.2016.
 */
public class CompanyModel extends UserModel
        implements RequestSkeleton {

    private String mId;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("category")
    @Expose
    private int mCategory;

    private PhoneModel mPhone;

    @SerializedName("contactPhones")
    @Expose
    private List<String> mContactPhones;

    @SerializedName("site")
    @Expose
    private String mSite;

    @SerializedName("coordinates")
    @Expose
    private List<CoordinatesModel> mCoordinates;

    @SerializedName("temporaryToken")
    @Expose
    private String mTemporaryToken;

    private boolean mIsVerify = false;
    private String mAvatarUrl;

    private String mAccessToken;

    private CompanyModel(Builder builder) {
        mName = builder.name;
        mDescription = builder.description;
        mCategory = builder.category;
        mPhone = builder.phone;
        mContactPhones = builder.contactPhones;
        mSite = builder.site;
        mCoordinates = builder.coordinates;
        mAccessToken = builder.accessToken;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public String getAccessToken() {
        return mAccessToken;
    }

    @Override
    public boolean isVerify() {
        return mIsVerify;
    }

    @Override
    public UserType getType() {
        return UserType.Company;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

    public static class Builder {

        private String name;
        private String description;
        private int category;
        private PhoneModel phone;
        private List<String> contactPhones;
        private String site;
        private List<CoordinatesModel> coordinates;
        private String accessToken;
        private String temporaryToken;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder category(int category) {
            this.category = category;
            return this;
        }

        public Builder phone(PhoneModel phone) {
            this.phone = phone;
            return this;
        }

        public Builder contactPhones(List<String> contactPhones) {
            this.contactPhones = contactPhones;
            return this;
        }

        public Builder site(String site) {
            this.site = site;
            return this;
        }

        public Builder coordinates(List<CoordinatesModel> coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder temporaryToken(String temporaryToken) {
            this.temporaryToken = temporaryToken;
            return this;
        }

        public CompanyModel build() {
            return new CompanyModel(this);
        }
    }
}
