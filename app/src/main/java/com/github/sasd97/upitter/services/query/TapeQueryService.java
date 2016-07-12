package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alexadner Dadukin on 10.07.2016.
 */
public class TapeQueryService {

    public interface OnTapeQueryListener extends OnErrorQueryListener {
        void onLike(PostResponseModel post);
        void onDislike(PostResponseModel post);

        void onAddFavorites(PostResponseModel post);
        void onRemoveFromFavorites(PostResponseModel post);

        @Override
        void onError(ErrorModel error);
    }

    private OnTapeQueryListener listener;

    private TapeQueryService(OnTapeQueryListener listener) {
        this.listener = listener;
    }

    public static TapeQueryService getService(OnTapeQueryListener listener) {
        return new TapeQueryService(listener);
    }

    public void like(@NonNull String language,
                     @NonNull String accessToken,
                     @NonNull String postId) {
        final Call<PostResponseModel> like = RestService
                .baseFactory()
                .likePost(postId, language, accessToken);

        like.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                RestService.logRequest(call);
                if (!RestService.handleError(call, response, listener)) return;

                if (response.body().getResponseModel().isLikedByMe()) listener.onLike(response.body().getResponseModel());
                else listener.onDislike(response.body().getResponseModel());
            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                RestService.handleThrows(t, call, listener);
            }
        });
    }

    public void favorite(@NonNull String language,
                     @NonNull String accessToken,
                     @NonNull String postId) {
        final Call<PostResponseModel> favorite = RestService
                .baseFactory()
                .addPostToFavorite(postId, language, accessToken);

        favorite.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                RestService.logRequest(call);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onAddFavorites(response.body());
            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                RestService.handleThrows(t, call, listener);
            }
        });
    }
}
