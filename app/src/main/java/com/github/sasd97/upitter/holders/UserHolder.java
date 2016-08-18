package com.github.sasd97.upitter.holders;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.utils.Prefs;
import com.orhanobut.logger.Logger;
import com.twitter.sdk.android.core.models.User;

/**
 * Created by Alexadner Dadukin on 02.07.2016.
 */

public abstract class UserHolder <T extends UserModel> {

    private static final String USER = "USER_AVAILABLE";
    private static final String USER_TYPE = "USER_TYPE";
    private static final String USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN";

    protected T userModel;

    protected UserHolder() {}

    public static UserHolder init() {
        Logger.d(String.valueOf(isUserAvailable()));
        Logger.d(String.valueOf(Prefs.get().getInt(USER_TYPE, 0)));

        if (!isUserAvailable()) return null;
        if (Prefs.get().getInt(USER_TYPE, 0) == UserModel.UserType.People.getValue()) return PeopleHolder.getHolder();
        return null;
    }

    public static boolean isUserAvailable() {
        return Prefs.get().getBoolean(USER, false);
    }

    public static int getUserType() {
        return Prefs.get().getInt(USER_TYPE, 0);
    }

    public static String getAccessToken() {
        return Prefs.get().getString(USER_ACCESS_TOKEN, "");
    }

    public abstract void set(T userModel);
    public abstract void restore();
    public abstract T get();
    public abstract void saveAvatar(@NonNull String path);

    public  void save(T userModel) {
        this.userModel = userModel;

        Prefs.put(USER, true);
        Prefs.put(USER_TYPE, this.userModel.getType().getValue());
        Prefs.put(USER_ACCESS_TOKEN, this.userModel.getAccessToken());
    }

    public void delete() {
        Prefs
                .edit()
                .clear()
                .apply();
    }
}
