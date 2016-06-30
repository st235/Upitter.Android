package com.github.sasd97.upitter.services.query;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.PhoneModel;
import com.github.sasd97.upitter.models.request.CoordinatesRequestModel;
import com.github.sasd97.upitter.models.request.RegisterBusinessUserRequestModel;
import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationBusinessUserResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationRequestCodeResponseModel;
import com.github.sasd97.upitter.models.response.categories.CatergoriesResponseModel;
import com.github.sasd97.upitter.models.response.requestCode.RequestCodeResponseModel;
import com.github.sasd97.upitter.services.RestService;
import com.github.sasd97.upitter.utils.ListUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexander on 23.06.16.
 */
public class BusinessAuthorizationQueryService {

    public interface OnBusinessAuthorizationListener {
        void onCodeObtained();
        void onSendCodeError();
        void onAuthorize();
        void onRegister(String temporaryToken);
    }

    private OnBusinessAuthorizationListener listener;

    private BusinessAuthorizationQueryService(OnBusinessAuthorizationListener listener) {
        this.listener = listener;
    }

    public static BusinessAuthorizationQueryService getService(OnBusinessAuthorizationListener listener) {
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

    public void registerBusinessUser(@NonNull String name,
                                     @NonNull String description,
                                     @NonNull PhoneModel phone,
                                     int category,
                                     @NonNull List<String> contactPhones,
                                     @NonNull String temporaryToken,
                                     @NonNull List<CoordinatesModel> coordinates,
                                     @NonNull String site) {

        List<CoordinatesRequestModel> coors = ListUtils.mutate(coordinates, new ListUtils.OnListMutateListener<CoordinatesModel, CoordinatesRequestModel>() {
            @Override
            public CoordinatesRequestModel mutate(CoordinatesModel object) {
                return new CoordinatesRequestModel()
                        .setLatitude(object.getLatitude())
                        .setLongitude(object.getLongitude());
            }
        });

        RegisterBusinessUserRequestModel register =
                RegisterBusinessUserRequestModel
                .getRequest()
                .name(name)
                .temporaryToken(temporaryToken)
                .category(category)
                .coordinates(coors)
                .site(site);

        RequestBody body = RestService.obtainJsonRaw(register.toJSON());

        Call<AuthorizationBusinessUserResponseModel> registerCall = RestService.baseFactory().registerBusinessUser(phone.getPhoneBody(), phone.getDialCode(), body);
        registerCall.enqueue(new Callback<AuthorizationBusinessUserResponseModel>() {
            @Override
            public void onResponse(Call<AuthorizationBusinessUserResponseModel> call, Response<AuthorizationBusinessUserResponseModel> response) {

            }

            @Override
            public void onFailure(Call<AuthorizationBusinessUserResponseModel> call, Throwable t) {

            }
        });
    }
}
