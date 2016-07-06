package com.github.sasd97.upitter.holders;

import com.github.sasd97.upitter.models.CompanyModel;

/**
 * Created by alexander on 06.07.16.
 */
public class CompanyHolder extends UserHolder<CompanyModel> {

    private CompanyHolder() {
        super();
    }

    private static CompanyHolder getHolder() {
        return new CompanyHolder();
    }

    @Override
    public void set(CompanyModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void restore() {

    }

    @Override
    public CompanyModel get() {
        return this.userModel;
    }

    @Override
    public void save(CompanyModel userModel) {
        super.save(userModel);
    }

    @Override
    public void delete() {
        super.delete();
    }
}
