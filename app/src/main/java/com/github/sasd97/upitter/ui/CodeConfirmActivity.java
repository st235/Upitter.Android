package com.github.sasd97.upitter.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.receivers.RequestCodeReceiver;
import com.github.sasd97.upitter.models.PhoneModel;
import com.github.sasd97.upitter.models.SmsModel;
import com.github.sasd97.upitter.services.query.BusinessAuthorizationQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import static com.github.sasd97.upitter.constants.IntentKeysConstants.*;
import static com.github.sasd97.upitter.constants.RequestCodesConstants.CODE_RECEIVER_INTENT_NAME;

public class CodeConfirmActivity extends BaseActivity implements
        RequestCodeReceiver.OnRequestCodeReceiveListener,
        BusinessAuthorizationQueryService.OnBusinessAuthorizationListener {

    private final String UPITTER_SMS_HEADER = "999999";

    private RequestCodeReceiver requestCodeReceiver;
    private MaterialEditText requestCodeEditText;

    private PhoneModel currentPhone;
    private BusinessAuthorizationQueryService queryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_confirm_activity);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        queryService = BusinessAuthorizationQueryService.getService(this);

        requestCodeReceiver = RequestCodeReceiver.getReceiver(this);
        currentPhone = getIntent().getParcelableExtra(RECEIVED_PHONE);
    }

    public void onLoginClick(View v) {
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
    public void onSendCodeError() {
        Snackbar
                .make(getRootView(), "Error", Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onAuthorize() {

    }

    @Override
    public void onRegister(String temporaryToken) {
        Intent intent = new Intent(this, BusinessRegistrationActivity.class);
        intent.putExtra(RECEIVED_TEMPORARY_TOKEN, temporaryToken);
        startActivity(intent);
    }

    @Override
    public void onReceiveRequestCode(ArrayList<SmsModel> messages) {
        for (SmsModel sms: messages) {
            if (sms.getAuthor().equalsIgnoreCase(UPITTER_SMS_HEADER)) {
                setRequestCode(sms.getBody().replaceAll("[^\\d]", ""));
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
}
