package com.github.sasd97.upitter.utils;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

/**
 * Created by alexander on 25.08.16.
 */

public class FileUtils implements Handler.Callback {

    private final int ERROR_MESSAGE_ID = -1;
    private final int COPY_MESSAGE_ID = 21;

    public interface OnInteractionListener {
        void onCopy(File file);
        void onError();
    }

    private OnInteractionListener listener;
    private WeakReference<Handler> handlerWR;

    private FileUtils(@NonNull OnInteractionListener listener) {
        this.listener = listener;
        handlerWR = new WeakReference<>(new Handler(this));
    }

    public static FileUtils getUtil(@NonNull OnInteractionListener listener) {
        return new FileUtils(listener);
    }

    public void copyFile(final File src, final File dest) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileInputStream fis = new FileInputStream(src);
                    FileOutputStream fos = new FileOutputStream(dest);

                    byte[] buffer = new byte[2048];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }

                    Handler handler = handlerWR.get();
                    Message message = Message.obtain(handler, COPY_MESSAGE_ID, dest);
                    if (handler != null) handler.sendMessage(message);

                    fis.close();
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Handler handler = handlerWR.get();
                    if (handler != null) handler.sendEmptyMessage(ERROR_MESSAGE_ID);
                }
            }
        }).start();
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case ERROR_MESSAGE_ID:
                listener.onError();
                break;
            case COPY_MESSAGE_ID:
                if (!(message.obj instanceof File)) break;
                File destination = (File) message.obj;
                listener.onCopy(destination);
                break;
        }
        return false;
    }
}
