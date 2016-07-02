package com.github.sasd97.upitter.events.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.github.sasd97.upitter.models.SmsModel;

import java.util.ArrayList;
import static com.github.sasd97.upitter.constants.IntentKeysConstants.RECEIVED_SMS_CODES;

/**
 * Created by Alexadner Dadukin on 23.06.2016.
 */

public class RequestCodeReceiver extends BroadcastReceiver {

    public interface OnRequestCodeReceiveListener {
        void onReceiveRequestCode(ArrayList<SmsModel> messages);
    }

    private OnRequestCodeReceiveListener listener;

    private RequestCodeReceiver(@NonNull OnRequestCodeReceiveListener listener) {
        this.listener = listener;
    }

    public static RequestCodeReceiver getReceiver(@NonNull OnRequestCodeReceiveListener listener) {
        return new RequestCodeReceiver(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<SmsModel> messages = intent.getParcelableArrayListExtra(RECEIVED_SMS_CODES);
        if (listener != null)
            listener.onReceiveRequestCode(messages);
    }
}
