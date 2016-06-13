package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.models.response.report.ReportResponseModel;
import com.github.sasd97.upitter.services.RestService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alexander Dadukin on 13.06.2016.
 */
public class ReportQueryService {

    public interface OnReportSendListener {
        void onSend();
        void onSendError();
    }

    private OnReportSendListener listener;

    private ReportQueryService(OnReportSendListener listener) {
        this.listener = listener;
    }

    public static ReportQueryService getService(@NonNull OnReportSendListener listener) {
        return new ReportQueryService(listener);
    }

    public void sendReport(@NonNull String id, @NonNull JSONObject log) {
        Call<ReportResponseModel> sendReport = RestService.baseFactory().sendCrashReport(id, log);
        sendReport.enqueue(new Callback<ReportResponseModel>() {
            @Override
            public void onResponse(Call<ReportResponseModel> call, Response<ReportResponseModel> response) {
                if (response.body().isError()) {
                    listener.onSendError();
                    return;
                }

                listener.onSend();
            }

            @Override
            public void onFailure(Call<ReportResponseModel> call, Throwable t) {
                t.printStackTrace();
                listener.onSendError();
            }
        });
    }
}
