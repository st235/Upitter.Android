package com.github.sasd97.upitter.runners;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.github.sasd97.upitter.utils.CollageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by alexander on 19.07.16.
 */
public class DownloadImageRunner extends AsyncTask<String, Void, List<Bitmap>> {

    public interface OnDownloadListener {
        void onDownloaded(List<Bitmap> models);
        void onError();
    }

    private Context context;
    private OnDownloadListener listener;

    private DownloadImageRunner(Context context, OnDownloadListener listener) {
         this.context = context;
         this.listener = listener;
    }

    public static <T extends Activity & OnDownloadListener> void upload(T context, String... urls) {
        upload(context.getApplicationContext(), context, urls);
    }
    
    public static <T extends Fragment & OnDownloadListener> void upload(T context, String... urls) {
        upload(context.getActivity().getApplicationContext(), context, urls);
    }

    public static <T extends Context & OnDownloadListener> void upload(T context, String... urls) {
        upload(context, context, urls);
    }

    public static void upload(Context context, OnDownloadListener listener, String... urls) {
        DownloadImageRunner runner = new DownloadImageRunner(context, listener);
        runner.execute(urls);
    }

    @Override
    protected List<Bitmap> doInBackground(String... urls) {
        try {
            List<Bitmap> list = new ArrayList<>(urls.length);

            for (String url : urls) {
                Bitmap bitmap = Glide
                        .with(context)
                        .load(url)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();

                list.add(bitmap);
            }

            return list;
        } catch (InterruptedException ine) {
            ine.printStackTrace();
            return null;
        } catch (ExecutionException exe) {
            exe.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmaps) {
        super.onPostExecute(bitmaps);

        if (bitmaps == null || bitmaps.size() == 0) {
            listener.onError();
            return;
        }

        listener.onDownloaded(bitmaps);
    }
}
