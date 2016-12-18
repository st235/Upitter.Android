package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.components.ImageUploadRequestBody;
import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.containers.FileContainerModel;
import com.github.sasd97.upitter.models.response.containers.MediaContainerModel;
import com.github.sasd97.upitter.models.response.pointers.FilePointerModel;
import com.github.sasd97.upitter.models.response.pointers.MediaPointerModel;
import com.github.sasd97.upitter.services.RestService;

import java.io.File;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Alexadner Dadukin on 30.06.2016.
 */

public class FileUploadQueryService {
    public interface OnFileUploadListener extends OnErrorQueryListener {
        void onUpload(String path);
    }

    private OnFileUploadListener listener;

    private FileUploadQueryService(OnFileUploadListener listener) {
        this.listener = listener;
    }

    public static FileUploadQueryService getService(OnFileUploadListener listener) {
        return new FileUploadQueryService(listener);
    }

    public Call<MediaContainerModel> uploadImage(@NonNull String id,
                                                 @NonNull String path,
                                                 @NonNull String type,
                                                 @NonNull String purpose) {

        HashMap<String, RequestBody> imageBody = RestService.obtainImageMultipart(new File(path));

        Call<MediaContainerModel> uploadImage = RestService
                .fileServerFactory()
                .uploadImage(RestService.obtainTextMultipart(id),
                        RestService.obtainTextMultipart(type),
                        RestService.obtainTextMultipart(purpose),
                        imageBody);

        uploadImage.enqueue(new Callback<MediaContainerModel>(listener) {
            @Override
            public void onResponse(Call<MediaContainerModel> call, Response<MediaContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onUpload(response.body().getResponseModel().getPath());
            }
        });

        return uploadImage;
    }

    public Call<MediaContainerModel> uploadAvatar(@NonNull String id,
                                                  @NonNull String path) {

        HashMap<String, RequestBody> imageBody = RestService.obtainImageMultipart(new File(path));

        Call<MediaContainerModel> uploadImage = RestService
                .fileServerFactory()
                .uploadAvatar(RestService.obtainTextMultipart(id),
                        imageBody);

        uploadImage.enqueue(new Callback<MediaContainerModel>(listener) {
            @Override
            public void onResponse(Call<MediaContainerModel> call, Response<MediaContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onUpload(response.body().getResponseModel().getPath());
            }
        });

        return uploadImage;
    }

    public Call<FileContainerModel> uploadPostImage(@NonNull String id,
                                                    @NonNull String path,
                                                    @NonNull ImageUploadRequestBody.UploadCallback callback) {
        File file = new File(path);
        final RequestBody requestId = RestService.obtainTextMultipart(id);
        ImageUploadRequestBody imageToRequest = new ImageUploadRequestBody(file, callback);

        Call<FileContainerModel> uploadPostImage = RestService
                .fileServerFactory()
                .uploadPostImage(requestId, imageToRequest);

        uploadPostImage.enqueue(new Callback<FileContainerModel>(listener) {
            @Override
            public void onResponse(Call<FileContainerModel> call, Response<FileContainerModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onUpload(response.body().getResponseModel().getFid());
            }
        });

        return uploadPostImage;
    }
}
