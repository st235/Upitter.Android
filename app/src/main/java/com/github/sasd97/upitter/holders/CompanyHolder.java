package com.github.sasd97.upitter.holders;

import android.util.Log;

import com.github.sasd97.upitter.models.CompanyModel;

/**
 * Created by alexander on 06.07.16.
 */
public class CompanyHolder extends UserHolder<CompanyModel> {

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
        this.userModel = CompanyModel.findById(CompanyModel.class, 1);
        if (this.userModel != null) Log.d("RESTORED", this.userModel.toString());
    }

    @Override
    public CompanyModel get() {
        return this.userModel;
    }

    @Override
    public void save(CompanyModel userModel) {
        super.save(userModel);
        userModel.save();
    }

    @Override
    public void delete() {
        super.delete();
        this.userModel.delete();
    }
}
