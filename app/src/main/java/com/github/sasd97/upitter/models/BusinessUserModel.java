package com.github.sasd97.upitter.models;

import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.PhoneModel;
import com.github.sasd97.upitter.models.response.BaseResponseModel;

import java.util.List;

/**
 * Created by Alexadner Dadukin on 30.06.2016.
 */
public class BusinessUserModel extends BaseResponseModel {

    private String mName;
    private String mDescription;
    private int mCategory;
    private PhoneModel mPhone;
    private List<String> mContactPhones;
    private String mSite;
    private List<CoordinatesModel> mCoordinates;
    private String mAccessToken;

    private BusinessUserModel(Builder builder) {
        mName = builder.name;
        mDescription = builder.description;
        mCategory = builder.category;
        mPhone = builder.phone;
        mContactPhones = builder.contactPhones;
        mSite = builder.site;
        mCoordinates = builder.coordinates;
        mAccessToken = builder.accessToken;
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

        public BusinessUserModel build() {
            return new BusinessUserModel(this);
        }
    }
}
