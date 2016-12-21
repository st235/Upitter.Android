package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.SimpleResponseModel;
import com.github.sasd97.upitter.models.response.containers.CommentContainerModel;
import com.github.sasd97.upitter.models.response.containers.CommentsContainerModel;
import com.github.sasd97.upitter.models.response.pointers.CommentPointerModel;
import com.github.sasd97.upitter.models.response.pointers.CommentsPointerModel;
import com.github.sasd97.upitter.services.RestService;

import retrofit2.Call;
import retrofit2.Response;

import static com.github.sasd97.upitter.Upitter.language;

/**
 * Created by Alexadner Dadukin on 04.09.2016.
 */

public class CommentsQueryService {

    public interface OnCommentListener extends OnErrorQueryListener {
        void onObtainNew(CommentsPointerModel comments);
        void onObtain(CommentsPointerModel comments);
        void onAdd(CommentPointerModel comment);
        void onEdit(CommentPointerModel comment);
        void onRemove(boolean isSuccess);
    }

    private OnCommentListener listener;

    private CommentsQueryService(@NonNull OnCommentListener listener) {
        this.listener = listener;
    }

    public static CommentsQueryService getService(@NonNull OnCommentListener listener) {
        return new CommentsQueryService(listener);
    }

    public void obtainComments(@NonNull String accessToken,
                                    @NonNull String postId) {
        Call<CommentsContainerModel> obtainComments = RestService
                .baseFactory()
                .obtainPostComments(language(), accessToken, postId);

        obtainComments.enqueue(new Callback<CommentsContainerModel>(listener) {
            @Override
            public void onResponse(Call<CommentsContainerModel> call, Response<CommentsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onObtain(response.body().getComments());
            }
        });
    }

    public void obtainNewComments(@NonNull String accessToken,
                                  @NonNull String postId,
                                  @NonNull String commentId) {
        Call<CommentsContainerModel> obtainComments = RestService
                .baseFactory()
                .obtainNewPostComments(language(), accessToken, postId, commentId, "new");

        obtainComments.enqueue(new Callback<CommentsContainerModel>(listener) {
            @Override
            public void onResponse(Call<CommentsContainerModel> call, Response<CommentsContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onObtainNew(response.body().getComments());
            }
        });
    }

    public void addComment(@NonNull String accessToken,
                           @NonNull String postId,
                           @NonNull String text) {
        Call<CommentContainerModel> addComment = RestService
                .baseFactory()
                .addPostComment(language(), accessToken, postId, text);

        addComment.enqueue(new Callback<CommentContainerModel>(listener) {
            @Override
            public void onResponse(Call<CommentContainerModel> call, Response<CommentContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onAdd(response.body().getComment());
            }
        });
    }

    public void addComment(@NonNull String accessToken,
                           @NonNull String postId,
                           @NonNull String text,
                           @NonNull String replyTo) {
        Call<CommentContainerModel> obtainComments = RestService
                .baseFactory()
                .addPostComment(language(), accessToken, postId, text, replyTo);

        obtainComments.enqueue(new Callback<CommentContainerModel>(listener) {
            @Override
            public void onResponse(Call<CommentContainerModel> call, Response<CommentContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onAdd(response.body().getComment());
            }
        });
    }

    public void editComment(@NonNull String accessToken,
                            @NonNull String commentId,
                            @NonNull String text) {
        Call<CommentContainerModel> obtainComments = RestService
                .baseFactory()
                .editPostComment(language(), accessToken, commentId, text);

        obtainComments.enqueue(new Callback<CommentContainerModel>(listener) {
            @Override
            public void onResponse(Call<CommentContainerModel> call, Response<CommentContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onEdit(response.body().getComment());
            }
        });
    }

    public void removeComment(@NonNull String accessToken,
                               @NonNull String commentId) {
        Call<SimpleResponseModel> obtainComments = RestService
                .baseFactory()
                .removePostComment(language(), accessToken, commentId);

        obtainComments.enqueue(new Callback<SimpleResponseModel>(listener) {
            @Override
            public void onResponse(Call<SimpleResponseModel> call, Response<SimpleResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onRemove(response.body().isSuccess());
            }
        });
    }
}
