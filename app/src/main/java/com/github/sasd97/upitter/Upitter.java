package com.github.sasd97.upitter;

import android.app.Application;
import android.util.Log;

import com.github.sasd97.upitter.services.RestService;
import com.github.sasd97.upitter.utils.Assets;
import com.github.sasd97.upitter.utils.Authorization;
import com.github.sasd97.upitter.utils.Connectivity;
import com.github.sasd97.upitter.utils.Palette;
import com.github.sasd97.upitter.utils.Prefs;
import com.orm.SugarContext;

import java.util.Locale;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public class Upitter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Assets.init(this);
        Authorization.init(this);
        SugarContext.init(this);
        Connectivity.init(this);
        Palette.init(this);
        Prefs.init(this);
        RestService.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
