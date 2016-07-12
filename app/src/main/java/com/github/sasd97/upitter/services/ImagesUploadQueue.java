package com.github.sasd97.upitter.services;

import android.os.AsyncTask;
import android.util.Log;

import com.github.sasd97.upitter.events.OnQueueListener;
import com.github.sasd97.upitter.models.response.fileServer.UploadAvatarResponseModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by alexander on 12.07.16.
 */


public class ImagesUploadQueue extends AsyncTask<String, Integer, ArrayList<String>> {

    private OnQueueListener listener;
    private static ImagesUploadQueue imagesUploadQueue;

    private ImagesUploadQueue(OnQueueListener listener) {
        this.listener = listener;
    }

    public static void executeQueue(OnQueueListener listener, String ...path) {
        imagesUploadQueue = new ImagesUploadQueue(listener);
        imagesUploadQueue.execute(path);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<String> doInBackground(String... paths) {
        ArrayList<String> result = new ArrayList<>();

        for (String path: paths) {
            Call<UploadAvatarResponseModel> call = RestService
                    .fileServerFactory()
                    .uploadImage(RestService.obtainTextMultipart("1"),
                            RestService.obtainTextMultipart("image"),
                            RestService.obtainTextMultipart("photo"),
                            RestService.obtainImageMultipart(new File(path)));

            try {
                UploadAvatarResponseModel model = call.execute().body();
                result.add(model.getImageModel().getPath());
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> arrayList) {
        super.onPostExecute(arrayList);
        listener.onQueueCompete(arrayList);
    }
}