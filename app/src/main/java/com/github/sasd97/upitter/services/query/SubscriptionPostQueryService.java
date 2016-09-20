package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.PostsPointerModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by alexander on 20.09.16.
 */

public class SubscriptionPostQueryService {

    public interface OnSubscriptionPostListener extends OnErrorQueryListener {
        void onPostObtained(PostsPointerModel posts);
    }

    private OnSubscriptionPostListener listener;

    private SubscriptionPostQueryService(@NonNull OnSubscriptionPostListener listener) {
        this.listener = listener;
    }

    public static SubscriptionPostQueryService getService(@NonNull OnSubscriptionPostListener listener) {
        return new SubscriptionPostQueryService(listener);
    }

    public void obtainSubscriptionPosts(@NonNull String accessToken) {
        Call<PostsContainerModel> obtainSubscriptionPosts = RestService
                .baseFactory()
                .obtainSubscriptionsPosts(language(), accessToken);

        obtainSubscriptionPosts.enqueue(new Callback<PostsContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostsContainerModel> call, Response<PostsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onPostObtained(response.body().getResponseModel());
            }
        });
    }
}
