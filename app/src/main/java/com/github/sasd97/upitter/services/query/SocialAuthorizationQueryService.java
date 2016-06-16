package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.models.response.ErrorResponseModel;
import com.github.sasd97.upitter.models.response.authorization.AuthorizationResponseModel;
import com.github.sasd97.upitter.models.response.user.UserResponseModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alex on 10.06.2016.
 */
public class SocialAuthorizationQueryService {

    public interface OnSocialAuthorizationListener {
        void onServerNotify(UserResponseModel userResponseModel);
        void onNotifyError(int code, String message);
    }

    private OnSocialAuthorizationListener listener;

    private SocialAuthorizationQueryService(OnSocialAuthorizationListener listener) {
        this.listener = listener;
    }

    public static SocialAuthorizationQueryService getService(OnSocialAuthorizationListener listener) {
        return new SocialAuthorizationQueryService(listener);
    }

    public void notifyByGoogle(@NonNull String tokenId) {
        Call<AuthorizationResponseModel> notify = RestService
                .baseFactory()
                .authorizeWithGooglePlus(tokenId);

        notifyServerBySignIn(notify);
    }

    public void notifyByFacebook(@NonNull String accessToken) {
        Call<AuthorizationResponseModel> notify = RestService
                .baseFactory()
                .authorizeWithFacebook(accessToken);

        notifyServerBySignIn(notify);
    }

    public void notifyByTwitter(@NonNull String token,
                                @NonNull String secret) {
        Call<AuthorizationResponseModel> notify = RestService
                .baseFactory()
                .authorizeWithTwitter(token, secret);

        notifyServerBySignIn(notify);
    }

    private void notifyServerBySignIn(@NonNull Call<AuthorizationResponseModel> notify) {
        notify.enqueue(new Callback<AuthorizationResponseModel>() {
            @Override
            public void onResponse(Call<AuthorizationResponseModel> call, Response<AuthorizationResponseModel> response) {

                if (response.body().isError()) {
                    ErrorResponseModel error = response.body().getError();
                    listener.onNotifyError(error.getCode(), error.getMessage());
                    return;
                }

                listener.onServerNotify(response.body().getUser());
            }

            @Override
            public void onFailure(Call<AuthorizationResponseModel> call, Throwable t) {
                t.printStackTrace();
                listener.onNotifyError(-1, null);
            }
        });
    }
}
