package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.fileServer.UploadAvatarResponseModel;
import com.github.sasd97.upitter.services.RestService;

import java.io.File;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
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

        Call<UploadAvatarResponseModel> uploadImage = RestService
                .fileServerFactory()
                .uploadImage(RestService.obtainTextMultipart(id),
                        imageBody,
                        RestService.obtainTextMultipart(type),
                        RestService.obtainTextMultipart(purpose));

        uploadImage.enqueue(new Callback<UploadAvatarResponseModel>() {
            @Override
            public void onResponse(Call<UploadAvatarResponseModel> call, Response<UploadAvatarResponseModel> response) {
                if (!RestService.handleError(response, listener)) return;
                listener.onUpload(response.body().getImageModel().getPath());
            }

            @Override
            public void onFailure(Call<UploadAvatarResponseModel> call, Throwable t) {
                t.printStackTrace();
                listener.onError(RestService.getEmptyError());
            }
        });
    }
}
