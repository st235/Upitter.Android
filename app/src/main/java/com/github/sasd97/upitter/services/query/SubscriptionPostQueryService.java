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
        void onObtainOld(PostsPointerModel posts);
        void onObtainNew(PostsPointerModel posts);
        void onEmpty();
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

    public void obtainNewSubscriptionPosts(@NonNull String accessToken,
                                           @NonNull String postId) {
        Call<PostsContainerModel> obtainNewSubscriptionPosts = RestService
                .baseFactory()
                .obtainNewSubscriptionsPosts(language(), accessToken, postId);

        obtainNewSubscriptionPosts.enqueue(new Callback<PostsContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostsContainerModel> call, Response<PostsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                if (response.body().getResponseModel().getPosts().size() == 0) {
                    listener.onEmpty();
                    return;
                }
                listener.onObtainNew(response.body().getResponseModel());
            }
        });
    }

    public void obtainOldSubscriptionPosts(@NonNull String accessToken,
                                           @NonNull String postId) {
        Call<PostsContainerModel> obtainOldSubscriptionsPosts = RestService
                .baseFactory()
                .obtainOldSubscriptionsPosts(language(), accessToken, postId);

        obtainOldSubscriptionsPosts.enqueue(new Callback<PostsContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostsContainerModel> call, Response<PostsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onObtainOld(response.body().getResponseModel());
            }
        });
    }
}
