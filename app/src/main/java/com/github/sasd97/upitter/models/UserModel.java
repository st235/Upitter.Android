package com.github.sasd97.upitter.models;

import com.orm.SugarRecord;

/**
 * Created by Alexadner Dadukin on 02.07.2016.
 */
public abstract class UserModel extends SugarRecord {

    public enum UserType {
        People,
        Company
    }

    public abstract String getUId();
    public abstract String getName();
    public abstract String getAvatarUrl();
    public abstract String getDescription();
    public abstract String getAccessToken();
    public abstract boolean isVerify();

    public abstract UserType getType();
}
