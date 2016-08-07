package com.github.sasd97.upitter.holders;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.models.PeopleModel;
import com.github.sasd97.upitter.utils.Prefs;

/**
 * Created by Alexander Dadukin on 01.05.2016.
 */

public class PeopleHolder extends UserHolder<PeopleModel> {

    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_SURNAME = "USER_SURNAME";
    private static final String USER_NICKNAME = "USER_NICKNAME";
    private static final String USER_DESCRIPTION = "USER_DESCRIPTION";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_AVATAR_URL = "USER_AVATAR_URL";
    private static final String USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN";
    private static final String USER_SEX = "USER_SEX";
    private static final String USER_IS_VERIFY = "USER_IS_VERIFY";

    private PeopleHolder() {
        super();
    }

    public static PeopleHolder getHolder() {
        return new PeopleHolder();
    }

    @Override
    public void set(PeopleModel model) {
        this.userModel = model;
    }

    @Override
    public void save(PeopleModel userModel) {
        super.save(userModel);
        
        Prefs.put(USER_ID, this.userModel.getUId());
        Prefs.put(USER_NAME, this.userModel.getName());
        Prefs.put(USER_SURNAME, this.userModel.getSurname());
        Prefs.put(USER_NICKNAME, this.userModel.getNickname());
        Prefs.put(USER_AVATAR_URL, this.userModel.getAvatarUrl());
        Prefs.put(USER_DESCRIPTION, this.userModel.getDescription());
        Prefs.put(USER_EMAIL, this.userModel.getEmail());
        Prefs.put(USER_ACCESS_TOKEN, this.userModel.getAccessToken());
        Prefs.put(USER_SEX, this.userModel.getSex());
        Prefs.put(USER_IS_VERIFY, this.userModel.isVerify());
    }

    @Override
    public void saveAvatar(@NonNull String path) {
        Prefs.put(USER_AVATAR_URL, path);
    }

    @Override
    public PeopleModel get() {
        if (this.userModel == null) {
            if (isUserAvailable()) restore();
            else throw new NullPointerException("There is no stored users");
        }
        return this.userModel;
    }

    @Override
    public void restore() {
        this.userModel = new PeopleModel.Builder()
                            .id(Prefs.get().getString(USER_ID, null))
                            .name(Prefs.get().getString(USER_NAME, null))
                            .surname(Prefs.get().getString(USER_SURNAME, null))
                            .nickname(Prefs.get().getString(USER_NICKNAME, null))
                            .avatarUrl(Prefs.get().getString(USER_AVATAR_URL, null))
                            .description(Prefs.get().getString(USER_DESCRIPTION, null))
                            .email(Prefs.get().getString(USER_EMAIL, null))
                            .accessToken(Prefs.get().getString(USER_ACCESS_TOKEN, null))
                            .sex(Prefs.get().getInt(USER_SEX, 2))
                            .isVerify(Prefs.get().getBoolean(USER_IS_VERIFY, false))
                            .build();
    }
}
