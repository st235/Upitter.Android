package com.github.sasd97.upitter.models.response.user;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by Alex on 16.06.2016.
 */
public class UserResponseModel extends BaseResponseModel {

    @SerializedName("customId")
    @Expose
    private String mId;

    @SerializedName("nickname")
    @Expose
    private String mNickname;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("surname")
    @Expose
    private String mSurname;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("email")
    @Expose
    private String mEmail;

    @SerializedName("sex")
    @Expose
    private int mSex = 2;

    @SerializedName("picture")
    @Expose
    private String mAvatarUrl;

    @SerializedName("isVerify")
    @Expose
    private boolean mIsVerify = false;

    @SerializedName("accessToken")
    @Expose
    private String mToken;

    @SerializedName("subscriptions")
    @Expose
    private List<String> mSubscriptions;

    public String getId() {
        return mId;
    }

    public String getNickname() {
        return mNickname;
    }

    public String getName() {
        return mName;
    }

    public String getSurname() {
        return mSurname;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getEmail() {
        return mEmail;
    }

    public int getSex() {
        return mSex;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public String getToken() {
        return mToken;
    }

    public boolean isVerify() {
        return mIsVerify;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "User #%1$s\n%2$s (%3$s) %4$s\nAvatarUrl: %5$s\nEmail: %6$s\nSex: %7$d\nToken: %8$s\nVerify state %9$b",
                mId == null ? "Null" : mId,
                mName == null ? "Null" : mName,
                mNickname == null ? "Null" : mNickname,
                mSurname == null ? "Null" : mSurname,
                mAvatarUrl == null ? "Null" : mAvatarUrl,
                mEmail == null ? "Null" : mEmail,
                mSex,
                mToken == null ? "Null" : mToken,
                mIsVerify);
    }
}
