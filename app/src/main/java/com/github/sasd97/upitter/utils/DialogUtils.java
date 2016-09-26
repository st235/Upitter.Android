package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.response.pointers.ComplaintPointerModel;

import java.util.List;

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

    public static MaterialDialog showUpdateDialog(Context context) {
        return new MaterialDialog
                .Builder(context)
                .title(R.string.title_update_dialog)
                .content(R.string.content_update_dialog)
                .positiveText(R.string.upload_update_dialog)
                .negativeText(R.string.close_update_dialog)
                .build();
    }

    public static MaterialDialog showNoUpdatesDialog(Context context) {
        return new MaterialDialog
                .Builder(context)
                .title(R.string.title_no_updates_dialog)
                .content(R.string.content_no_updates_dialog)
                .positiveText(R.string.ok_no_updates_dialog)
                .build();
    }
}
