package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.IntentKeysConstants;
import com.github.sasd97.upitter.holders.CompanyHolder;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.services.GeocoderService;
import com.github.sasd97.upitter.services.query.CompanyAuthorizationQueryService;
import com.github.sasd97.upitter.ui.CompanyFeedActivity;
import com.github.sasd97.upitter.ui.adapters.recyclers.CompanyAddressListRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.CompanyCoordinatesSelectionResult;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.CHOOSE_ON_MAP_POINT_REQUEST;
import static com.github.sasd97.upitter.Upitter.*;

/**
 * Created by alexander on 28.06.16.
 */

public class CompanyAdditionalRegistrationFragment extends BaseFragment
        implements View.OnClickListener,
        GeocoderService.OnAddressListener,
        CompanyAuthorizationQueryService.OnCompanyAuthorizationListener {

    private CompanyModel.Builder companyModelBuilder;
    private CompanyAuthorizationQueryService queryService;
    private CompanyAddressListRecycler companyAddressListRecycler;

    @BindView(R.id.set_position_business_registration_base_fragment) Button setPositionButton;
    @BindView(R.id.recycler_view_company_registration_address_fragment) RecyclerView addressRecyclerView;
    @BindView(R.id.add_position_company_registration_address_fragment) RelativeLayout addPositionRelativeLayout;

    public CompanyAdditionalRegistrationFragment() {
        super(R.layout.fragment_company_additional_registration);
    }

    public static CompanyAdditionalRegistrationFragment getFragment(@NonNull CompanyModel.Builder companyModelBuilder) {
        CompanyAdditionalRegistrationFragment fragment = new CompanyAdditionalRegistrationFragment();
        fragment.setCompanyBuilder(companyModelBuilder);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setupViews() {
        queryService = CompanyAuthorizationQueryService.getService(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        companyAddressListRecycler = new CompanyAddressListRecycler(new ArrayList<CoordinatesModel>());
        addressRecyclerView.setLayoutManager(linearLayoutManager);
        addressRecyclerView.setAdapter(companyAddressListRecycler);

        setPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFinishRegistration();
            }
        });

        addPositionRelativeLayout.setOnClickListener(this);
    }

    public void onFinishRegistration() {
        companyModelBuilder.coordinates(companyAddressListRecycler.getCoordinates());
        queryService.registerCompanyUser(companyModelBuilder);
    }

    private void setCompanyBuilder(CompanyModel.Builder builder) {
        this.companyModelBuilder = builder;
    }

    @Override
    public void onClick(View view) {
        startActivityForResult(new Intent(getActivity(), CompanyCoordinatesSelectionResult.class),
                CHOOSE_ON_MAP_POINT_REQUEST);
    }

    @Override
    public void onAddressReady(CoordinatesModel address) {
        companyAddressListRecycler.addItem(address);
    }

    @Override
    public void onAddressFail() {

    }

    @Override
    public void onCodeObtained() {

    }

    @Override
    public void onSendCodeError(int attemptsAmount) {

    }

    @Override
    public void onError(ErrorModel errorModel) {
        Log.d("ERROR_FALLING", errorModel.toString());

    }

    @Override
    public void onAuthorize(CompanyPointerModel companyPointerModel) {
        setHolder(CompanyHolder.getHolder());
        Logger.i(companyPointerModel.toString());

        CompanyModel companyModel = new CompanyModel
                .Builder()
                .id(companyPointerModel.getId())
                .name(companyPointerModel.getName())
                .alias(companyPointerModel.getAlias())
                .isVerify(companyPointerModel.isVerify())
                .avatarUrl(companyPointerModel.getLogoUrl())
                .description(companyPointerModel.getDescription())
                .categories(companyPointerModel.getActivity())
                .contactPhones(companyPointerModel.getContactPhones())
                .site(companyPointerModel.getSite())
                .socialIcons(companyPointerModel.getSocialLinksBoxed())
                .coordinates(companyPointerModel.getCoordinates())
                .accessToken(companyPointerModel.getAccessToken())
                .build();

        ((CompanyHolder) getHolder()).save(companyModel);
        startActivity(new Intent(getContext(), CompanyFeedActivity.class));
        getActivity().finish();
    }

    @Override
    public void onRegister(String temporaryToken) {

    }

    private void handleCoordinates(Intent data) {
        CoordinatesModel coordinatesModel = data.getParcelableExtra(IntentKeysConstants.COORDINATES_ATTACH);
        GeocoderService.find(getContext(), coordinatesModel, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != BaseActivity.RESULT_OK) return;
        if (requestCode == CHOOSE_ON_MAP_POINT_REQUEST) handleCoordinates(data);
    }
}
