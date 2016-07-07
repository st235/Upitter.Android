package com.github.sasd97.upitter.models;

import com.github.sasd97.upitter.models.skeletons.RequestSkeleton;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 30.06.2016.
 */

public class CompanyModel extends UserModel
        implements RequestSkeleton {

    private String mId;
    private PhoneModel mPhone;
    private boolean mIsVerify = false;
    private String mAvatarUrl;
    private String mAccessToken;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("category")
    @Expose
    private List<Integer> mCategories;

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

    public CompanyModel() {}

    private CompanyModel(Builder builder) {
        mId = builder.id;
        mName = builder.name;
        mDescription = builder.description;
        mCategories = builder.categories;
        mPhone = builder.phone;
        mContactPhones = builder.contactPhones;
        mSite = builder.site;
        mCoordinates = builder.coordinates;
        mAccessToken = builder.accessToken;
        mTemporaryToken = builder.temporaryToken;
        mAvatarUrl = builder.avatarUrl;
    }

    @Override
    public String getUId() {
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

    public String getTemporaryToken() {
        return mTemporaryToken;
    }

    public PhoneModel getPhone() {
        return mPhone;
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
    public long save() {
        if (mPhone != null) mPhone.save();
        return super.save();
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "User #%1$s\nType: Company\nPhone: %2$s\nIs verify: %3$b\nAvatar url: %4$s\nAccess token: %5$s",
                mId == null ? "Null" : mId,
                mPhone == null ? "Null" : mPhone.toString(),
                mIsVerify,
                mAvatarUrl == null ? "Null" : mAvatarUrl,
                mAccessToken == null ? "Null" : mAccessToken);
    }

    public static class Builder {

        private String id;
        private String name;
        private String description;
        private String avatarUrl;
        private List<Integer> categories;
        private PhoneModel phone;
        private List<String> contactPhones;
        private String site;
        private List<CoordinatesModel> coordinates;
        private String accessToken;
        private String temporaryToken;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder categories(List<Integer> categories) {
            this.categories = categories;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
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
