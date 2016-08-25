package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.sasd97.upitter.BuildConfig;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.ErrorModel;
import com.github.sasd97.upitter.models.UserModel;
import com.github.sasd97.upitter.services.query.ApplicationInfoQueryService;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.DialogUtils;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.orhanobut.logger.Logger;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.sasd97.upitter.Upitter.getHolder;

/**
 * Created by alexander on 08.07.16.
 */

public class AppInfoActivity extends BaseActivity implements ApplicationInfoQueryService.OnInfoListener {

    private final String SCHEMA = "v %1$s";
    private final String PATTERN = "[^\\d]";
    private final int versionName = Integer.valueOf(BuildConfig.VERSION_NAME.replaceAll(PATTERN, ""));

    @BindView(R.id.app_version) TextView appVersionTv;

    private UserModel userModel;
    private ApplicationInfoQueryService queryService;

    private MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
    }

    @Override
    protected void setupViews() {
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));
        queryService = ApplicationInfoQueryService.getService(this);
        userModel = getHolder().get();

        appVersionTv.setText(String.format(SCHEMA, BuildConfig.VERSION_NAME));
    }

    @Override
    public void onObtainInfo(int code, int version) {
        if (version > versionName) {
            dialog = DialogUtils.showUpdateDialog(this);
        } else {
            dialog = DialogUtils.showNoUpdatesDialog(this);
        }

        dialog.show();
    }

    @Override
    public void onError(ErrorModel error) {

    }

    @OnClick(R.id.app_version)
    public void onCheckInfoClick(View v) {
        queryService.obtainInfo(userModel.getAccessToken());
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}
