package com.github.sasd97.upitter.models;

/**
 * Created by alexander on 30.06.16.
 */
public class CoordinatesModel {

    private float mLatitude;
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
