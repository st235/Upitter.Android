package com.github.sasd97.upitter;

import android.app.Application;

import com.github.sasd97.upitter.utils.Connectivity;
import com.github.sasd97.upitter.utils.Prefs;
import com.orm.SugarContext;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public class Upitter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SugarContext.init(this);
        Connectivity.init(this);
        Prefs.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
