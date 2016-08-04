package com.github.sasd97.upitter.services.reports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.holders.UserHolder;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.services.query.ReportQueryService;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.json.JSONObject;

/**
 * Created by Alexander Dadukin on 13.06.2016.
 */

public class ReportSenderService implements ReportSender, ReportQueryService.OnReportSendListener {

    private static final String TAG = "REPORT_SENDER_SERVICE";
    private static final String SEND_FROM_LOGIN = "upitter.log@gmail.com";
    private static final String SEND_FROM_PASSWORD = "loclook2016";
    private static final String SEND_TO_LOGIN = "upitter.tch@gmail.com";

    private static final String EMPTY_STRING = "";

    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {
        Log.i(TAG, String.format("[:USER_AVAILABLE] %1$b", UserHolder.isUserAvailable()));

        try {
            String id = errorContent.getProperty(ReportField.REPORT_ID);
            JSONObject description = errorContent.toJSON();

            if (!UserHolder.isUserAvailable() || UserHolder.getAccessToken().equalsIgnoreCase(EMPTY_STRING)) {
                sendByEmail(id, description.toString());
                return;
            }

            ReportQueryService reportQueryService = ReportQueryService.getService(this);
            reportQueryService.sendReport(UserHolder.getAccessToken(), id, description);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSend() {
        Log.d(TAG, "SENT");
    }

    @Override
    public void onError(ErrorModel errorModel) {
        Log.d(TAG, errorModel.toString());
    }

    private void sendByEmail(@NonNull String id,
                            @NonNull String reportJson) {
        try {
            GMailSender sender = new GMailSender(SEND_FROM_LOGIN, SEND_FROM_PASSWORD);
            sender.sendMail(id, reportJson, SEND_FROM_LOGIN, SEND_TO_LOGIN);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
