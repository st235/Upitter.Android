package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.PlainSubscribeContainerModel;
import com.github.sasd97.upitter.models.response.pointers.PlainSubscribePointerModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by Alexadner Dadukin on 21.09.2016.
 */

public class SubscribeQueryService {

    public interface OnSubscribeListener extends OnErrorQueryListener {
        void onSubscribe(boolean isSubscribe, String amount);
    }

    private OnSubscribeListener listener;

    private SubscribeQueryService(@NonNull OnSubscribeListener listener) {
        this.listener = listener;
    }

    public static SubscribeQueryService getService(@NonNull OnSubscribeListener listener) {
        return new SubscribeQueryService(listener);
    }

    public void subscribe(@NonNull String companyId,
                          @NonNull String accessToken) {
        Call<PlainSubscribeContainerModel> subscribe =
                RestService
                .baseFactory()
                .subscribe(companyId, accessToken, language());

        subscribe.enqueue(new Callback<PlainSubscribeContainerModel>(listener) {
            @Override
            public void onResponse(Call<PlainSubscribeContainerModel> call, Response<PlainSubscribeContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                PlainSubscribePointerModel model = response.body().getSubscribe();
                listener.onSubscribe(model.isSubscribed(), model.getSubscriptionAmount());
            }
        });
    }
}
