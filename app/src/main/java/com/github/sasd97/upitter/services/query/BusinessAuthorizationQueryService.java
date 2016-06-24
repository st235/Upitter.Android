package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationRequestCodeResponseModel;
import com.github.sasd97.upitter.models.response.requestCode.RequestCodeResponseModel;
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
        void onAuthorize();
        void onRegister(String temporaryToken);
    }

    private onBusinessAuthorizationListener listener;

    private BusinessAuthorizationQueryService(onBusinessAuthorizationListener listener) {
        this.listener = listener;
    }

    public static BusinessAuthorizationQueryService getService(onBusinessAuthorizationListener listener) {
        return new BusinessAuthorizationQueryService(listener);
    }

    public void obtainCodeRequest(@NonNull String number,
                                  @NonNull String countryCode) {
        Call<SimpleResponseModel> obtainRequest = RestService.baseFactory().obtainRequestCode(number, countryCode);
        obtainRequest.enqueue(new Callback<SimpleResponseModel>() {

            @Override
            public void onResponse(Call<SimpleResponseModel> call, Response<SimpleResponseModel> response) {
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

    public void sendRequestCode(@NonNull String number,
                                @NonNull String countryCode,
                                @NonNull final String requestCode) {
        Call<AuthorizationRequestCodeResponseModel> sendRequest = RestService.baseFactory().sendRequestCode(number, countryCode, requestCode);
        sendRequest.enqueue(new Callback<AuthorizationRequestCodeResponseModel>() {
            @Override
            public void onResponse(Call<AuthorizationRequestCodeResponseModel> call, Response<AuthorizationRequestCodeResponseModel> response) {
                if (response.body().isError()) {
                    listener.onSendCodeError();
                    return;
                }

                RequestCodeResponseModel responseModel = response.body().getRequestCode();
                if (!responseModel.isAuthorized()) listener.onRegister(responseModel.getTemporaryToken());
            }

            @Override
            public void onFailure(Call<AuthorizationRequestCodeResponseModel> call, Throwable t) {
                t.printStackTrace();
                listener.onSendCodeError();
            }
        });
    }
}
