package com.github.sasd97.upitter.events.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.github.sasd97.upitter.models.SmsModel;

import java.util.ArrayList;
import java.util.List;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.CODE_RECEIVER_INTENT_NAME;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.RECEIVED_SMS_CODES;

/**
 * Created by alexander on 23.06.16.
 */
public class SmsReceiver extends BroadcastReceiver {

    private final String PDUS_FORMAT = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get(PDUS_FORMAT);
            messages = new SmsMessage[pdus.length];

            if (Build.VERSION.SDK_INT >= 19) messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            else for (int i = 0; i < messages.length; i++) messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }


        ArrayList<SmsModel> messageList = new ArrayList<>();

        for (SmsMessage sms: messages) {
            messageList.add(new SmsModel.Builder()
                                .author(sms.getDisplayOriginatingAddress())
                                .body(sms.getMessageBody())
                                .build());
            Log.d("SMS_RECEIVER_BODY", sms.getDisplayMessageBody());
            Log.d("SMS_RECEIVER_ADDRESS", sms.getDisplayOriginatingAddress());
            Log.d("SMS_RECEIVER_BODY", sms.getMessageBody());
            Log.d("SMS_RECEIVER_TIMESTAMP", sms.getTimestampMillis()+ "");
            Log.d("SMS_RECEIVER_STATUS", sms.getStatus() + "");
        }

        Intent sendIntent = new Intent(CODE_RECEIVER_INTENT_NAME);
        sendIntent.putParcelableArrayListExtra(RECEIVED_SMS_CODES, messageList);
        context.sendBroadcast(sendIntent);
    }
}
