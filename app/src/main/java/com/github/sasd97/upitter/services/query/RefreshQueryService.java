package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by Alexadner Dadukin on 24.07.2016.
 */

public class RefreshQueryService {

    public interface OnRefreshListener extends OnErrorQueryListener {
        void onLoadNew(PostsResponseModel posts);
        void onLoadOld(PostsResponseModel posts);
        void onEmpty();
    }

    private OnRefreshListener listener;

    private RefreshQueryService(@NonNull OnRefreshListener listener) {
        this.listener = listener;
    }

    public static RefreshQueryService getService(@NonNull OnRefreshListener listener) {
        return new RefreshQueryService(listener);
    }

    public void loadNew(@NonNull String accessToken,
                        int radius,
                        double latitude,
                        double longitude,
                        @NonNull String postId) {
        Call<PostsResponseModel> loadNew = RestService
                .baseFactory()
                .obtainNewPosts(language(),
                                accessToken,
                                latitude,
                                longitude,
                                radius,
                                postId);

        loadNew.enqueue(new Callback<PostsResponseModel>(listener) {
            @Override
            public void onResponse(Call<PostsResponseModel> call, Response<PostsResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                if (response.body().getResponseModel().getPosts().size() == 0) {
                    listener.onEmpty();
                    return;
                }
                listener.onLoadNew(response.body().getResponseModel());
            }
        });
    }

    public void loadOld(@NonNull String accessToken,
                        @NonNull String postId,
                        int radius,
                        double latitude,
                        double longitude) {
        Call<PostsResponseModel> loadOld = RestService
                .baseFactory()
                .obtainOldPosts(language(),
                        accessToken,
                        latitude,
                        longitude,
                        radius,
                        postId);

        loadOld.enqueue(new Callback<PostsResponseModel>(listener) {
            @Override
            public void onResponse(Call<PostsResponseModel> call, Response<PostsResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onLoadOld(response.body().getResponseModel());
            }
        });
    }
}
