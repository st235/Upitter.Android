package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexander on 23.06.16.
 */
public class BusinessAuthorizationQueryService {

    public interface onBusinessAuthorizationListener {
        void onCodeObtained();
        void onSendCodeError();
    }

    private onBusinessAuthorizationListener listener;

    private BusinessAuthorizationQueryService(onBusinessAuthorizationListener listener) {
        this.listener = listener;
    }

    public static BusinessAuthorizationQueryService getService(onBusinessAuthorizationListener listener) {
        return new BusinessAuthorizationQueryService(listener);
    }

    public void sendCodeRequest(@NonNull String number,
                                @NonNull String countryCode) {
        Call<SimpleResponseModel> sendRequest = RestService.baseFactory().sendRequestCode(number, countryCode);
        sendRequest.enqueue(new Callback<SimpleResponseModel>() {

            @Override
            public void onResponse(Call<SimpleResponseModel> call, Response<SimpleResponseModel> response) {
                Log.d("REQUEST", call.request().url().toString());
                if (response.body().isError()) {
                    listener.onSendCodeError();
                    return;
                }

                listener.onCodeObtained();
            }

            @Override
            public void onFailure(Call<SimpleResponseModel> call, Throwable t) {
                t.printStackTrace();
                listener.onSendCodeError();
            }
        });
    }
}
