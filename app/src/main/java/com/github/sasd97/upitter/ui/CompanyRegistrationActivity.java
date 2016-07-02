package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.OnCompanyRegistrationListener;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.fragments.CompanyBaseRegistrationFragment;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

public class CompanyRegistrationActivity extends BaseActivity
    implements OnCompanyRegistrationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_registration_activity);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_holder_business_registration_activity, CompanyBaseRegistrationFragment.getFragment(this))
                .commit();
    }

    @Override
    protected void bindViews() {}

    @Override
    public void onBaseInfoPrepared(String temporaryToken, CompanyModel.Builder builder) {

    }
}
