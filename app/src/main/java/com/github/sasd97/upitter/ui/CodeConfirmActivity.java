package com.github.sasd97.upitter.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.receivers.RequestCodeReceiver;
import com.github.sasd97.upitter.events.receivers.SmsReceiver;
import com.github.sasd97.upitter.models.PhoneModel;
import com.github.sasd97.upitter.models.SmsModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.CODE_RECEIVER_INTENT_NAME;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.RECEIVED_PHONE;

public class CodeConfirmActivity extends BaseActivity implements RequestCodeReceiver.OnRequestCodeReceiveListener {

    private final String UPITTER_SMS_HEADER = "999999";

    private RequestCodeReceiver requestCodeReceiver;
    private MaterialEditText requestCodeEditText;

    private PhoneModel currentPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_confirm_activity);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        requestCodeReceiver = RequestCodeReceiver.getReceiver(this);
        currentPhone = getIntent().getParcelableExtra(RECEIVED_PHONE);
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
        if (requestCodeReceiver != null) unregisterReceiver(requestCodeReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestCodeReceiver != null) unregisterReceiver(requestCodeReceiver);
    }

    @Override
    public void onReceiveRequestCode(ArrayList<SmsModel> messages) {
        for (SmsModel sms: messages) {
            Log.d("SMS", sms.toString());
            if (sms.getAuthor().equalsIgnoreCase(UPITTER_SMS_HEADER)) {
                setRequestCode(sms.getBody().replaceAll("[^\\d]", ""));
            }
        }
    }

    private void setRequestCode(@NonNull String requestCode) {
        requestCodeEditText.setText(requestCode);
    }
}
