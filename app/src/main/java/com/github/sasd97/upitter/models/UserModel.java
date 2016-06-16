package com.github.sasd97.upitter.models;

import com.facebook.AccessToken;

/**
 * Created by Alex on 16.06.2016.
 */
public class UserModel {

    private String mId;
    private String mName;
    private String mSurname;
    private String mNickname;
    private String mDescription;
    private String mEmail;
    private String mAvatarUrl;
    private String mAccessToken;
    private int mSex;
    private boolean mIsVerify;

    private UserModel(Builder builder) {
        mId = builder.id;
        mName = builder.name;
        mSurname = builder.surname;
        mNickname = builder.nickname;
        mDescription = builder.description;
        mEmail = builder.email;
        mAvatarUrl = builder.avatarUrl;
        mAccessToken = builder.accessToken;
        mSex = builder.sex;
        mIsVerify = builder.isVerify;
    }

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
        return mAccessToken;
    }

    public boolean isVerify() {
        return mIsVerify;
    }

    public static class Builder {

        private String id;
        private String name;
        private String surname;
        private String nickname;
        private String description;
        private String email;
        private String avatarUrl;
        private String accessToken;
        private int sex = 2;
        private boolean isVerify = false;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder sex(int sex) {
            this.sex = sex;
            return this;
        }

        public Builder isVerify(boolean isVerify) {
            this.id = id;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
        }
    }
}
