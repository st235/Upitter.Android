package com.github.sasd97.upitter.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.github.sasd97.upitter.models.skeletons.RequestSkeleton;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by alexander on 12.09.16.
 */
public class SocialIconModel implements RequestSkeleton, Parcelable {

    @SerializedName("customId")
    @Expose
    private String mId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("icon")
    @Expose
    private String mIcon;

    @SerializedName("link")
    @Expose
    private String mLink;

    private SocialIconModel(Builder builder) {
        mId = builder.id;
        mTitle = builder.title;
        mIcon = builder.icon;
        mLink = builder.link;
    }

    protected SocialIconModel(Parcel in) {
        mId = in.readString();
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

    public String getId() {
        return mId;
    }

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
    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Social Icon\nTitle: %1$s\nAvatar: %2$s\nLink %3$s",
                mTitle,
                mIcon,
                mLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mIcon);
        parcel.writeString(mLink);
    }

    public static class Builder {

        private String id;
        private String title;
        private String icon;
        private String link;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

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
