package com.github.sasd97.upitter.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;

/**
 * Created by alexander on 31.07.16.
 */
public class DialogUtils {

    private DialogUtils() {}

    public static MaterialDialog.Builder showError(ErrorModel errorModel) {
        return null;
    }

    public static MaterialDialog showDebugInfo(Context context) {
        return new MaterialDialog
                .Builder(context)
                .title(R.string.debug_dialog_title)
                .content(R.string.debug_dialog_content)
                .positiveText(R.string.debug_dialog_confirm)
                .cancelable(false)
                .build();
    }

    public static MaterialDialog showProgressDialog(Context context) {
        return new MaterialDialog
                .Builder(context)
                .content(R.string.progress_dialog_title)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }
}
