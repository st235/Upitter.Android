package com.github.sasd97.upitter.models.response.coordinates;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 30.06.16.
 */
public class CoordinatesResponseModel extends BaseResponseModel {

    @SerializedName("latitude")
    @Expose
    private float mLatitude;

    @SerializedName("longitude")
    @Expose
    private float mLongitude;

    public float getLatitude() {
        return mLatitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Coordinate (%1$f, %2$f)",
                mLatitude,
                mLongitude);
    }
}
