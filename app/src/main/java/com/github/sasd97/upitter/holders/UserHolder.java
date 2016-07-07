package com.github.sasd97.upitter.holders;

import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.utils.Prefs;

/**
 * Created by Alexadner Dadukin on 02.07.2016.
 */

public abstract class UserHolder <T extends UserModel> {

    private static final String USER = "USER_AVAILABLE";
    private static final String USER_TYPE = "USER_TYPE";

    protected T userModel;

    protected UserHolder() {}

    public static UserHolder init() {
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

    public abstract void set(T userModel);
    public abstract void restore();
    public abstract T get();

    public  void save(T userModel) {
        this.userModel = userModel;

        Prefs.put(USER, true);
        Prefs.put(USER_TYPE, this.userModel.getType().getValue());
    }

    public void delete() {
        Prefs
                .edit()
                .clear()
                .apply();
    }
}
