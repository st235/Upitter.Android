package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.PostContainerModel;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.models.response.pointers.PostsPointerModel;
import com.github.sasd97.upitter.services.RestService;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by alexander on 08.07.16.
 */

public class PostQueryService {

    public interface OnPostListener extends OnErrorQueryListener {
        void onPostObtained(PostsPointerModel posts);
        void onPostWatch(int amount);
        void onFindPost(PostPointerModel post);
        void onCreatePost();
    }

    private OnPostListener listener;

    private PostQueryService(OnPostListener listener) {
        this.listener = listener;
    }

    public static PostQueryService getService(OnPostListener listener) {
        return new PostQueryService(listener);
    }

    public void obtainPosts(@NonNull Integer radius,
                            @NonNull String accessToken,
                            double latitude,
                            double longitude,
                            @NonNull List<Integer> categories) {

        Call<PostsContainerModel> obtainPosts =
                RestService
                .baseFactory()
                .obtainPosts(language(),
                        accessToken,
                        latitude,
                        longitude,
                        radius,
                        categories);

        obtainPosts.enqueue(new Callback<PostsContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostsContainerModel> call, Response<PostsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onPostObtained(response.body().getResponseModel());
            }
        });
    }

    public void obtainPostsAnonymous(@NonNull Integer radius,
                            double latitude,
                            double longitude,
                            @NonNull List<Integer> categories) {

        Call<PostsContainerModel> obtainPosts =
                RestService
                        .baseFactory()
                        .obtainPostsAnonymous(language(),
                                latitude,
                                longitude,
                                radius,
                                categories);

        obtainPosts.enqueue(new Callback<PostsContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostsContainerModel> call, Response<PostsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onPostObtained(response.body().getResponseModel());
            }
        });
    }

    public void createPost(@NonNull String accessToken,
                           @NonNull RequestBody body) {
        Call<PostContainerModel> createPost = RestService
                .baseFactory()
                .createPost(language(), accessToken, body);

        createPost.enqueue(new Callback<PostContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostContainerModel> call, Response<PostContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onCreatePost();
            }
        });
    }

    public void watchPost(@NonNull String accessToken,
                          @NonNull String postId) {
        Call<PostContainerModel> createPost = RestService
                .baseFactory()
                .watchPost(postId, accessToken, language());

        createPost.enqueue(new Callback<PostContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostContainerModel> call, Response<PostContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onPostWatch(response.body().getResponseModel().getWatchesAmount());
            }
        });
    }

    public Call<PostContainerModel> findPost(@NonNull String accessToken,
                          @NonNull String postId) {
        Call<PostContainerModel> createPost = RestService
                .baseFactory()
                .findPostById(postId, accessToken, language());

        createPost.enqueue(new Callback<PostContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostContainerModel> call, Response<PostContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onFindPost(response.body().getResponseModel());
            }
        });

        return createPost;
    }
}
