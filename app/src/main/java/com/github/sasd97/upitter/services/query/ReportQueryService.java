package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.ReportContainerModel;
import com.github.sasd97.upitter.services.RestService;

import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Alexander Dadukin on 13.06.2016.
 */

public class ReportQueryService {

    public interface OnReportSendListener extends OnErrorQueryListener {
        void onSend();
    }

    private OnReportSendListener listener;

    private ReportQueryService(OnReportSendListener listener) {
        this.listener = listener;
    }

    public static ReportQueryService getService(@NonNull OnReportSendListener listener) {
        return new ReportQueryService(listener);
    }

    public void sendReport(@NonNull String accessToken,
                           @NonNull String id,
                           @NonNull JSONObject log) {
        Call<ReportContainerModel> sendReport = RestService
                .baseFactory()
                .sendCrashReport(id,
                        Locale.getDefault().getLanguage(),
                        accessToken,
                        log);

        sendReport.enqueue(new Callback<ReportContainerModel>(listener) {
            @Override
            public void onResponse(Call<ReportContainerModel> call, Response<ReportContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onSend();
            }
        });
    }
}
