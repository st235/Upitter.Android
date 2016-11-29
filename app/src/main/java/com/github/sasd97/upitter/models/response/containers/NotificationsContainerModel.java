package com.github.sasd97.upitter.models.response.containers;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.github.sasd97.upitter.models.response.pointers.NotificationPointerModel;

import java.util.List;

/**
 * Created by Alexadner Dadukin on 11/29/2016.
 */

public class NotificationsContainerModel extends BaseResponseModel<List<NotificationPointerModel>> {

    public List<NotificationPointerModel> getNotifications() {
        return mResponse;
    }
}
