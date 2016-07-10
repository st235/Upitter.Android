package com.github.sasd97.upitter.services.query;

import com.github.sasd97.upitter.models.response.posts.PostResponseModel;

/**
 * Created by Alexadner Dadukin on 10.07.2016.
 */
public class TapeQueryService {

    public interface OnTapeQueryListener {
        void onLike(PostResponseModel post);
        void onDislike(PostResponseModel post);

        void onAddFavorites(PostResponseModel post);
        void onRemoveFromFavorites(PostResponseModel post);
    }

    private OnTapeQueryListener listener;

    private TapeQueryService(OnTapeQueryListener listener) {
        this.listener = listener;
    }

    public static TapeQueryService getService(OnTapeQueryListener listener) {
        return new TapeQueryService(listener);
    }
}
