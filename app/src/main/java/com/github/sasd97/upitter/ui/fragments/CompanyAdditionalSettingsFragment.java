package com.github.sasd97.upitter.ui.fragments;

import android.support.v7.widget.RecyclerView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by alexander on 06.08.16.
 */

public class CompanyAdditionalSettingsFragment extends BaseFragment {

    @BindView(R.id.phones_recyclerview_additional_settings) RecyclerView phonesRecyclerView;

    public CompanyAdditionalSettingsFragment() {
        super(R.layout.fragment_company_additional_settings);
    }

    public static CompanyAdditionalSettingsFragment getFragment() {
        return new CompanyAdditionalSettingsFragment();
    }

    @Override
    protected void setupViews() {

    }
}
