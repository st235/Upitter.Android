package com.github.sasd97.upitter.services;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.sasd97.upitter.events.OnQueueListener;
import com.github.sasd97.upitter.models.response.fileServer.FileResponseModel;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by alexander on 12.07.16.
 */

public class ImagesUploadQueue extends AsyncTask<String, Integer, ArrayList<FileResponseModel>> {

    private static final String TAG = "Image Upload Queue";
    private static ImagesUploadQueue imagesUploadQueue;

    private String uid;
    private OnQueueListener<List<FileResponseModel>> listener;

    private ImagesUploadQueue(@NonNull String uid,
                              @NonNull OnQueueListener listener) {
        this.listener = listener;
        this.uid = uid;
    }

    public static void executeQueue(@NonNull String accessToken,
                                    @NonNull OnQueueListener listener,
                                    @NonNull String ...path) {
        imagesUploadQueue = new ImagesUploadQueue(accessToken, listener);
        imagesUploadQueue.execute(path);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<FileResponseModel> doInBackground(String... paths) {
        ArrayList<FileResponseModel> result = new ArrayList<>();

        for (String path: paths) {
            Call<FileResponseModel> call = RestService
                    .fileServerFactory()
                    .uploadPostImage(RestService.obtainTextMultipart(uid),
                            RestService.obtainImageMultipart(new File(path)));
            RestService.logRequest(call);

            try {
                FileResponseModel model = call.execute().body();
                Logger.d(model.toString());
                result.add(model.getResponseModel());
            } catch (IOException io) {
                Logger.e(io, TAG);
            } catch (Exception e) {
                Logger.e(e, TAG);
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<FileResponseModel> arrayList) {
        super.onPostExecute(arrayList);
        listener.onQueueCompete(arrayList);
    }
}