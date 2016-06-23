package com.github.sasd97.upitter.events.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

/**
 * Created by alexander on 23.06.16.
 */
public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;

        String str = "";
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];

            if (Build.VERSION.SDK_INT >= 19) {
                messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            } else {
                for (int i=0; i < messages.length; i++) {
                    byte[] data = null;

                     messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    // Return the User Data section minus the
                    // User Data Header (UDH) (if there is any UDH at all)
                    data = messages[i].getUserData();

                    // Generally you can do away with this for loop
                    // You'll just need the next for loop
                    for (int index=0; index < data.length; index++) {
                        str += Byte.toString(data[index]);
                    }

                    str += "\nTEXT MESSAGE (FROM BINARY): ";

                    for (int index=0; index < data.length; index++) {
                        str += Character.toString((char) data[index]);
                    }

                    str += "\n";
                }
            }
    }
}
