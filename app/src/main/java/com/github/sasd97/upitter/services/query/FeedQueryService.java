package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.posts.PostResponseModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by Alexadner Dadukin on 10.07.2016.
 */

public class FeedQueryService {

    public interface OnTapeQueryListener extends OnErrorQueryListener {
        void onLike(PostResponseModel post);
        void onDislike(PostResponseModel post);

        void onAddFavorites(PostResponseModel post);
        void onRemoveFromFavorites(PostResponseModel post);

        void onVote(PostResponseModel post);

        @Override
        void onError(ErrorModel error);
    }

    private OnTapeQueryListener listener;

    private FeedQueryService(OnTapeQueryListener listener) {
        this.listener = listener;
    }

    public static FeedQueryService getService(OnTapeQueryListener listener) {
        return new FeedQueryService(listener);
    }

    public void like(@NonNull String accessToken,
                     @NonNull String postId) {
        final Call<PostResponseModel> like = RestService
                .baseFactory()
                .likePost(postId, language(), accessToken);

        like.enqueue(new Callback<PostResponseModel>(listener) {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;

                if (response.body().getResponseModel().isLikedByMe()) listener.onLike(response.body().getResponseModel());
                else listener.onDislike(response.body().getResponseModel());
            }
        });
    }

    public void favorite(@NonNull String accessToken,
                         @NonNull String postId) {
        final Call<PostResponseModel> favorite = RestService
                .baseFactory()
                .addPostToFavorite(postId, language(), accessToken);

        favorite.enqueue(new Callback<PostResponseModel>(listener) {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onAddFavorites(response.body());
            }
        });
    }

    public void vote(@NonNull String accessToken,
                     @NonNull String postId,
                     int voteVariant) {
        final Call<PostResponseModel> favorite = RestService
                .baseFactory()
                .voteInPost(postId, voteVariant, language(), accessToken);

        favorite.enqueue(new Callback<PostResponseModel>(listener) {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onVote(response.body().getResponseModel());
            }
        });
    }
}
