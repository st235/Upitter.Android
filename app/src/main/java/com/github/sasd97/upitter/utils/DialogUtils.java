package com.github.sasd97.upitter.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.events.OnDeleteListener;
import com.github.sasd97.upitter.models.ErrorModel;

/**
 * Created by alexander on 31.07.16.
 */

public class DialogUtils {

    private DialogUtils() {}

    public static MaterialDialog.Builder showError(ErrorModel errorModel) {
        return null;
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

    public static MaterialDialog showNoAccessDialog(Context context) {
        return new MaterialDialog
                .Builder(context)
                .title(R.string.no_access_title)
                .content(R.string.no_access_content)
                .positiveText(R.string.no_access_ok)
                .build();
    }

    public static MaterialDialog deleteDialog(Context context, final OnDeleteListener listener) {
        return new MaterialDialog
                .Builder(context)
                .title(R.string.delete_post_title_dialog)
                .content(R.string.delete_post_content_dialog)
                .positiveText(R.string.delete_post_ok_dialog)
                .negativeText(R.string.delete_post_cancel_dialog)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onDelete();
                    }
                })
                .build();
    }
}
