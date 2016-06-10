package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.models.response.ErrorResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationResponseModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alex on 10.06.2016.
 */
public class AuthorizationQueryService {

    public interface OnAuthorizationListener {
        void onServerNotify();
        void onError(int code, String message);
    }

    private OnAuthorizationListener listener;

    private AuthorizationQueryService(OnAuthorizationListener listener) {
        this.listener = listener;
    }

    public static AuthorizationQueryService getService(OnAuthorizationListener listener) {
        return new AuthorizationQueryService(listener);
    }

    public void notifyServerBySignIn(@NonNull String tokenId) {
        Call<AuthorizationResponseModel> notify = RestService
                .baseFactory()
                .authorizeWithGooglePlus(tokenId);

        notify.enqueue(new Callback<AuthorizationResponseModel>() {
            @Override
            public void onResponse(Call<AuthorizationResponseModel> call, Response<AuthorizationResponseModel> response) {
                if (response.body().isError()) {
                    ErrorResponseModel error = response.body().getError();
                    listener.onError(error.getCode(), error.getMessage());
                    return;
                }

                listener.onServerNotify();
            }

            @Override
            public void onFailure(Call<AuthorizationResponseModel> call, Throwable t) {
                t.printStackTrace();
                listener.onError(-1, null);
            }
        });
    }
}
