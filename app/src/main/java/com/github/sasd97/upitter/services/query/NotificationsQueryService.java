package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.NotificationsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.NotificationPointerModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

/**
 * Created by Alexadner Dadukin on 11/29/2016.
 */

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

public class NotificationsQueryService {

    public interface OnNotificationListener extends OnErrorQueryListener {
        void onLoad(List<NotificationPointerModel> notifications);
        void onOld(List<NotificationPointerModel> notifications);
    }

    private OnNotificationListener listener;

    private NotificationsQueryService(OnNotificationListener listener) {
        this.listener = listener;
    }

    public static NotificationsQueryService getService(OnNotificationListener listener) {
        return new NotificationsQueryService(listener);
    }

    public void load(@NonNull String accessToken) {
        final Call<NotificationsContainerModel> notifications = RestService
                .baseFactory()
                .obtainNotifications(accessToken, language());

        notifications.enqueue(new Callback<NotificationsContainerModel>(listener) {
            @Override
            public void onResponse(Call<NotificationsContainerModel> call, Response<NotificationsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onLoad(response.body().getNotifications());
            }
        });
    }

    public void loadOld(@NonNull String accessToken,
                        @NonNull String notificationId) {
        final Call<NotificationsContainerModel> notifications = RestService
                .baseFactory()
                .obtainOldNotifications(accessToken, language(), "old", notificationId);

        notifications.enqueue(new Callback<NotificationsContainerModel>(listener) {
            @Override
            public void onResponse(Call<NotificationsContainerModel> call, Response<NotificationsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onOld(response.body().getNotifications());
            }
        });
    }
}
