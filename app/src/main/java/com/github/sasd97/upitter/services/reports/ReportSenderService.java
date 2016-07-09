package com.github.sasd97.upitter.services.reports;

import android.content.Context;
import android.content.Intent;
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

import static com.github.sasd97.upitter.Upitter.*;

/**
 * Created by Alexander Dadukin on 13.06.2016.
 */

public class ReportSenderService implements ReportSender, ReportQueryService.OnReportSendListener {

    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {
        Log.i("REPORT_SENDER_SERVICE", String.format("[:USER_AVAILABLE] %1$b", UserHolder.isUserAvailable()));

        try {
            String id = errorContent.getProperty(ReportField.REPORT_ID);
            JSONObject description = errorContent.toJSON();

            if (!UserHolder.isUserAvailable()) {
                sendByEmail(context, id, description.toString());
                return;
            }

            ReportQueryService reportQueryService = ReportQueryService.getService(this);
            reportQueryService.sendReport(getHolder().get().getAccessToken(), id, description);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSend() {
        Log.d("REPORT_STATUS", "SENT");
    }

    @Override
    public void onError(ErrorModel errorModel) {
        Log.d("REPORT_STATUS", errorModel.toString());
    }

    public void sendByEmail(@NonNull Context context,
                            @NonNull String id,
                            @NonNull String reportJson) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "st235@yandex.ru"); //  TODO: change to upitter domain post
        intent.putExtra(Intent.EXTRA_SUBJECT, id);
        intent.putExtra(Intent.EXTRA_TEXT, reportJson);

        context.startActivity(Intent.createChooser(intent, "Send by mail"));
    }
}
