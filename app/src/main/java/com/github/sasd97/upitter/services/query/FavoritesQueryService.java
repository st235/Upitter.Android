package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.PostsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.PostPointerModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by alexander on 19.08.16.
 */

public class FavoritesQueryService {

    public interface OnFavoritesObtainListener extends OnErrorQueryListener {
        void onFavoritesObtained(List<PostPointerModel> favorites);
        void onOldFavoritesObtained(List<PostPointerModel> favorites);
    }

    private OnFavoritesObtainListener listener;

    public FavoritesQueryService(@NonNull OnFavoritesObtainListener listener) {
        this.listener = listener;
    }

    public static FavoritesQueryService getService(@NonNull OnFavoritesObtainListener listener) {
        return new FavoritesQueryService(listener);
    }

    public void obtainFavorites(@NonNull String accessToken) {
        Call<PostsContainerModel> obtainFavorites = RestService
                .baseFactory()
                .obtainFavorites(language(), accessToken);

        obtainFavorites.enqueue(new Callback<PostsContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostsContainerModel> call, Response<PostsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onFavoritesObtained(response.body().getPosts());
            }
        });
    }

    public void obtainOldFavorites(@NonNull String accessToken,
                                   @NonNull String postId) {
        Call<PostsContainerModel> obtainFavorites = RestService
                .baseFactory()
                .obtainOldFavorites(language(), accessToken, postId);

        obtainFavorites.enqueue(new Callback<PostsContainerModel>(listener) {
            @Override
            public void onResponse(Call<PostsContainerModel> call, Response<PostsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onOldFavoritesObtained(response.body().getPosts());
            }
        });
    }
}
