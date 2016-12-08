package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.ActivitiesContainerModel;
import com.github.sasd97.upitter.models.response.pointers.ActivityPointerModel;
import com.github.sasd97.upitter.services.RestService;
import com.orhanobut.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by Alexadner Dadukin on 11.09.2016.
 */
public class ActivityQueryService {

    public interface OnActivityListener extends OnErrorQueryListener {
        void obObtainTitles(List<ActivityPointerModel> categories);
    }

    private OnActivityListener listener;

    private ActivityQueryService(OnActivityListener listener) {
        this.listener = listener;
    }

    public static ActivityQueryService getService(OnActivityListener listener) {
        return new ActivityQueryService(listener);
    }

    public void getTitles(@NonNull Integer[] activities) {
        Call<ActivitiesContainerModel> getTitles = RestService
                .baseFactory()
                .obtainActivitiesTitles(language(), activities);

        for (int i: activities) Logger.e(String.valueOf(i));

        getTitles.enqueue(new Callback<ActivitiesContainerModel>(listener) {
            @Override
            public void onResponse(Call<ActivitiesContainerModel> call, Response<ActivitiesContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.obObtainTitles(response.body().getTitles());
            }
        });
    }
}
