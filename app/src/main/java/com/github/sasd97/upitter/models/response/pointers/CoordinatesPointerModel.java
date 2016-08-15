package com.github.sasd97.upitter.models.response.pointers;

import com.github.sasd97.upitter.models.CoordinatesModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 30.06.16.
 */

public class CoordinatesPointerModel {

    @SerializedName("latitude")
    @Expose
    private double mLatitude;

    @SerializedName("longitude")
    @Expose
    private double mLongitude;

    @SerializedName("address")
    @Expose
    private String mAddress;

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getAddress() {
        return mAddress;
    }

    public CoordinatesModel toCoordinatesModel() {
        return new CoordinatesModel
                .Builder()
                .latitude(mLatitude)
                .longitude(mLongitude)
                .addressName(mAddress)
                .build();
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Coordinate (%1$f, %2$f)",
                mLatitude,
                mLongitude);
    }
}
