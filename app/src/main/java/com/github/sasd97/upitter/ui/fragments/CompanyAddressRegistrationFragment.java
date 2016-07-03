package com.github.sasd97.upitter.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.constants.IntentKeysConstants;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;
import com.github.sasd97.upitter.services.GeocoderService;
import com.github.sasd97.upitter.services.query.CompanyAuthorizationQueryService;
import com.github.sasd97.upitter.ui.adapters.AddressRecyclerAdapter;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.base.BaseFragment;
import com.github.sasd97.upitter.ui.results.MapChooseActivity;

import java.util.ArrayList;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.CHOOSE_ON_MAP_POINT_REQUEST;

/**
 * Created by alexander on 28.06.16.
 */
public class CompanyAddressRegistrationFragment extends BaseFragment
        implements View.OnClickListener,
        GeocoderService.OnAddressListener,
        CompanyAuthorizationQueryService.OnCompanyAuthorizationListener {

    private CompanyModel.Builder companyModelBuilder;
    private CompanyAuthorizationQueryService queryService;

    private RecyclerView addressRecyclerView;
    private AddressRecyclerAdapter addressRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;

    private Button setPositionButton;
    private RelativeLayout addPositionRelativeLayout;

    public static CompanyAddressRegistrationFragment getFragment(@NonNull CompanyModel.Builder companyModelBuilder) {
        CompanyAddressRegistrationFragment fragment = new CompanyAddressRegistrationFragment();
        fragment.setCompanyBuilder(companyModelBuilder);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.company_registration_address_fragment, container, false);}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queryService = CompanyAuthorizationQueryService.getService(this);

        linearLayoutManager = new LinearLayoutManager(getContext());
        addressRecyclerAdapter = new AddressRecyclerAdapter(new ArrayList<CoordinatesModel>());
        addressRecyclerView.setLayoutManager(linearLayoutManager);
        addressRecyclerView.setAdapter(addressRecyclerAdapter);

        setPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFinishRegistration();
            }
        });

        addPositionRelativeLayout.setOnClickListener(this);
    }

    @Override
    protected void bindViews() {
        addressRecyclerView = findById(R.id.recycler_view_company_registration_address_fragment);
        addPositionRelativeLayout = findById(R.id.add_position_company_registration_address_fragment);
        setPositionButton = findById(R.id.set_position_business_registration_base_fragment);
    }

    public void onFinishRegistration() {
        companyModelBuilder.coordinates(addressRecyclerAdapter.getCoordinates());
        queryService.registerCompanyUser(companyModelBuilder);
    }

    private void setCompanyBuilder(CompanyModel.Builder builder) {
        this.companyModelBuilder = builder;
    }

    @Override
    public void onClick(View view) {
        startActivityForResult(new Intent(getActivity(), MapChooseActivity.class), CHOOSE_ON_MAP_POINT_REQUEST);
    }

    @Override
    public void onAddressReady(CoordinatesModel address) {
        addressRecyclerAdapter.addItem(address);
    }

    @Override
    public void onAddressFail() {

    }

    @Override
    public void onCodeObtained() {

    }

    @Override
    public void onSendCodeError() {

    }

    @Override
    public void onAuthorize(CompanyResponseModel companyResponseModel) {
        Log.d("RESPONSE", companyResponseModel.toString());
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
