package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.ui.fragments.BaseBusinessRegistrationFragment;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

public class BusinessRegistrationActivity extends BaseActivity {

    public interface OnBusinessRegistrationListener {
        void onBaseInfoPrepared();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_registration_activity);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_holder_business_registration_activity, BaseBusinessRegistrationFragment.getFragment())
                .commit();
    }

    @Override
    protected void bindViews() {}
}
