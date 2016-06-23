package com.github.sasd97.upitter.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.receivers.RequestCodeReceiver;
import com.github.sasd97.upitter.events.receivers.SmsReceiver;
import com.github.sasd97.upitter.models.SmsModel;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.CODE_RECEIVER_INTENT_NAME;

public class CodeConfirmActivity extends BaseActivity implements RequestCodeReceiver.OnRequestCodeReceiveListener {

    private RequestCodeReceiver requestCodeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_confirm_activity);
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));

        requestCodeReceiver = RequestCodeReceiver.getReceiver(this);
    }

    @Override
    protected void bindViews() {
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
        Log.d("ACTIVITY_RECIEVE", "SMS_RECIVED");
        for (SmsModel sms: messages) {
            Log.d("ACTIVITY_RECIEVE", sms.toString());
        }
    }
}
