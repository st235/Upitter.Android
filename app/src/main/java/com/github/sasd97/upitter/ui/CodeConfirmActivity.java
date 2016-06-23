package com.github.sasd97.upitter.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.receivers.SmsReceiver;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

public class CodeConfirmActivity extends BaseActivity {

    private SmsReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_confirm_activity);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        smsReceiver = new SmsReceiver();
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSmsReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSmsReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterSmsReceiver();
    }

    private void registerSmsReceiver() {
        IntentFilter smsFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, smsFilter);
    }

    private void unregisterSmsReceiver() {
        if (smsReceiver != null)
            unregisterReceiver(smsReceiver);
    }
}
