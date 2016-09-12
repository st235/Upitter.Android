package com.github.sasd97.upitter.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Created by alexander on 12.09.16.
 */
public class SocialIconModel implements Parcelable {

    private String mTitle;
    private String mIcon;
    private String mLink;

    private SocialIconModel(Builder builder) {
        mTitle = builder.title;
        mIcon = builder.icon;
        mLink = builder.link;
    }

    protected SocialIconModel(Parcel in) {
        mTitle = in.readString();
        mIcon = in.readString();
        mLink = in.readString();
    }

    public static final Creator<SocialIconModel> CREATOR = new Creator<SocialIconModel>() {
        @Override
        public SocialIconModel createFromParcel(Parcel in) {
            return new SocialIconModel(in);
        }

        @Override
        public SocialIconModel[] newArray(int size) {
            return new SocialIconModel[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getIcon() {
        return mIcon;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(@NonNull String link) {
        mLink = link;
    }

    public boolean isIcon() {
        return mIcon != null && !mIcon.trim().equalsIgnoreCase("");
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Social Icon\nTitle: %1$s\nAvatar: %2$s",
                mTitle,
                mIcon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mIcon);
        parcel.writeString(mLink);
    }

    public static class Builder {

        private String title;
        private String icon;
        private String link;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public SocialIconModel build() {
            return new SocialIconModel(this);
        }
    }
}
