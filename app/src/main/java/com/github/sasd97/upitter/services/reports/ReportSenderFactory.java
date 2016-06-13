package com.github.sasd97.upitter.services.reports;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;

/**
 * Created by Alexander Dadukin on 13.06.2016.
 */
public class ReportSenderFactory implements org.acra.sender.ReportSenderFactory {

    @NonNull
    @Override
    public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration config) {
        return new ReportSenderService();
    }
}
