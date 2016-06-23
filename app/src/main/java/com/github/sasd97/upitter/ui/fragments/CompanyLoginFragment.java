package com.github.sasd97.upitter.ui.fragments;


import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.RequestCodesConstants;
import com.github.sasd97.upitter.models.CountryModel;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.services.query.BusinessAuthorizationQueryService;
import com.github.sasd97.upitter.ui.CodeConfirmActivity;
import com.github.sasd97.upitter.ui.result.CountryCodeChooseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Countries;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.watchers.DialCodeWatcher;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Locale;

public class CompanyLoginFragment extends BaseFragment
        implements View.OnClickListener,
        Countries.OnLoadListener,
        DialCodeWatcher.OnCountryReadyListener,
        LocationService.OnLocationListener,
        BusinessAuthorizationQueryService.onBusinessAuthorizationListener {

    private String COUNTRY_NOT_VALID;

    private LocationService locationService;
    private BusinessAuthorizationQueryService queryService;

    private TextView countryDisplayTextView;
    private Button continueRegistrationButton;
    private MaterialEditText countryDialCodeEditText;
    private MaterialEditText countryBodyCodeEditText;

    public static CompanyLoginFragment getFragment() {
        return new CompanyLoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        locationService = LocationService.getService(this);
        queryService = BusinessAuthorizationQueryService.getService(this);
        return inflater.inflate(R.layout.company_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        COUNTRY_NOT_VALID = getString(R.string.nothing_found_country_code_choose);

        Countries.obtainCountries(this);
        continueRegistrationButton.setOnClickListener(this);

        prepareCountryDisplay(countryDisplayTextView);

        countryDialCodeEditText.addTextChangedListener(new DialCodeWatcher(countryDialCodeEditText, this));
    }

    @Override
    protected void bindViews() {
        continueRegistrationButton = findById(R.id.continue_registration_button_company_login_fragment);
        countryDisplayTextView = findById(R.id.country_display_company_login_fragment);
        countryDialCodeEditText = findById(R.id.country_dial_code_company_login_fragment);
        countryBodyCodeEditText = findById(R.id.country_body_company_login_fragment);
    }

    @Override
    public void onClick(View view) {
        queryService.sendCodeRequest(countryBodyCodeEditText.getText().toString(),
                countryDialCodeEditText.getText().toString().replaceAll("[^\\d]", ""));
    }

    @Override
    public void onLoad(ArrayList<CountryModel> list) {
        locationService.init(getContext());
        countryDisplayTextView.setClickable(true);
        countryDialCodeEditText.setEnabled(true);
        countryBodyCodeEditText.setEnabled(true);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onCountryReady(CountryModel country) {
        countryDisplayTextView.setText(country.getName());
    }

    @Override
    public void onNotCountry() {
        countryDisplayTextView.setText(COUNTRY_NOT_VALID);
        countryDialCodeEditText.setError("");
    }

    @Override
    public void onLocationFind(Location location) {
        onLocationChanged(location);
    }

    @Override
    public void onLocationChanged(Location location) {
        locationService.getAddress(location);
    }

    @Override
    public void onAddressReady(final Address address) {
        CountryModel country = ListUtils.select(Countries.getCountries(), new ListUtils.OnSelectListener<CountryModel>() {
            @Override
            public boolean isSelectable(CountryModel other) {
                return other.getName().contains(address.getCountryName());
            }
        });

        if (country == null) {
            onNotCountry();
            return;
        }

        countryDisplayTextView.setText(country.getName());
        countryDialCodeEditText.setText(String.format(Locale.getDefault(), "+%1$s", country.getDialCode()));
    }

    @Override
    public void onCodeObtained() {
        startActivity(new Intent(getActivity(), CodeConfirmActivity.class));
    }

    @Override
    public void onSendCodeError() {
        Snackbar
                .make(getView(), getString(R.string.code_confirm_request_filed), Snackbar.LENGTH_SHORT)
                .show();
    }

    public void prepareCountryDisplay(TextView display) {
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), CountryCodeChooseActivity.class),
                        RequestCodesConstants.COUNTRY_CHOOSE_LIST_REQUEST);
            }
        });
        display.setClickable(false);
    }
}
