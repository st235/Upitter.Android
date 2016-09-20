package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.UserSubscriptionsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.PlainCompanyPointerModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by Alexadner Dadukin on 20.09.2016.
 */

public class UserSubscriptionsQueryService {

    public interface OnSubscriptionsListener extends OnErrorQueryListener {
        void onSubscriptionsObtained(List<PlainCompanyPointerModel> subscriptions);
    }

    private OnSubscriptionsListener listener;

    private UserSubscriptionsQueryService(@NonNull OnSubscriptionsListener listener) {
        this.listener = listener;
    }

    public static UserSubscriptionsQueryService getService(@NonNull OnSubscriptionsListener listener) {
        return new UserSubscriptionsQueryService(listener);
    }

    public void obtainSubscriptions(@NonNull String accessToken) {
        Call<UserSubscriptionsContainerModel> obtainSubscriptions = RestService
                .baseFactory()
                .obtainUserSubscriptions(language(), accessToken);

        obtainSubscriptions.enqueue(new Callback<UserSubscriptionsContainerModel>(listener) {
            @Override
            public void onResponse(Call<UserSubscriptionsContainerModel> call, Response<UserSubscriptionsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onSubscriptionsObtained(response.body().getResponseModel());
            }
        });
    }
}
