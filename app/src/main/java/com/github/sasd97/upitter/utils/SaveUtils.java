package com.github.sasd97.upitter.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.github.sasd97.upitter.events.OnSaveListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex on 09.02.2016.
 */

public class SaveUtils extends AsyncTask<Bitmap, Void, ArrayList<String>>{

    private SaveUtils() {}

    private final File sdCard = Environment.getExternalStorageDirectory();

    private OnSaveListener onSaveListener;
    private static SaveUtils saveUtils;

    public static void save(OnSaveListener listener, Bitmap... bitmaps) {
        saveUtils = new SaveUtils(listener);
        saveUtils.execute(bitmaps);
    }

    private SaveUtils(OnSaveListener listener) {
        onSaveListener = listener;
    }

    @Override
    protected ArrayList<String> doInBackground(Bitmap... bitmaps) {
        ArrayList<String> result = new ArrayList<>();

        for (Bitmap bitmap: bitmaps) {
            File picture = new File(sdCard, Names.getUniqueFileName("png"));
            FileOutputStream outStream = null;

            try {
                outStream = new FileOutputStream(picture);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outStream != null) outStream.close();
                    result.add(picture.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);

        if (strings != null) onSaveListener.onSave(strings);
        else onSaveListener.onSaveError();
    }
}
