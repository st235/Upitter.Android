package com.github.sasd97.upitter.ui.fragments;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.RequestCodesConstants;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.CountryModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.PhoneModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.services.GeocoderService;
import com.github.sasd97.upitter.services.LocationService;
import com.github.sasd97.upitter.services.query.CompanyAuthorizationQueryService;
import com.github.sasd97.upitter.ui.CodeVerificationActivity;
import com.github.sasd97.upitter.ui.results.CountryCodeSelectionResult;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.utils.Countries;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.watchers.DialCodeWatcher;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.RECEIVED_PHONE;

public class CompanyAuthorizationFragment extends BaseFragment
        implements View.OnClickListener,
        Countries.OnLoadListener,
        DialCodeWatcher.OnCountryReadyListener,
        LocationService.OnLocationListener,
        GeocoderService.OnAddressListener,
        CompanyAuthorizationQueryService.OnCompanyAuthorizationListener,
        View.OnKeyListener {

    @BindString(R.string.empty_field) String EMPTY_FIELD;
    @BindString(R.string.nothing_found_country_code_choose) String COUNTRY_NOT_VALID;

    private boolean notACountryCode = false;
    private LocationService locationService;
    private CompanyAuthorizationQueryService queryService;

    @BindView(R.id.country_display_company_login_fragment) TextView countryDisplayTextView;
    @BindView(R.id.continue_registration_button_company_login_fragment) Button continueRegistrationButton;
    @BindView(R.id.country_dial_code_company_login_fragment) MaterialEditText countryDialCodeEditText;
    @BindView(R.id.country_body_company_login_fragment) MaterialEditText countryBodyCodeEditText;

    private PhoneModel currentPhone;

    public CompanyAuthorizationFragment() {
        super(R.layout.fragment_company_authorization);
    }

    public static CompanyAuthorizationFragment getFragment() {
        return new CompanyAuthorizationFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setupViews() {
        locationService = LocationService.getService(this);
        queryService = CompanyAuthorizationQueryService.getService(this);

        Countries.obtainCountries(this);
        continueRegistrationButton.setOnClickListener(this);

        prepareCountryDisplay(countryDisplayTextView);

        countryDialCodeEditText.addTextChangedListener(new DialCodeWatcher(countryDialCodeEditText, this));

        countryDialCodeEditText.setOnKeyListener(this);
        countryBodyCodeEditText.setOnKeyListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!validateForms()) return;

        currentPhone = new PhoneModel.Builder()
                .dialCode(countryDialCodeEditText.getText().toString().replaceAll("[^\\d]", ""))
                .phoneBody(countryBodyCodeEditText.getText().toString())
                .build();

        queryService.obtainCodeRequest(currentPhone.getPhoneBody(),
                currentPhone.getDialCode());
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
        notACountryCode = false;
        countryDisplayTextView.setText(country.getName());
    }

    @Override
    public void onNotCountry() {
        notACountryCode = true;
        countryDisplayTextView.setText(COUNTRY_NOT_VALID);
        countryDialCodeEditText.setError("");
    }

    @Override
    public void onLocationFind(Location location) {
        onLocationChanged(location);
    }

    @Override
    public void onLocationChanged(Location location) {
        GeocoderService.find(getContext(), CoordinatesModel.fromLocation(location), this);
    }

    @Override
    public void onAddressReady(final CoordinatesModel address) {
        Log.d("ADDRESS_IS_NULL", address == null ? "NULL" : address.toString());

        CountryModel country = ListUtils.select(Countries.getCountries(), new ListUtils.OnListInteractionListener<CountryModel>() {
            @Override
            public boolean isFit(CountryModel other) {
                return other.getName().contains(address.getAddress().getCountryName());
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
    public void onAddressFail() {

    }

    @Override
    public void onCodeObtained() {
        Intent intent = new Intent(getActivity(), CodeVerificationActivity.class);
        intent.putExtra(RECEIVED_PHONE, currentPhone);
        startActivity(intent);
    }

    @Override
    public void onAuthorize(CompanyPointerModel companyPointerModel) {

    }

    @Override
    public void onRegister(String temporaryToken) {

    }

    @Override
    public void onError(ErrorModel errorModel) {
        Snackbar
                .make(getView(), getString(R.string.code_confirm_request_filed), Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onSendCodeError(int attemptsAmount) {
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            onClick(getView());
            return true;
        }

        return false;
    }

    private boolean validateForms() {
        if (notACountryCode) {
            countryDialCodeEditText.setError("");
            return false;
        }
        else if (countryDialCodeEditText.getText().length() == 0) {
            countryDialCodeEditText.setText(EMPTY_FIELD);
            return false;
        }
        if (countryBodyCodeEditText.getText().length() == 0) {
            countryBodyCodeEditText.setError(EMPTY_FIELD);
            return false;
        }

        return true;
    }

    public void prepareCountryDisplay(TextView display) {
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), CountryCodeSelectionResult.class),
                        RequestCodesConstants.COUNTRY_CHOOSE_LIST_REQUEST);
            }
        });
        display.setClickable(false);
    }
}
