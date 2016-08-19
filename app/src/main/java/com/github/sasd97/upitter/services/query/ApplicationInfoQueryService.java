package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.ApplicationInfoContainerModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by alexander on 20.08.16.
 */
public class ApplicationInfoQueryService {

    public interface OnInfoListener extends OnErrorQueryListener {
        void onObtainInfo(int code, int version);
    }

    private OnInfoListener listener;

    private ApplicationInfoQueryService(@NonNull OnInfoListener listener) {
        this.listener = listener;
    }

    public static ApplicationInfoQueryService getService(@NonNull OnInfoListener listener) {
        return new ApplicationInfoQueryService(listener);
    }

    public void obtainInfo(@NonNull String accessToken) {
        Call<ApplicationInfoContainerModel> info =
                RestService
                .baseFactory()
                .checkForUpdates(language(), accessToken);

        info.enqueue(new Callback<ApplicationInfoContainerModel>(listener) {
            @Override
            public void onResponse(Call<ApplicationInfoContainerModel> call, Response<ApplicationInfoContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onObtainInfo(response.body().getResponseModel().getCode(), response.body().getResponseModel().getVersion());
            }
        });
    }
}
