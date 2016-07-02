package com.github.sasd97.upitter.models;

import com.github.sasd97.upitter.models.skeletons.RequestSkeleton;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexander on 30.06.16.
 */
public class CoordinatesModel implements RequestSkeleton {

    @SerializedName("latitude")
    @Expose
    private float mLatitude;

    @SerializedName("longitude")
    @Expose
    private float mLongitude;

    private CoordinatesModel(Builder builder) {
        mLatitude = builder.latitude;
        mLongitude = builder.longitude;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

    public static class Builder {

        private float latitude;
        private float longitude;

        public Builder latitude(float latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(float longitude) {
            this.longitude = longitude;
            return this;
        }

        public CoordinatesModel build() {
            return new CoordinatesModel(this);
        }
    }
}
