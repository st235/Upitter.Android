package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
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
        void onPostObtained(PostsResponseModel posts);
        void onCreatePost();
    }

    private OnPostListener listener;

    private PostQueryService(OnPostListener listener) {
        this.listener = listener;
    }

    public static PostQueryService getService(OnPostListener listener) {
        return new PostQueryService(listener);
    }

    public void obtainPosts(@NonNull String accessToken,
                            @NonNull Integer radius,
                            double latitude,
                            double longitude,
                            @NonNull List<Integer> categories) {

        Call<PostsResponseModel> obtainPosts =
                RestService
                .baseFactory()
                .obtainPosts(language(),
                        accessToken,
                        latitude,
                        longitude,
                        radius,
                        categories);

        obtainPosts.enqueue(new Callback<PostsResponseModel>(listener) {
            @Override
            public void onResponse(Call<PostsResponseModel> call, Response<PostsResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onPostObtained(response.body().getResponseModel());
            }
        });
    }

    public void createPost(@NonNull String accessToken,
                           @NonNull RequestBody body) {

        Call<PostResponseModel> createPost = RestService
                .baseFactory()
                .createPost(language(), accessToken, body);

        createPost.enqueue(new Callback<PostResponseModel>(listener) {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onCreatePost();
            }
        });
    }
}
