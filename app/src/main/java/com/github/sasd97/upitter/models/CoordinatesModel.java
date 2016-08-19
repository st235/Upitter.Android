package com.github.sasd97.upitter.models;

import android.location.Address;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.sasd97.upitter.models.skeletons.RequestSkeleton;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 30.06.16.
 */
public class CoordinatesModel implements RequestSkeleton, Parcelable {

    @SerializedName("latitude")
    @Expose
    private double mLatitude;

    @SerializedName("longitude")
    @Expose
    private double mLongitude;

    @SerializedName("address")
    @Expose
    private String mAddressName;

    private Address mAddress;

    private CoordinatesModel(Builder builder) {
        mLatitude = builder.latitude;
        mLongitude = builder.longitude;
        mAddress = builder.address;
        mAddressName = builder.addressName;
        if (mAddress != null) mAddressName = mAddress.getAddressLine(0);
    }

    protected CoordinatesModel(Parcel in) {
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mAddressName = in.readString();
        mAddress = in.readParcelable(Address.class.getClassLoader());
        if (mAddress != null) mAddressName = mAddress.getAddressLine(0);
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public Address getAddress() {
        return mAddress;
    }

    public String getAddressName() {
        return mAddressName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        if (mAddressName == null) mAddressName = "";
        parcel.writeString(mAddressName);
        parcel.writeParcelable(mAddress, i);
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Coordinate of %3$s (%1$f, %2$f)",
                mLatitude,
                mLongitude,
                mAddressName == null ? "Unknown location" : mAddressName);
    }

    public static CoordinatesModel fromLocation(Location location) {
        return new Builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    public static class Builder {

        private double latitude;
        private double longitude;
        private Address address;
        private String addressName;

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder addressName(String addressName) {
            this.addressName = addressName;
            return this;
        }

        public CoordinatesModel build() {
            return new CoordinatesModel(this);
        }
    }

    public static final Creator<CoordinatesModel> CREATOR = new Creator<CoordinatesModel>() {
        @Override
        public CoordinatesModel createFromParcel(Parcel in) {
            return new CoordinatesModel(in);
        }

        @Override
        public CoordinatesModel[] newArray(int size) {
            return new CoordinatesModel[size];
        }
    };
}
