package com.github.sasd97.upitter.runners;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by alexander on 29.07.16.
 */
public class MapExecutor extends AsyncTask<Void, Void, String> {

    private String url;
    private OnMapExecutionListener listener;

    public interface OnMapExecutionListener {
        void onBuildPath(String response);
    }

    private MapExecutor(@NonNull String url,
                        @NonNull OnMapExecutionListener listener){
        this.url = url;
        this.listener = listener;
    }

    public static void execute(@NonNull String url,
                               @NonNull OnMapExecutionListener listener) {
        MapExecutor mapExecutor = new MapExecutor(url, listener);
        mapExecutor.execute();
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... params) {
        HttpRunner runner = HttpRunner.getRunner();
        return runner.getJSONFromUrl(url, "iso-8859-1");
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null) listener.onBuildPath(result);
    }
}
