package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.AuthorizationContainerModel;
import com.github.sasd97.upitter.models.response.pointers.UserPointerModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Alex on 10.06.2016.
 */

public class UserAuthorizationQueryService {

    public interface OnSocialAuthorizationListener extends OnErrorQueryListener {
        void onServerNotify(UserPointerModel userPointerModel);
    }

    private OnSocialAuthorizationListener listener;

    private UserAuthorizationQueryService(OnSocialAuthorizationListener listener) {
        this.listener = listener;
    }

    public static UserAuthorizationQueryService getService(OnSocialAuthorizationListener listener) {
        return new UserAuthorizationQueryService(listener);
    }

    public void notifyByGoogle(@NonNull String tokenId) {
        Call<AuthorizationContainerModel> notify = RestService
                .baseFactory()
                .authorizeWithGooglePlus(tokenId);

        notifyServerBySignIn(notify);
    }

    public void notifyByFacebook(@NonNull String accessToken) {
        Call<AuthorizationContainerModel> notify = RestService
                .baseFactory()
                .authorizeWithFacebook(accessToken);

        notifyServerBySignIn(notify);
    }

    public void notifyByTwitter(@NonNull String token,
                                @NonNull String secret) {
        Call<AuthorizationContainerModel> notify = RestService
                .baseFactory()
                .authorizeWithTwitter(token, secret);

        notifyServerBySignIn(notify);
    }

    private void notifyServerBySignIn(@NonNull Call<AuthorizationContainerModel> notify) {
        notify.enqueue(new Callback<AuthorizationContainerModel>(listener) {
            @Override
            public void onResponse(Call<AuthorizationContainerModel> call, Response<AuthorizationContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onServerNotify(response.body().getUser());
            }
        });
    }
}
