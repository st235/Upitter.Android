package com.github.sasd97.upitter.ui.schemas;

import android.os.Bundle;

import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.ui.base.BaseActivity;

public class CompanyLocationPreviewSchema extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_location_preview);
        setToolbar(R.id.toolbar);
    }

    @Override
    protected void setupViews() {

    }
}
