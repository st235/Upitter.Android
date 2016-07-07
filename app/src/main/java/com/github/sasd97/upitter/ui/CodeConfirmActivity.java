package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.receivers.RequestCodeReceiver;
import com.github.sasd97.upitter.holders.CompanyHolder;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.PhoneModel;
import com.github.sasd97.upitter.models.SmsModel;
import com.github.sasd97.upitter.models.response.company.CompanyResponseModel;
import com.github.sasd97.upitter.services.query.CompanyAuthorizationQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Locale;

import static com.github.sasd97.upitter.Upitter.getHolder;
import static com.github.sasd97.upitter.Upitter.setHolder;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CODE_RECEIVER_INTENT_NAME;

public class CodeConfirmActivity extends BaseActivity implements
        RequestCodeReceiver.OnRequestCodeReceiveListener,
        CompanyAuthorizationQueryService.OnCompanyAuthorizationListener {

    private String WRONG_REQUEST_CODE;
    private final String UPITTER_SMS_HEADER = "999999";

    private RequestCodeReceiver requestCodeReceiver;
    private MaterialEditText requestCodeEditText;

    private PhoneModel currentPhone;
    private CompanyAuthorizationQueryService queryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_confirm_activity);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        WRONG_REQUEST_CODE = getString(R.string.wrong_request_code_confirm_activity);
        currentPhone = getIntent().getParcelableExtra(RECEIVED_PHONE);

        queryService = CompanyAuthorizationQueryService.getService(this);
        requestCodeReceiver = RequestCodeReceiver.getReceiver(this);
    }

    public void onLoginClick(View v) {
        if (!validateForms()) return;
        setRequestCode(requestCodeEditText.getText().toString());
    }

    @Override
    protected void bindViews() {
        requestCodeEditText = findById(R.id.request_code_label_code_confirm_activity);
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
    public void onAuthorize(CompanyResponseModel companyResponseModel) {
        setHolder(CompanyHolder.getHolder());

        CompanyModel companyModel = new CompanyModel
                .Builder()
                .id(companyResponseModel.getId())
                .phone(currentPhone)
                .accessToken(companyResponseModel.getAccessToken())
                .name(companyResponseModel.getName())
                .build();

        Log.d("PRESAVE", companyModel.toString());

        getHolder().save(companyModel);
        startActivity(new Intent(this, TapeActivity.class));
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
        requestCodeEditText.setText(requestCode);

        queryService.sendRequestCode(
                currentPhone.getPhoneBody(),
                currentPhone.getDialCode(),
                requestCode);
    }

    private boolean validateForms() {
        if (requestCodeEditText.getText().length() == 0) {
            requestCodeEditText.setError(WRONG_REQUEST_CODE);
            return false;
        }

        return true;
    }
}
