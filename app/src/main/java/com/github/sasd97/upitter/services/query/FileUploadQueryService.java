package com.github.sasd97.upitter.services.query;

import android.support.annotation.NonNull;

/**
 * Created by Alexadner Dadukin on 30.06.2016.
 */
public class FileUploadQueryService {

    public interface OnFileUploadListener {
        void onUpload();
        void onError();
    }

    private OnFileUploadListener listener;

    private FileUploadQueryService(OnFileUploadListener listener) {
        this.listener = listener;
    }

    public static FileUploadQueryService getService(OnFileUploadListener listener) {
        return new FileUploadQueryService(listener);
    }

    public void uploadFile(@NonNull String path) {

    }
}
