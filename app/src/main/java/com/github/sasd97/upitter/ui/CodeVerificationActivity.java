package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.github.sasd97.upitter.BuildConfig;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.receivers.RequestCodeReceiver;
import com.github.sasd97.upitter.holders.CompanyHolder;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.PhoneModel;
import com.github.sasd97.upitter.models.SmsModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.services.query.CompanyAuthorizationQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.Upitter.setHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CODE_RECEIVER_INTENT_NAME;

public class CodeVerificationActivity extends BaseActivity implements
        RequestCodeReceiver.OnRequestCodeReceiveListener,
        CompanyAuthorizationQueryService.OnCompanyAuthorizationListener {

    private final static String TAG = "Code Verification";

    private final String UPITTER_SMS_HEADER = "999999";

    private PhoneModel currentPhone;
    private RequestCodeReceiver requestCodeReceiver;
    private CompanyAuthorizationQueryService queryService;

    @BindString(R.string.wrong_request_code_confirm_activity) String WRONG_REQUEST_CODE;
    @BindView(R.id.request_code_label_code_confirm_activity) MaterialEditText requestCodeEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));
    }

    @Override
    protected void setupViews() {
        currentPhone = getIntent().getParcelableExtra(RECEIVED_PHONE);
        queryService = CompanyAuthorizationQueryService.getService(this);
        requestCodeReceiver = RequestCodeReceiver.getReceiver(this);

        if (BuildConfig.DEBUG) requestCodeEdt.setText("615243");
    }

    public void onLoginClick(View v) {
        if (!validateForms()) return;
        setRequestCode(requestCodeEdt.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(requestCodeReceiver, new IntentFilter(CODE_RECEIVER_INTENT_NAME));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (requestCodeReceiver != null)
            unregisterReceiver(requestCodeReceiver);
    }

    @Override
    public void onCodeObtained() {

    }

    @Override
    public void onSendCodeError(int attempts) {
        final String attemptsCounter = String.format(Locale.getDefault(), "%1$s %2$d %3$s",
                getString(R.string.attempts_number_company_registration_activity),
                attempts,
                getString(R.string.attempts_of_company_registration_activity));

        Snackbar
                .make(getRootView(), attemptsCounter, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onError(ErrorModel error) {
        Snackbar
                .make(getRootView(), "Error", Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onAuthorize(CompanyPointerModel companyPointerModel) {
        setHolder(CompanyHolder.getHolder());
        Logger.d(companyPointerModel.toString());

        CompanyModel companyModel = new CompanyModel
                .Builder()
                .id(companyPointerModel.getId())
                .name(companyPointerModel.getName())
                .alias(companyPointerModel.getAlias())
                .isVerify(companyPointerModel.isVerify())
                .avatarUrl(companyPointerModel.getLogoUrl())
                .description(companyPointerModel.getDescription())
                .categories(companyPointerModel.getActivity())
                .phone(currentPhone)
                .contactPhones(companyPointerModel.getContactPhones())
                .site(companyPointerModel.getSite())
                .coordinates(companyPointerModel.getCoordinates())
                .accessToken(companyPointerModel.getAccessToken())
                .build();

        ((CompanyHolder) getHolder()).save(companyModel);
        startActivity(new Intent(this, CompanyFeedActivity.class));
        finish();
    }

    @Override
    public void onRegister(String temporaryToken) {
        Intent intent = new Intent(this, CompanyRegistrationActivity.class);
        intent.putExtra(RECEIVED_TEMPORARY_TOKEN, temporaryToken);
        intent.putExtra(RECEIVED_PHONE, currentPhone);
        startActivity(intent);
        finish();
    }

    @Override
    public void onReceiveRequestCode(ArrayList<SmsModel> messages) {
        for (SmsModel sms: messages) {
            if (sms.getAuthor().equalsIgnoreCase(UPITTER_SMS_HEADER)) {
                setRequestCode(sms.getBody().replaceAll("[^\\d]", ""));
                return;
            }
        }
    }

    private void setRequestCode(@NonNull String requestCode) {
        requestCodeEdt.setText(requestCode);

        if (BuildConfig.DEBUG) {
            queryService.debugRequestCode(
                    currentPhone.getPhoneBody(),
                    currentPhone.getDialCode(),
                    requestCode);
            return;
        }

        queryService.sendRequestCode(
                currentPhone.getPhoneBody(),
                currentPhone.getDialCode(),
                requestCode);
    }

    private boolean validateForms() {
        if (requestCodeEdt.getText().length() == 0) {
            requestCodeEdt.setError(WRONG_REQUEST_CODE);
            return false;
        }

        return true;
    }
}
