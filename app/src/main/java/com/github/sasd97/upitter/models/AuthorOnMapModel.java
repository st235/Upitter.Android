package com.github.sasd97.upitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by alexander on 21.07.16.
 */
public class AuthorOnMapModel implements Parcelable {

    private String mAuthorName;
    private String mAuthorAvatarUrl;
    private double mLatitude;
    private double mLongitude;

    private AuthorOnMapModel(Builder builder) {
        mAuthorName = builder.authorName;
        mAuthorAvatarUrl = builder.authorAvatarUrl;
        mLatitude = builder.latitude;
        mLongitude = builder.longitude;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getAuthorAvatarUrl() {
        return mAuthorAvatarUrl;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public boolean isAvatar() {
        return mAuthorAvatarUrl != null;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$s (%2$s)\n(%3$f;%4$f)\n",
                mAuthorName,
                mAuthorAvatarUrl,
                mLatitude,
                mLongitude);
    }

    public static class Builder {
        
        private String authorName;
        private String authorAvatarUrl;
        private double latitude;
        private double longitude;

        public Builder authorName(String authorName) {
            this.authorName = authorName;
            return this;
        }    
        
        public Builder authorAvatarUrl(String authorAvatarUrl) {
            this.authorAvatarUrl = authorAvatarUrl;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public AuthorOnMapModel build() {
            return new AuthorOnMapModel(this);
        }
    }

    protected AuthorOnMapModel(Parcel in) {
        mAuthorName = in.readString();
        mAuthorAvatarUrl = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthorName);
        dest.writeString(mAuthorAvatarUrl);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuthorOnMapModel> CREATOR = new Creator<AuthorOnMapModel>() {
        @Override
        public AuthorOnMapModel createFromParcel(Parcel in) {
            return new AuthorOnMapModel(in);
        }

        @Override
        public AuthorOnMapModel[] newArray(int size) {
            return new AuthorOnMapModel[size];
        }
    };
}
