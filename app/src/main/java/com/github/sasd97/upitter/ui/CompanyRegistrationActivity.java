package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.OnCompanyRegistrationListener;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.PhoneModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.fragments.CompanyAddressRegistrationFragment;
import com.github.sasd97.upitter.ui.fragments.CompanyBaseRegistrationFragment;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.RECEIVED_PHONE;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.RECEIVED_TEMPORARY_TOKEN;

public class CompanyRegistrationActivity extends BaseActivity
    implements OnCompanyRegistrationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));
    }

    @Override
    protected void setupViews() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_holder_business_registration_activity, CompanyBaseRegistrationFragment.getFragment(this))
                .commit();
    }

    @Override
    public void onBaseInfoPrepared(@NonNull CompanyModel.Builder builder) {
        String temporaryToken = getIntent().getStringExtra(RECEIVED_TEMPORARY_TOKEN);
        PhoneModel phoneModel = getIntent().getParcelableExtra(RECEIVED_PHONE);

        builder
                .temporaryToken(temporaryToken)
                .phone(phoneModel);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder_business_registration_activity, CompanyAddressRegistrationFragment.getFragment(builder))
                .addToBackStack(null)
                .commit();
    }
}
