package com.github.sasd97.upitter.events;

import com.github.sasd97.upitter.models.CompanyModel;

/**
 * Created by Alexadner Dadukin on 01.07.2016.
 */
public interface OnCompanyRegistrationListener {
    void onBaseInfoPrepared(String temporaryToken, CompanyModel.Builder builder);
}