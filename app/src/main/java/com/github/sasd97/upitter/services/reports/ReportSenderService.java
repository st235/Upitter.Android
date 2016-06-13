package com.github.sasd97.upitter.services.reports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.services.query.ReportQueryService;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

/**
 * Created by Alexander Dadukin on 13.06.2016.
 */
public class ReportSenderService implements ReportSender, ReportQueryService.OnReportSendListener {

    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {
        try {
            ReportQueryService reportQueryService = ReportQueryService.getService(this);
            reportQueryService.sendReport(errorContent.getProperty(ReportField.REPORT_ID), errorContent.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSend() {
        Log.d("REPORT_STATUS", "SENT");
    }

    @Override
    public void onSendError() {
        Log.d("REPORT_STATUS", "ERROR");
    }
}
