package com.github.sasd97.upitter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.sasd97.upitter.BuildConfig;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseActivity;
import com.github.sasd97.upitter.utils.SlidrUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrPosition;

import butterknife.BindView;

/**
 * Created by alexander on 08.07.16.
 */
public class AppInfoActivity extends BaseActivity {

    private final String SCHEMA = "v %1$s";

    @BindView(R.id.app_version) TextView appVersionTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
    }

    @Override
    protected void setupViews() {
        Slidr.attach(this, SlidrUtils.config(SlidrPosition.LEFT));
        appVersionTv.setText(String.format(SCHEMA, BuildConfig.VERSION_NAME));
    }
}
