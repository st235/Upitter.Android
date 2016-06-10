package com.github.sasd97.upitter.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.CountryCodeChooseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;

public class CompanyLoginFragment extends BaseFragment implements View.OnClickListener {

    private Button continueRegistrationButton;

    public static CompanyLoginFragment getFragment() {
        return new CompanyLoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.company_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        continueRegistrationButton.setOnClickListener(this);
    }

    @Override
    protected void bindViews() {
        continueRegistrationButton = findById(R.id.continue_registration_button_company_login_fragment);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CountryCodeChooseActivity.class);
        getActivity().startActivity(intent);
    }
}
