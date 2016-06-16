package com.github.sasd97.upitter.holders;

import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.utils.Prefs;

/**
 * Created by Alexander Dadukin on 01.05.2016.
 */

public class UserHolder {

    private static final String USER = "USER_AVAILABLE";
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

    private static UserModel userModel = null;

    public static void save(UserModel user) {
        userModel = user;
        Prefs.put(USER, true);
        Prefs.put(USER_ID, userModel.getId());
        Prefs.put(USER_NAME, userModel.getName());
        Prefs.put(USER_SURNAME, userModel.getSurname());
        Prefs.put(USER_NICKNAME, userModel.getNickname());
        Prefs.put(USER_AVATAR_URL, userModel.getAvatarUrl());
        Prefs.put(USER_DESCRIPTION, userModel.getDescription());
        Prefs.put(USER_EMAIL, userModel.getEmail());
        Prefs.put(USER_AVATAR_URL, userModel.getAvatarUrl());
        Prefs.put(USER_ACCESS_TOKEN, userModel.getToken());
        Prefs.put(USER_SEX, userModel.getSex());
        Prefs.put(USER_IS_VERIFY, userModel.isVerify());
    }

    public static boolean isUserCreate() {
        return Prefs.get().getBoolean(USER, false);
    }

    public static void set(UserModel model) {
        userModel = model;
    }

    public static UserModel get() {
        if (userModel == null)
            if (Prefs.get().contains(USER)) restore();
            else throw new NullPointerException("There is no stored users");
        return userModel;
    }

    public static void restore() {
        userModel = new UserModel.Builder()
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

    public static void delete() {
        Prefs
                .edit()
                .clear()
                .apply();
    }
}
