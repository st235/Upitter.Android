package com.github.sasd97.upitter.events;

import com.github.sasd97.upitter.models.BusinessUserModel;

/**
 * Created by Alexadner Dadukin on 01.07.2016.
 */
public interface OnBusinessRegistrationListener {
    void onBaseInfoPrepared(String temporaryToken, BusinessUserModel.Builder builder);
}