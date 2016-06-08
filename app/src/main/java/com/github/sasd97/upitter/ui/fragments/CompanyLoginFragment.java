package com.github.sasd97.upitter.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseFragment;

public class CompanyLoginFragment extends BaseFragment {

    public static CompanyLoginFragment getFragment() {
        return new CompanyLoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.company_login_fragment, container, false);
    }
}
