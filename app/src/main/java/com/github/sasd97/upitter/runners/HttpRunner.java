package com.github.sasd97.upitter.runners;

import android.util.Log;

import org.acra.util.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alexander on 29.07.16.
 */

public class HttpRunner {

    static InputStream is = null;
    static String json = "";

    private HttpRunner() {}

    public static HttpRunner getRunner() {
        return new HttpRunner();
    }

    public String getJSONFromUrl(String path, String encoding) {

        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            json = sb.toString();
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        return json;
    }
}
