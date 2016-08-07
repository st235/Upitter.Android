package com.github.sasd97.upitter.holders;

import android.support.annotation.NonNull;
import android.util.Log;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.utils.Prefs;

import java.util.List;

/**
 * Created by alexander on 06.07.16.
 */

public class CompanyHolder extends UserHolder<CompanyModel> {

    private static final String TAG = "Company Holder";
    private static final int FIRST_POSITION = 0;

    private static final String USER_CUSTOM_ID = "USER_CUSTOM_ID";
    private static final String USER_CUSTOM_ID_DEFAULT = "0";
    private static final int USER_POSITION = 2;

    private CompanyHolder() {
        super();
    }

    public static CompanyHolder getHolder() {
        return new CompanyHolder();
    }

    @Override
    public void set(CompanyModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void restore() {
        String id = Prefs.get().getString(USER_CUSTOM_ID, USER_CUSTOM_ID_DEFAULT);
        List<CompanyModel> queryList = CompanyModel.find(CompanyModel.class, "m_id = ?", id);

        if (queryList != null && queryList.size() != 0) this.userModel = queryList.get(0);
        if (this.userModel != null) Log.d(TAG, this.userModel.toString());
        else Log.d(TAG, "User is null");
    }

    @Override
    public CompanyModel get() {
        return this.userModel;
    }

    @Override
    public void save(CompanyModel userModel) {
        super.save(userModel);
        Prefs.put(USER_CUSTOM_ID, userModel.getUId());

        List<CompanyModel> queryList = CompanyModel.find(CompanyModel.class, "m_id = ?", userModel.getUId());
        if (queryList == null || queryList.size() == 0) {
            userModel.save();
            return;
        }

        update(queryList.get(FIRST_POSITION), userModel);
    }

    @Override
    public void saveAvatar(@NonNull String path) {
        if (this.userModel == null) return;
        this.userModel.setAvatarUrl(path);
        this.userModel.save();
    }

    public void update(CompanyModel fromDB, CompanyModel fromServer) {
        fromServer.setId(fromDB.getId());
        fromServer.save();
    }
}
