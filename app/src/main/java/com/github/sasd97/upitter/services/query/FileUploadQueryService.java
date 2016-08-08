package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.Callback;
import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.fileServer.MediaResponseModel;
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

    public void uploadImage(@NonNull String id,
                            @NonNull String path,
                            @NonNull String type,
                            @NonNull String purpose) {

        HashMap<String, RequestBody> imageBody = RestService.obtainImageMultipart(new File(path));

        Call<MediaResponseModel> uploadImage = RestService
                .fileServerFactory()
                .uploadImage(RestService.obtainTextMultipart(id),
                        RestService.obtainTextMultipart(type),
                        RestService.obtainTextMultipart(purpose),
                        imageBody);

        uploadImage.enqueue(new Callback<MediaResponseModel>(listener) {
            @Override
            public void onResponse(Call<MediaResponseModel> call, Response<MediaResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onUpload(response.body().getPath());
            }
        });
    }

    public void uploadAvatar(@NonNull String id,
                            @NonNull String path) {

        HashMap<String, RequestBody> imageBody = RestService.obtainImageMultipart(new File(path));

        Call<MediaResponseModel> uploadImage = RestService
                .fileServerFactory()
                .uploadAvatar(RestService.obtainTextMultipart(id),
                        imageBody);

        uploadImage.enqueue(new Callback<MediaResponseModel>(listener) {
            @Override
            public void onResponse(Call<MediaResponseModel> call, Response<MediaResponseModel> response) {
                super.onResponse(call, response);
                if (!RestService.handleError(call, response, listener)) return;
                listener.onUpload(response.body().getResponseModel().getPath());
            }
        });
    }
}
