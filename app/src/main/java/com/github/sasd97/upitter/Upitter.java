package com.github.sasd97.upitter;

import android.app.Application;
import android.content.Context;

import com.github.sasd97.upitter.services.RestService;
import com.github.sasd97.upitter.utils.Assets;
import com.github.sasd97.upitter.utils.Authorization;
import com.github.sasd97.upitter.utils.Connectivity;
import com.github.sasd97.upitter.utils.Palette;
import com.github.sasd97.upitter.utils.Prefs;
import com.orm.SugarContext;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */

@ReportsCrashes(
        reportSenderFactoryClasses = { com.github.sasd97.upitter.services.reports.ReportSenderFactory.class },
        customReportContent = { ReportField.ANDROID_VERSION,
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.APPLICATION_LOG,
                ReportField.AVAILABLE_MEM_SIZE,
                ReportField.BRAND,
                ReportField.BUILD,
                ReportField.LOGCAT,
                ReportField.STACK_TRACE,
                ReportField.USER_COMMENT,
                ReportField.REPORT_ID
        },
        mode = ReportingInteractionMode.NOTIFICATION,
        resDialogTitle = R.string.crash_ticker_message,
        resDialogText = R.string.crash_body_message,
        resDialogTheme = R.style.MD_Light,
        resNotifTitle = R.string.crash_title_message,
        resNotifText = R.string.crash_body_message,
        resNotifTickerText = R.string.crash_ticker_message
)
public class Upitter extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Assets.init(this);
        Authorization.init(this);
        SugarContext.init(this);
        Connectivity.init(this);
        Palette.init(this);
        Prefs.init(this);
        RestService.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
