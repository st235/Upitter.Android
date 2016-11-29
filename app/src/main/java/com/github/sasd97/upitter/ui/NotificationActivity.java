package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.models.response.pointers.NotificationPointerModel;
import com.github.sasd97.upitter.services.query.NotificationsQueryService;
import com.github.sasd97.upitter.ui.adapters.recyclers.NotificationsRecycler;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;

import static com.github.sasd97.upitter.Upitter.getHolder;

public class NotificationActivity extends BaseActivity implements NotificationsQueryService.OnNotificationListener {

    private UserModel userModel;
    private NotificationsQueryService queryService;

    @BindView(R.id.notifications_recycler_view) RecyclerView notificationRecyclerView;
    private NotificationsRecycler notificationsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_notification);
    }

    @Override
    protected void setupViews() {
        setToolbar(R.id.toolbar, true);
        queryService = NotificationsQueryService.getService(this);
        userModel = getHolder().get();

        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        queryService.load(userModel.getAccessToken());
    }

    @Override
    public void onLoad(List<NotificationPointerModel> notifications) {
        notificationsRecycler = new NotificationsRecycler(notifications);
        notificationRecyclerView.setAdapter(notificationsRecycler);

        Logger.i(notifications.size() + ";");
        for (NotificationPointerModel n: notifications) Logger.i(n.toString());
    }

    @Override
    public void onOld(List<NotificationPointerModel> notifications) {

    }

    @Override
    public void onError(ErrorModel error) {

    }
}
