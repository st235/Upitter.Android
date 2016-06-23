package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Alex on 09.06.2016.
 */
public final class Assets {

    private static AssetManager manager;

    private Assets() {}

    public static void init(Context context) {
        manager = context.getAssets();
    }

    public static String obtainJSONRepresentation(@NonNull String jsonName) throws IOException {
        InputStream inputStream = manager.open(jsonName);
        int inputStreamLength = inputStream.available();
        byte[] outputStream = new byte[inputStreamLength];
        inputStream.read(outputStream);
        inputStream.close();
        return new String(outputStream);
    }
}
