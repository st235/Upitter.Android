package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Alexander Dadukin on 08.05.2016.
 */
public class Keyboard {

    private static InputMethodManager inputManager = null;

    private Keyboard() {}

    public static void init(Context context) {
        inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void hide(IBinder token) {
        inputManager.hideSoftInputFromWindow(token, 0);
    }
}
