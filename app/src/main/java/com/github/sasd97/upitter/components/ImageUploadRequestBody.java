package com.github.sasd97.upitter.components;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by alexander on 12.08.16.
 */
public class ImageUploadRequestBody extends RequestBody {

    private static final String MEDIA_TYPE = "image/*";
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public interface UploadCallback {
        void onUpdate(int percentage);
        void onError();
    }

    private File file;
    private UploadCallback callback;

    public ImageUploadRequestBody(@NonNull File file,
                                  @NonNull UploadCallback callback) {
        super();
        this.file = file;
        this.callback = callback;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(MEDIA_TYPE);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = file.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(file);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                handler.post(new ProgressUpdater(uploaded, fileLength));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            callback.onUpdate((int) (100 * mUploaded / mTotal));
        }
    }
}
