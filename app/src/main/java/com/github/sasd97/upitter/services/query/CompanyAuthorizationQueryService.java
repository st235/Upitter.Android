package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.models.response.containers.AuthorizationCompanyContainerModel;
import com.github.sasd97.upitter.models.response.containers.AuthorizationRequestCodeContainerModel;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.models.response.pointers.RequestCodePointerModel;
import com.github.sasd97.upitter.services.RestService;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by alexander on 23.06.16.
 */
public class CompanyAuthorizationQueryService {

    public interface OnCompanyAuthorizationListener extends OnErrorQueryListener {
        void onCodeObtained();
        void onSendCodeError(int attemptsAmount);
        void onAuthorize(CompanyPointerModel companyModel);
        void onRegister(String temporaryToken);
    }

    private OnCompanyAuthorizationListener listener;

    private CompanyAuthorizationQueryService(OnCompanyAuthorizationListener listener) {
        this.listener = listener;
    }

    public static CompanyAuthorizationQueryService getService(OnCompanyAuthorizationListener listener) {
        return new CompanyAuthorizationQueryService(listener);
    }

    public void obtainCodeRequest(@NonNull String number,
                                  @NonNull String countryCode) {

        Call<SimpleResponseModel> obtainRequest = RestService
                .baseFactory()
                .obtainRequestCode(number, countryCode);

        obtainRequest.enqueue(new Callback<SimpleResponseModel>(listener) {

            @Override
            public void onResponse(Call<SimpleResponseModel> call, Response<SimpleResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onCodeObtained();
            }
        });
    }

    public void sendRequestCode(@NonNull String number,
                                @NonNull String countryCode,
                                @NonNull final String requestCode) {
        Call<AuthorizationRequestCodeContainerModel> sendRequest = RestService
                .baseFactory()
                .sendRequestCode(number, countryCode, requestCode);

        sendRequest.enqueue(new Callback<AuthorizationRequestCodeContainerModel>(listener) {
            @Override
            public void onResponse(Call<AuthorizationRequestCodeContainerModel> call, Response<AuthorizationRequestCodeContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;

                RequestCodePointerModel responseModel = response.body().getRequestCode();
                if (!response.body().isSuccess()) {
                    listener.onSendCodeError(responseModel.getAttemptsAmount());
                    return;
                }

                if (!responseModel.isAuthorized()) listener.onRegister(responseModel.getTemporaryToken());
                else listener.onAuthorize(responseModel.getCompany());
            }
        });
    }

    public void debugRequestCode(@NonNull String number,
                                @NonNull String countryCode,
                                @NonNull final String requestCode) {
        Call<AuthorizationRequestCodeContainerModel> sendRequest = RestService
                .baseFactory()
                .debugRequestCode(number, countryCode, requestCode);

        sendRequest.enqueue(new Callback<AuthorizationRequestCodeContainerModel>(listener) {
            @Override
            public void onResponse(Call<AuthorizationRequestCodeContainerModel> call, Response<AuthorizationRequestCodeContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;

                RequestCodePointerModel responseModel = response.body().getRequestCode();
                if (!response.body().isSuccess()) {
                    listener.onSendCodeError(responseModel.getAttemptsAmount());
                    return;
                }

                if (!responseModel.isAuthorized()) listener.onRegister(responseModel.getTemporaryToken());
                else listener.onAuthorize(responseModel.getCompany());
            }
        });
    }

    public void registerCompanyUser(@NonNull CompanyModel.Builder builder) {
        final CompanyModel register = builder.build();

        RequestBody body = RestService.obtainJsonRaw(register.toJson());

        Call<AuthorizationCompanyContainerModel> registerCall = RestService.baseFactory()
                .registerCompany(register.getPhone().getPhoneBody(), register.getPhone().getDialCode(), body);

        registerCall.enqueue(new Callback<AuthorizationCompanyContainerModel>(listener) {
            @Override
            public void onResponse(Call<AuthorizationCompanyContainerModel> call, Response<AuthorizationCompanyContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                if (response.body().isSuccess()) listener.onAuthorize(response.body().getBusinessUser());
            }
        });
    }
}
