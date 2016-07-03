package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.models.CompanyModel;
import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.PhoneModel;
import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationCompanyResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationRequestCodeResponseModel;
import com.github.sasd97.upitter.models.response.requestCode.RequestCodeResponseModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexander on 23.06.16.
 */
public class CompanyAuthorizationQueryService {

    public interface OnBusinessAuthorizationListener {
        void onCodeObtained();
        void onSendCodeError();
        void onAuthorize();
        void onRegister(String temporaryToken);
    }

    private OnBusinessAuthorizationListener listener;

    private CompanyAuthorizationQueryService(OnBusinessAuthorizationListener listener) {
        this.listener = listener;
    }

    public static CompanyAuthorizationQueryService getService(OnBusinessAuthorizationListener listener) {
        return new CompanyAuthorizationQueryService(listener);
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

    public void registerCompanyUser(@NonNull String name,
                                    @NonNull String description,
                                    @NonNull PhoneModel phone,
                                    @NonNull List<Integer> categories,
                                    @NonNull List<String> contactPhones,
                                    @NonNull String temporaryToken,
                                    @NonNull List<CoordinatesModel> coordinates,
                                    @NonNull String site) {

        CompanyModel register =
                new CompanyModel.Builder()
                .name(name)
                .description(description)
                .temporaryToken(temporaryToken)
                .categories(categories)
                .contactPhones(contactPhones)
                .coordinates(coordinates)
                .site(site)
                .build();

        RequestBody body = RestService.obtainJsonRaw(register.toJson());

        Call<AuthorizationCompanyResponseModel> registerCall = RestService.baseFactory().registerBusinessUser(phone.getPhoneBody(), phone.getDialCode(), body);
        registerCall.enqueue(new Callback<AuthorizationCompanyResponseModel>() {
            @Override
            public void onResponse(Call<AuthorizationCompanyResponseModel> call, Response<AuthorizationCompanyResponseModel> response) {

            }

            @Override
            public void onFailure(Call<AuthorizationCompanyResponseModel> call, Throwable t) {

            }
        });
    }
}
